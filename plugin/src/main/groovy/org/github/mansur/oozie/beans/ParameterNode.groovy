package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

import java.io.Serializable;
import static org.github.mansur.oozie.beans.NodeXmlUtils.*;

final class ParameterNode extends XmlCapable implements Serializable{

	String name;
	String defaultValue;
	String description;
	
	@Override
	public void buildXml(MarkupBuilder xml) {
		assert name, "parameter name is null or empty"
		xml.property{
			xml.name(name)
			addNode("value",defaultValue)
			addNode("description",description)
		}
	}
}
