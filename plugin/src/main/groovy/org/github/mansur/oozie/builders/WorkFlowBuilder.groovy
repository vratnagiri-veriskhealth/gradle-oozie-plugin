/*
 * Copyright 2013. Muhammad Ashraf
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.github.mansur.oozie.builders

import groovy.xml.MarkupBuilder

import org.github.mansur.oozie.beans.ActionNode
import org.github.mansur.oozie.beans.CredentialNode
import org.github.mansur.oozie.beans.DecisionNode;
import org.github.mansur.oozie.beans.ForkNode;
import org.github.mansur.oozie.beans.JoinNode
import org.github.mansur.oozie.beans.KillNode;
import org.github.mansur.oozie.beans.WorkflowNode
import org.github.mansur.oozie.tasks.OozieWorkflowTask

/**
 * @author Muhammad Ashraf
 * @since 7/24/13
 */
class WorkFlowBuilder {
    def String buildWorkflow(OozieWorkflowTask wf) {
        def actions = wf.getWorkflowActions()
        def graph = createDAG(actions, wf.end)
        def writer = new StringWriter()
        def workflow = new MarkupBuilder(writer)
        workflow.'workflow-app'('xmlns': "$wf.namespace", name: "$wf.name") {
            if (wf.credentials != null && !wf.credentials.isEmpty()) {
              List<CredentialNode> credentialNodes = wf.credentials;
              workflow.'credentials' {
                credentialNodes.each { cred ->
                  credential(name: cred.name, type: cred.type) {
                    cred.properties?.each { propertyName, propertyValue ->
                      property {
                        name(propertyName)
                        value(propertyValue)
                      }
                    }
                  }
                }
              }
            }
            start(to: actions.isEmpty() ? wf.end : graph.findHead())
            graph.tSort().each {
              findAction(it.toString(), actions).buildXml(workflow, wf.common)
            }
            if (wf.sla != null) {
              wf.sla.buildXml(workflow, wf.common)
            }
            end(name: wf.end)
        }
        writer.toString()
    }

    def String buildJobXML(HashMap<String, Object> props) {
        String result = null;
        if (!props.isEmpty()) {
            def writer = new StringWriter()
            def xml = new MarkupBuilder(writer)
            xml.configuration {
                props.each { k, v ->
                    property {
                        name(k)
                        value(v)
                    }
                }
            }
            result = writer.toString()
        }
        result
    }

    /**
     * Creates Direct Acyclic Graph of all the actions
     * @param actions
     * @return
     */
    private DirectedGraph createDAG(List<WorkflowNode> actions, String endName) {
        def graph = new DirectedGraph();
        Map<String, DirectedGraph.Node> nodesMap = getNodeMap(actions)
        def nodes = nodesMap.values()
        nodes.each { n ->
            def nodeName = n.toString()
            if (nodeName == endName) {
              return;
            }
            def workflowNode = findAction(nodeName, actions)
            if (workflowNode instanceof ForkNode) {
                handleFork(workflowNode, nodesMap, n)
            } else if (workflowNode instanceof JoinNode) {
                handleJoin(workflowNode, nodesMap, n, endName)
            } else if (workflowNode instanceof DecisionNode) {
                handleDecision(workflowNode, nodesMap, n, endName)
            } else if (workflowNode instanceof KillNode) {
              // You can check in any time, but you can never leave
            }
            else {
                def action = (ActionNode) workflowNode
                def okNodeName = action.ok
                def okNode = nodesMap.get(okNodeName)
                if (okNode != null) {
                    n.addEdge(okNode)
                }
                else if (okNodeName != endName) {
                  throw new IllegalStateException("node " + n.name + " maps ok to non-existant node " + okNodeName);
                }
                def failNodeName = action.error
                def failNode = nodesMap.get(failNodeName)
                if (failNode != null) {
                    n.addEdge(failNode)
                }
                else if (failNodeName != endName) {
                  throw new IllegalStateException("node " + n.name + " maps fail to non-existant node " + failNodeName);
                }
            }
            graph.addNode(n)
        }
        return graph
    }

    private void handleDecision(
      DecisionNode action, Map<String, DirectedGraph.Node> nodesMap, DirectedGraph.Node n, String endName) {
        action.cases?.each { c ->
            def to = c.to
            def toNode = nodesMap.get(to)
            if (toNode != null) {
                n.addEdge(toNode)
            }
            else if (to != endName) {
              throw new IllegalStateException("decision node " + n.name + " maps to non-existant node " + to);
            }
        }
        String defaultTargetName = action.defaultCase
        if (defaultTargetName != null) {
          def defaultTarget = nodesMap.get(defaultTargetName);
          if (defaultTarget != null) {
            n.addEdge(defaultTarget);
          }
          else if (defaultTargetName != endName) {
            throw new IllegalStateException(
              "decision node " + n.name + " defaults to non-existant node " + defaultTargetName);
          }
        }
    }

    private void handleJoin(
      JoinNode action, Map<String, DirectedGraph.Node> nodesMap, DirectedGraph.Node n, String endName) {
        def to = action.to
        def toNode = nodesMap.get(to)
        if (toNode != null) {
            n.addEdge(toNode)
        }
        else if (to != endName) {
          throw new IllegalStateException("join node " + n.name + " forwards to non-existant node " + to);
        }
    }

    private void handleFork(ForkNode action, Map<String, DirectedGraph.Node> nodesMap, n) {
        def paths = action.paths
        paths?.each { p ->
            def toNode = nodesMap.get(p)
            if (toNode != null) {
                n.addEdge(toNode)
            }
            else {
              throw new IllegalStateException("node " + n.name + " forks to non-existant node " + p);
            }
        }
    }

    private Object findAction(String name, List<Object> actions) {
        actions.find { name == it.name }
    }

    def Object findBuilder(String type) {
        def builder = registry.get(type)
        if (builder == null) {
            throw new IllegalArgumentException(String.format("Invalid action type %s, supported action types are %s", type, registry.keySet().toString()))
        }
        builder
    }

    private HashMap<String, DirectedGraph.Node> getNodeMap(List<Object> actions) {
        def nodesMap = new HashMap<String, DirectedGraph.Node>()
        actions.each {
            String name = it.name
            if (name == null || name.length() <= 0) {
                throw new IllegalStateException("Found action without a name!")
            }
            def node = new DirectedGraph.Node(name)
            nodesMap.put(name, node)
        }
        nodesMap
    }
}
