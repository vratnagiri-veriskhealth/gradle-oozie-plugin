package org.github.mansur.oozie.beans

import java.util.Map;

import groovy.xml.MarkupBuilder;

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
}
