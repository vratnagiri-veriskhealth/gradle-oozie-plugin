package org.github.mansur.oozie.beans



import groovy.xml.MarkupBuilder;

final class ForkJoinNode extends WorkflowNode{

	List<WorkflowNode> actions
	List<String> startActions
	String to
	
	@Override
	public void buildXml(MarkupBuilder xml) {
		xml.fork(name: forkName()) {
			if(startActions){
				ForkJoinNodeTest thisNode=this
				startActions.each { actionName->
					assert actions.find{
						it.name=actionName
					} !=null ,"Action with name $actionName wasn't found among $thisNode.name fork node's actions";
					xml.path(start: actionName) 
				}
			}else{
				actions.each{
					xml.path(start: it.name)
				}
			}
		}
	}
	
	public void buildJoinXml(MarkupBuilder xml){
		xml.join(name: joinName(), to: to)
	}
	
	public String forkName(){
		name+'_fork'
	}
	
	public String joinName(){
		name+'_join'
	}

}
