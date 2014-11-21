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
import org.github.mansur.oozie.beans.EndNode;
import org.github.mansur.oozie.beans.ForkJoinNode;
import org.github.mansur.oozie.beans.KillNode;
import org.github.mansur.oozie.beans.WorkflowNode
import org.github.mansur.oozie.extensions.OozieWorkflowExtension;

/**
 * @author Muhammad Ashraf
 * @since 7/24/13
 */
class WorkFlowBuilder {
	class ActionScope{
		ForkJoinNode scopeDelimiter;
		ActionScope parentScope;
		List<WorkflowNode> actions;				
		WorkflowNode findInThisScope(String node){
			def n=actions.find {
				it.name==node
			} 
			if (n){
				return n;
			}else if(scopeDelimiter?.name==node){
				return scopeDelimiter;
			}
		}		
		WorkflowNode findVisibleInThisScope(String node){
			def n=findInThisScope(node) 
			if(!n){
				n=parentScope?.findVisibleInThisScope(node)
			}
			return n;
		}
		boolean isScopeDelimiter(WorkflowNode node){
			return node.is(scopeDelimiter)
		}
		String getScopeName(){
			return scopeDelimiter?'root':scopeDelimiter.name
		}
	}
    def String buildWorkflow(OozieWorkflowExtension wf) {
        def actions = wf.actions
		//def graph = createDAG(actions, wf.end.name)
		def ActionScope actionScope=[actions:actions]
        def writer = new StringWriter()
        def workflow = new MarkupBuilder(writer)
        workflow.'workflow-app'(xmlns: "uri:oozie:workflow:0.5", name: "$wf.name") {
			if(wf.parameters){
				workflow.'parameters'{
					wf.parameters.each { 
						it.buildXml(workflow)
					}
				}
			}
			wf.global?.buildXml(workflow)
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
			WorkflowNode startNode=actionScope.findInThisScope(wf.start)
			assert startNode, "could not find action $wf.start in $actionScope.getScopeName() scope"
			if(actionScope.isScopeDelimiter(startNode)){
				wf.start+="_join"
			}else if(startNode instanceof ForkJoinNode){
				wf.start+="_fork"
			}
			workflow.start(to: wf.start)
			writeActionXml(workflow,actionScope)
//            graph.tSort().each {
//              findAction(it.toString(), actions).buildXml(workflow)
//            }
			
            if (wf.sla != null) {
              wf.sla.buildXml(workflow)
            }
            wf.end.buildXml(workflow)
        }
        writer.toString()
    }
	
	private void writeActionXml(MarkupBuilder workflow,final ActionScope actionScope){
		actionScope.actions.each {
			if(it instanceof ForkJoinNode){
				WorkflowNode toNode=actionScope.findInThisScope(it.to);
				assert toNode,"could not find action $it.to in $actionScope.getScopeName() scope for transition from $it.name join"
				if(actionScope.isScopeDelimiter(toNode)){
					it.to+="_join"
				}else if(toNode instanceof ForkJoinNode){
					it.to+="_fork"
				}
				//build fork
				it.buildXml(workflow)
				ActionScope subScope=[scopeDelimiter:it,parentScope:actionScope,actions:it.actions]
				writeActionXml(workflow,subScope)
				//buildJoin
				it.buildJoinXml(workflow)
			}else if(it instanceof DecisionNode){
				it.decisions.each{entry->
					WorkflowNode toNode=actionScope.findInThisScope(entry.value);
					assert toNode, "could not find action $entry.value in $actionScope.getScopeName() scope for conditional transition from $it.name decision with $k"
					if(actionScope.isScopeDelimiter(toNode)){
						entry.value+="_join"
					}else if(toNode instanceof ForkJoinNode){
						entry.value+="_fork"
					}
				}
				WorkflowNode toNode=actionScope.findInThisScope(it.defaultDecision);
				assert toNode, "could not find action $v in $actionScope.getScopeName() scope for conditional transition from $it.name decision with default condition"
				if(actionScope.isScopeDelimiter(toNode)){
					it.defaultDecision+="_join"
				}else if(toNode instanceof ForkJoinNode){
					it.defaultDecision+="_fork"
				}
				it.buildXml(workflow)
			}else if(it instanceof KillNode){
				it.buildXml(workflow)
			}else if (it instanceof EndNode){
				//do nothing
			}else{
				ActionNode actionNode=it
				WorkflowNode okToNode=actionScope.findInThisScope(actionNode.ok)
				assert okToNode, "could not find action $actionNode.ok in ${actionScope.scopeDelimiter==null?'root':actionScope.scopeDelimiter.name} scope for ok transition from $actionNode.name"
				if(actionScope.isScopeDelimiter(okToNode)){
					actionNode.ok+="_join"
				}else if(okToNode instanceof ForkJoinNode){
					actionNode.ok+="_fork"
				}
				WorkflowNode errorToNode=actionScope.findVisibleInThisScope(actionNode.error)
				assert errorToNode, "could not find action $actionNode.error in $actionScope.getScopeName() scope for error transition from $actionNode.name"
				if(actionScope.isScopeDelimiter(okToNode)){
					actionNode.error+="_join"
				}else if(okToNode instanceof ForkJoinNode){
					actionNode.error+="_fork"
				}				
				actionNode.buildXml(workflow);
			}
		}
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
    private static DirectedGraph createDAG(List<WorkflowNode> actions, String endName) {
        def graph = new DirectedGraph();
        Map<String, DirectedGraph.Node> nodesMap = getNodeMap(actions)
        def nodes = nodesMap.values()
        nodes.each { n ->
            def nodeName = n.toString()
            if (nodeName == endName) {
              return;
            }
            def workflowNode = findAction(nodeName, actions)
            if (workflowNode instanceof ForkJoinNode) {
                handleForkJoin(workflowNode, nodesMap, n)
            } else if (workflowNode instanceof DecisionNode) {
                handleDecision(workflowNode, nodesMap, n, endName)
            } else if (workflowNode instanceof KillNode) {
              // You can check in any time, but you can never leave
            } else {
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
                  throw new IllegalStateException("node " + n.name + " maps error to non-existant node " + failNodeName);
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

    private void handleForkJoin(ForkJoinNode action, Map<String, DirectedGraph.Node> nodesMap, n) {
        def paths = action.paths
        paths?.each { p ->
            def toNode = nodesMap.get(p.toString())
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
