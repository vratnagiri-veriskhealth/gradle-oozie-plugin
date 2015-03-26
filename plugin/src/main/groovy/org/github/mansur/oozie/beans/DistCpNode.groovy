package org.github.mansur.oozie.beans

import groovy.lang.Closure;
import groovy.xml.MarkupBuilder;
import static org.github.mansur.oozie.beans.NodeXmlUtils.*;

final class DistCpNode extends ActionNode {
  private static final long serialVersionUID = 1L

  String jobTracker
  String nameNode
  List<String> args
  Boolean captureOutput

  @Override
  public void buildXml(MarkupBuilder xml) {
    actionXml(xml) {
      xml.'distcp'(xmlns:"uri:oozie:distcp-action:0.2") {
        xml.'job-tracker'(jobTracker)
        xml.'name-node'(nameNode)
        args?.each { xml.'args'(it) }
        addBooleanEmptyNode(xml,'capture-output', captureOutput)
      }
    }
  }

}
