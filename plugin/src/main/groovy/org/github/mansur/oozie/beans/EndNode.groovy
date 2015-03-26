package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

final class EndNode extends WorkflowNode implements Serializable{

	@Override
	public void buildXml(MarkupBuilder xml) {
		assert name, "name must not be blank"
		xml.end(name:name)
	}

}
