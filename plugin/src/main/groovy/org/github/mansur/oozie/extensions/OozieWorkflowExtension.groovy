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
	List<WorkflowNode> actions=new ArrayList<WorkflowNode>()
	GlobalNode global
	List<ParameterNode> parameters=new ArrayList<ParameterNode>();
	List<CredentialNode> credentials=new ArrayList<CredentialNode>();
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
				node.ok=okEndNode.name
			}
			if(node.error==null && defaultErrorNode){
				node.error=defaultErrorNode.name;
			}
		}else if(node instanceof ForkJoinNode){
			fixDefaultTerminalNodes(node,node.actions)
		}
	}

	private void fixDefaultTerminalNodes(WorkflowNode okEndNode,List<WorkflowNode> actions) {
		for(WorkflowNode n: actions) {
			fixTerminalNode(okEndNode,n) 
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

	def EndNode end(){
		return end
	}

	def EndNode end(String n){
		end=new EndNode(name:n)
		return end;
	}
	def EndNode end(Closure c){
		def n=c.call()
		if(n instanceof String){
			end=new EndNode(name:n)
		}else if(n instanceof EndNode){
			end=n
		}else{
			throw new IllegalArgumentException("Illegal closure return value $n")
		}
		return end;
	}

	def WorkflowNode fail(){
		return defaultErrorNode
	}
	def WorkflowNode fail(String n){
		defaultErrorNode=new KillNode(name:n)
		return defaultErrorNode;
	}
	def WorkflowNode fail(Closure c){
		def n=c.call()
		if(n instanceof String){
			defaultErrorNode=new KillNode(name:n)
		}else if(n instanceof WorkflowNode){
			defaultErrorNode=n
		}else{
			throw new IllegalArgumentException("Illegal closure return value $n")
		}
		return defaultErrorNode
	}


	def void start(WorkflowNode n){
		start=n.name
	}
	def void start(String n){
		start=n
	}
	def void start(Closure c){
		def n=c.call()
		if(n instanceof String){
			start=n
		}else if(n instanceof WorkflowNode){
			start=n.name
		}else{
			throw new IllegalArgumentException("Illegal closure return value $n")
		}
	}

	def void name(String n){
		name=n
	}
	def void name(Closure c){
		def n=c.call()
		if(n instanceof String){
			name=n
		}else{
			throw new IllegalArgumentException("Illegal closure return value $n")
		}
	}

	def GlobalNode global(Closure c){
		global=new GlobalNode()
		c.setDelegate(global)
		c.setResolveStrategy(Closure.DELEGATE_FIRST)
		c.call()
		return global;
	}

	def void "parameter declare"(String name, String defaultValue, String description){
		assert parameters.find { it.name==name } == null, "parameter with name $name was allready declared"
		parameters.add(new ParameterNode(name:name,defaultValue:defaultValue,description:description))
	}

	def String parameter(String name){
		ParameterNode p=parameters.find { it.name==name }
		assert p, "parameter $name was not declared"
		return "\${$name}"
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

	def HcatCredentialNode hcatCredentials(Closure c) {
		def cred=new HcatCredentialNode(c.call())
		credentials.add(cred);
		return cred;
	}
}
