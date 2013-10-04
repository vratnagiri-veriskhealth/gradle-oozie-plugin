package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

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

  protected void hadoopActionXml(MarkupBuilder xml, CommonProperties common, Closure actionSpecific) {
    xml.'job-tracker'(jobTracker ?: common.jobTracker)
    xml.'name-node'(nameNode ?: common.nameNode)
    prepareNodes(xml, delete, mkdir)
    xml.'job-xml'(jobXml ?: common.jobXml)
    addProperties(xml, 'configuration', configuration ?: common.configuration)
    actionSpecific.call()
    (file ?: common.file)?.each { xml.file(it) }
    (archive ?: common.archive)?.each { xml.archive(it) }
  }
}
