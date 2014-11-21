package org.github.mansur.oozie.beans

import groovy.lang.Closure;
import groovy.xml.MarkupBuilder;

import java.util.List;
import java.util.Map;

class NodeXmlUtils {

	static void addProperties(MarkupBuilder xml, String nodeName, Map<String, Object> properties) {
		if (properties != null) {
			xml."$nodeName" {
				properties.each { k, v ->
					assert k, "key must not be blank"
					assert v, "value must not be blank"
					xml.property {
						name(k)
						value(v)
					}
				}
			}
		}
	}
	static void prepareNodes(MarkupBuilder xml, List<String> delete, List<String> mkdir) {
		if ((delete ?: mkdir) != null) {
			xml.prepare {
				fsDelete(xml, delete)
				fsMkdir(xml, mkdir)
			}
		}
	}
	static void fsDelete(MarkupBuilder xml, List<String> delete){
		delete?.each { xml.delete(path: it) }
	}
	static void fsMkdir(MarkupBuilder xml, List<String> mkdir){
		mkdir?.each { xml.mkdir(path: it) }
	}
		
	static void addNode(MarkupBuilder xml, String node, String value) {
		if (value != null) {
			xml."$node"(value)
		}
	}

	static void addBooleanEmptyNode(MarkupBuilder xml, String node, Boolean value) {
		if (value!=null && value) {
			xml."$node"()
		}
	}
}
