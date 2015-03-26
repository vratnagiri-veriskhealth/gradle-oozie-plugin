package org.github.mansur.oozie.extensions

import java.util.Map;
import org.github.mansur.oozie.beans.*

/**
 * @author Muhammad Ashraf
 * @since 7/27/13
 */
class OozieWorkflowExtension {
    String name
	String start
    EndNode end=[name:"end"]
	WorkflowNode defaultErrorNode
    List<WorkflowNode> actions
	GlobalNode global
	List<ParameterNode> parameters=new ArrayList<ParameterNode>();
    List<CredentialNode> credentials
	SlaNode sla
    File outputDir
	
	public void fixActions() {
		Set<String> set=new HashSet<String>();
		boolean hasEndNode=false;
		actions.each{
			assert !set.contains(it.name), "action with name $it.name is defined more than once"
			if(it instanceof EndNode){
				assert !hasEndNode, "workflow can have only one end node."
				hasEndNode=true
			}
			set.add(it.name)
		}
		if(!hasEndNode){
			actions.add(end)
		}
		if(defaultErrorNode && !set.contains(defaultErrorNode.name)){
			actions.add(defaultErrorNode)
		}
		fixDefaultTerminalNodes(end,actions)
	}
	
	void fixTerminalNode(WorkflowNode okEndNode,WorkflowNode node){
		if(node instanceof ActionNode){
			if(node.ok==null){
				node.ok=okEndNode
			}
			if(node.error==null && defaultErrorNode){
				node.error=defaultErrorNode;
			}
		}else if(node instanceof ForkJoinNode){
			fixDefaultTerminalNodes(node,node.actions)
		}
	}

	private void fixDefaultTerminalNodes(WorkflowNode okEndNode,List<WorkflowNode> actions) {
		actions.each{
			fixTerminalNode(it, okEndNode)
		}
	}
	private void fixParameters() {
		Set<String> set=new HashSet<String>();
		parameters.each{
			assert !set.contains(it.name), "parameter with name $it.name is defined more than once"
			set.add(it.name)
		}
	}
	private void fixCredentials() {
		Set<String> set=new HashSet<String>();
		credentials.each{
			assert !set.contains(it.name), "credential with name $it.name is defined more than once"
			set.add(it.name)
		}
	}
	
	public void fixExtension(){
		fixActions()
		fixParameters()
		fixCredentials()
	}
	
	def GlobalNode global(Closure c){ new GlobalNode(c.call())}
	
	def void "parameter declare"(String name, String defaultValue, String description){
		assert parameters.find {
			it.name==name
		} == null, "parameter with name $name was allready declared"
		parameters.add(new ParameterNode(name:name,defaultValue:defaultValue,description:description))
	}
	
	def String parameter(String name){
		ParameterNode p=parameters.find {
			it.name==name
		}
		assert p, "parameter $name was not declared"
		return "\${$name}"
	}
	
	def HiveNode hive(Closure c) { new HiveNode(c.call()) }
    def PigNode pig(Closure c) { new PigNode(c.call()) }
    def JavaNode java(Closure c) { new JavaNode(c.call()) }
    def MapReduceNode mapReduce(Closure c)  { new MapReduceNode(c.call()) }
    def ShellNode shell(Closure c)  { new ShellNode(c.call()) }
    def SshNode ssh(Closure c)  { new SshNode(c.call()) }
    def FsNode fs(Closure c) { new FsNode(c.call()) }
    def EmailNode email(Closure c) { new EmailNode(c.call()) }
    def DistCpNode distCp(Closure c) { new DistCpNode(c.call()) }

	def KillNode kill(Closure c) { new KillNode(c.call()) }
	def DecisionNode decision(Closure c) { new DecisionNode(c.call()) }
	def ForkJoinNode forkJoin(Closure c) { new ForkJoinNode(c.call()) }
		
	def HcatCredentialNode hcatCredentials(Closure c) { new HcatCredentialNode(c.call()) }
	
}
