package org.github.mansur.oozie.beans

import java.util.Map;

import groovy.lang.Closure;
import groovy.xml.MarkupBuilder;
import static NodeXmlUtils.*;

final class GlobalNode extends XmlCapable implements Serializable{
	private static final long serialVersionUID = 1L
	String jobTracker
	String nameNode
	String jobXml
	Map<String,String> configuration;
	
	@Override
	public void buildXml(MarkupBuilder xml) {
		xml.global{
			addNode(xml,'job-tracker',jobTracker)
			addNode(xml,'name-node',nameNode)
			addNode(xml,'job-xml',jobXml)
			addProperties(xml,'configuration',configuration)
		}
	}
	
}
