package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

import java.util.List;
import java.util.Map

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

  @Override
  protected Map<String, String> rawMap() {
    super.rawMap() + [
      configuration: configuration,
      jobTracker: jobTracker,
      jobXML: jobXml,
      namenode: nameNode,
      delete: delete,
      mkdir: mkdir,
      file: file,
      archive: archive]
  }

  private void addPrepareNodes(MarkupBuilder xml, List<String> deletes, List<String> dirs) {
    if (deletes != null || dirs != null) {
        xml.prepare {
            addDeleteOrDir(xml, deletes, DELETE)
            addDeleteOrDir(xml, dirs, MKDIR)
        }
    }
  }

  protected void hadoopActionXml(MarkupBuilder xml, CommonProperties common, Closure actionSpecific) {
    xml.'job-tracker'(jobTracker ?: common.jobTracker)
    xml.'name-node'(nameNode ?: common.nameNode)
    // prepare nodes
    if ((delete ?: mkdir) != null) {
      xml.prepare {
        delete?.each { xml.delete(path: it) }
        mkdir?.each { xml.mkdir(path: it) }
      }
    }
    xml.'job-xml'(jobXml ?: common.jobXml)
    addProperties(xml, 'configuration', configuration ?: common.configuration)
    actionSpecific.call()
    (file ?: common.file)?.each { xml.file(it) }
    (archive ?: common.archive)?.each { xml.archive(it) }
  }
}
