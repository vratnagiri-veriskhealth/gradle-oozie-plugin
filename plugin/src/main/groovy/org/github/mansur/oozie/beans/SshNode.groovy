package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;
import static org.github.mansur.oozie.beans.NodeXmlUtils.*;

final class SshNode extends ActionNode {
  private static final long serialVersionUID = 1L

  String host
  String command
  List<String> args
  Boolean captureOutput

  @Override
  public void buildXml(MarkupBuilder xml) {
    actionXml(xml) {
      xml.'ssh'() {
        xml.'host'(host)
        xml.'command'(command)
        args?.each { xml.'args'(it) }
        addBooleanEmptyNode(xml,'capture-output', captureOutput)
      }
    }
  }
}
