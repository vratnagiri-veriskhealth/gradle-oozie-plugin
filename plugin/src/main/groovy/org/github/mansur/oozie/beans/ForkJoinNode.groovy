package org.github.mansur.oozie.beans



import java.util.List;

import groovy.lang.Closure;
import groovy.xml.MarkupBuilder;

final class ForkJoinNode extends WorkflowNode{

	List<WorkflowNode> actions=new ArrayList<WorkflowNode>();
	List<String> startActions
	String to
	
	@Override
	public void buildXml(MarkupBuilder xml) {
		xml.fork(name: forkName()) {
			if(startActions){
				ForkJoinNode thisNode=this
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

	public <T extends WorkflowNode> T rightShift(T b){
		to=b.name
		return b;
	}
	
	def HiveNode hive(Closure c) {
		return addActionNode(new HiveNode(),c)
	}
	def PigNode pig(Closure c) {
		return addActionNode(new PigNode(),c)
	}
	def JavaNode java(Closure c) {
		return addActionNode(new JavaNode(),c)
	}
	def MapReduceNode mapReduce(Closure c)  {
		return addActionNode(new MapReduceNode(),c)
	}
	def ShellNode shell(Closure c)  {
		return addActionNode(new ShellNode(),c)
	}
	def SshNode ssh(Closure c)  {
		return addActionNode(new SshNode(),c)
	}
	def FsNode fs(Closure c) {
		return addActionNode(new FsNode(),c)
	}
	def EmailNode email(Closure c) {
		return addActionNode(new EmailNode(),c)
	}
	def DistCpNode distCp(Closure c) {
		return addActionNode(new DistCpNode(),c)
	}

	def KillNode kill(Closure c) {
		return addActionNode(new KillNode(),c)
	}
	def DecisionNode decision(Closure c) {
		return addActionNode(new DecisionNode(),c)
	}
	def ForkJoinNode forkJoin(Closure c) {
		return addActionNode(new ForkJoinNode(),c)
	}

	private <T extends WorkflowNode> T addActionNode(T  node, Closure c){
		c.setDelegate(node)
		c.setResolveStrategy(Closure.DELEGATE_FIRST)
		c.call()
		actions.add(node);
		return node;
	}
}
