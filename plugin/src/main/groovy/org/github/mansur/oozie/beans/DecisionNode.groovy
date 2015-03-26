package org.github.mansur.oozie.beans

import java.util.Map;

import groovy.lang.Closure;
import groovy.xml.MarkupBuilder;

final class CaseBuilder{

	String when;
	String then;

	def void when(String n){
		when=n
	}
	def void when(Closure c){
		c.setDelegate(this)
		c.setResolveStrategy(Closure.DELEGATE_FIRST);
		def n=c.call()
		if(n instanceof String){
			when=n
		}else{
			throw new IllegalArgumentException("Illegal closure return value $n")
		}
	}

	def void then(WorkflowNode n){
		then=n.name
	}
	def void then(String n){
		then=n
	}
	def void then(Closure c){
		c.setDelegate(this)
		c.setResolveStrategy(Closure.DELEGATE_FIRST)
		def n=c.call()
		if(n instanceof String){
			then=n
		}else if(n instanceof WorkflowNode){
			then=n.name
		}else{
			throw new IllegalArgumentException("Illegal closure return value $n")
		}
	}
}

final class DecisionNode extends WorkflowNode {
	private static final long serialVersionUID = 1L;

	Map<String,String> decisions;
	String defaultDecision;

	@Override
	public void buildXml(MarkupBuilder xml) {
		xml.decision(name: name) {
			'switch' {
				decisions.each { k,v ->
					xml.'case'(to: v, k)
				}
				xml.'default'(to: defaultDecision)
			}
		}
	}
	
	def void branch(Closure c){
		CaseBuilder cb=new CaseBuilder()
		c.setDelegate(cb)
		c.setResolveStrategy(Closure.DELEGATE_FIRST)
		c.call()
		assert cb.when, "No branch condition specified"
		assert cb.then, "No branch target specified"
		assert !decisions.containsKey(cb.when), "Duplicate condition $cb.when"
		decisions.put(cb.when, cb.then)		
	}
	
	def void defaultBranch(WorkflowNode n){
		defaultDecision=n.name
	}
	def void defaultBranch(String n){
		defaultDecision=n
	}
	def void defaultBranch(Closure c){
		c.setDelegate(this)
		c.setResolveStrategy(Closure.DELEGATE_FIRST)
		def n=c.call()
		if(n instanceof String){
			defaultDecision=n
		}else if(n instanceof WorkflowNode){
			defaultDecision=n.name
		}else{
			throw new IllegalArgumentException("Illegal closure return value $n")
		}
	}
}
