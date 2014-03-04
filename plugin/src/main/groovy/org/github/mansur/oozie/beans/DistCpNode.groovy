package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

class DistCpNode extends ActionNode {
  private static final long serialVersionUID = 1L

  String jobTracker
  String nameNode
  List<String> args
  Boolean captureOutput

  @Override
  public void buildXml(MarkupBuilder xml, CommonProperties common) {
    actionXml(xml, common) {
      xml.'distcp'(xmlns:"uri:oozie:distcp-action:0.2") {
        xml.'job-tracker'(jobTracker)
        xml.'name-node'(nameNode)
        args?.each { xml.'args'(it) }
        CapturingUtils.captureOutputNode(xml, captureOutput)
      }
    }
  }
}
