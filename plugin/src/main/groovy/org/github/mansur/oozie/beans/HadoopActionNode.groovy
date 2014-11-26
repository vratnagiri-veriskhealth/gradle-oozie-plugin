package org.github.mansur.oozie.beans

import java.util.List;
import java.util.Map;

import groovy.lang.Closure;
import groovy.xml.MarkupBuilder;
import static org.github.mansur.oozie.beans.NodeXmlUtils.*;

abstract class HadoopActionNode extends ActionNode {
  private static final long serialVersionUID = 1L

  String jobTracker
  String nameNode
  String jobXml
  List<String> delete
  List<String> mkdir
  List<String> file
  List<String> archive
  Map<String, String> configuration

  protected void hadoopActionXml(MarkupBuilder xml, Closure actionSpecific) {
	  addNode(xml, 'job-tracker', jobTracker)
	  addNode(xml, 'name-node', nameNode)
	  prepareNodes(xml, delete, mkdir)
	  addNode(xml, 'job-xml', jobXml)
	  addProperties(xml, 'configuration', configuration)
	  actionSpecific.call()
	  file?.each { xml.file(it) }
	  archive?.each { xml.archive(it) }
  }
}
