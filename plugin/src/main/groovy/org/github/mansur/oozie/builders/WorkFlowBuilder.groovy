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

import org.github.mansur.oozie.beans.CredentialNode
import org.github.mansur.oozie.beans.Workflow
import org.github.mansur.oozie.beans.WorkflowNode

/**
 * @author Muhammad Ashraf
 * @since 7/24/13
 */
class WorkFlowBuilder {
    HashMap<String, Object> registry = [
            "java": new JavaBuilder(),
            "fs": new FSBuilder(),
            "ssh": new SSHBuilder(),
            "mapreduce": new MapReduceBuilder(),
            "shell": new ShellBuilder(),
            "fork": new ForkBuilder(),
            "join": new JoinBuilder(),
            "decision": new DecisionBuilder(),
            "pig": new PigBuilder(),
            "hive": new HiveBuilder(),
            "email": new EmailBuilder(),
            "kill": new KillBuilder(),
            "sub-workflow": new SubWorkflowBuilder()
    ]

    def String buildWorkflow(Workflow wf) {
        def actions = wf.actions
        def graph = createDAG(actions, wf.end)
        def writer = new StringWriter()
        def workflow = new MarkupBuilder(writer)
        workflow.'workflow-app'('xmlns': "$wf.namespace", name: "$wf.name") {
            if (wf.credentials != null) {
              if (wf.credentials instanceof Map && ! wf.credentials.isEmpty()) {
                workflow.'credentials' {
                  wf.credentials.each { k, v ->
                    credential(name: k, type: v.get("type")) {
                      v.get("configuration").each { propertyName, propertyValue ->
                        property {
                          name(propertyName)
                          value(propertyValue)
                        }
                      }
                    }
                  }
                }
              }
              else if (wf.credentials instanceof List && !wf.credentials.isEmpty()) {
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
            }
            start(to: actions.isEmpty() ? wf.end : graph.findHead())
            graph.tSort().each {
                def action = findAction(it.toString(), actions)
                if (action instanceof WorkflowNode) {
                  action.buildXml(workflow, wf.common)
                }
                else {
                  action = asMap(action)
                  def type = action.get("type")
                  def builder = findBuilder(type)
                  println("using maps to specify workflow nodes is deprecated; please convert the map of type ${type}")
                  builder.buildXML(workflow, action, asMap(wf.common))
                }
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
    private DirectedGraph createDAG(List<Object> actions, String endName) {
        def graph = new DirectedGraph();
        Map<String, DirectedGraph.Node> nodesMap = getNodeMap(actions)
        def nodes = nodesMap.values()
        nodes.each { n ->
            def nodeName = n.toString()
            if (nodeName == endName) {
              return;
            }
            def action = asMap(findAction(nodeName, actions))
            def type = action.get("type")
            if (type == "fork") {
                handleFork(action, nodesMap, n)
            } else if (type == "join") {
                handleJoin(action, nodesMap, n, endName)
            } else if (type == "decision") {
                handleDecision(action, nodesMap, n, endName)
            } else if (type != "kill") {
                def okNodeName = action.get("ok")
                def okNode = nodesMap.get(okNodeName)
                if (okNode != null) {
                    n.addEdge(okNode)
                }
                else if (okNodeName != endName) {
                  throw new IllegalStateException("node " + n.name + " maps ok to non-existant node " + okNodeName);
                }
                def failNodeName = action.get("error")
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
      Map<String, Object> action, Map<String, DirectedGraph.Node> nodesMap, DirectedGraph.Node n, String endName) {
        action.get("switch")?.each { c ->
            def to = c.get("to")
            def toNode = nodesMap.get(to)
            if (toNode != null) {
                n.addEdge(toNode)
            }
            else if (to != endName) {
              throw new IllegalStateException("decision node " + n.name + " maps to non-existant node " + to);
            }
        }
        String defaultTargetName = action.get("default");
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
      Map<String, Object> action, Map<String, DirectedGraph.Node> nodesMap, DirectedGraph.Node n, String endName) {
        def to = action.get("to")
        def toNode = nodesMap.get(to)
        if (toNode != null) {
            n.addEdge(toNode)
        }
        else if (to != endName) {
          throw new IllegalStateException("join node " + n.name + " forwards to non-existant node " + to);
        }
    }

    private void handleFork(Map<String, Object> action, Map<String, DirectedGraph.Node> nodesMap, n) {
        def paths = action.get("paths")
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

    private Map asMap(Object o) {
      return (o instanceof Map || o == null) ? o : o.toMap();
    }
    private Object findAction(String name, List<Object> actions) {
        actions.find { name == asMap(it).get("name") }
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
            it = asMap(it)
            String name = it.get("name")
            if (name == null || name.length() <= 0) {
                throw new IllegalStateException("Found action without a name!")
            }
            def node = new DirectedGraph.Node(name)
            nodesMap.put(name, node)
        }
        nodesMap
    }
}
