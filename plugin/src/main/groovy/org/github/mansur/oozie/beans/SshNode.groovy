package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

import java.util.Map

class SshNode extends ActionNode {
  private static final long serialVersionUID = 1L

  String host
  String command
  List<String> args
  Boolean captureOutput

  @Override
  protected Map<String, String> rawMap() {
    super.rawMap() + [ type: 'ssh', host: host, command: command, args: args ] +
      CapturingUtils.captureMapEntry(captureOutput)
  }

  @Override
  public void buildXml(MarkupBuilder xml, CommonProperties common) {
    actionXml(xml, common) {
      xml.'ssh'() {
        xml.'host'(host)
        xml.'command'(command)
        args?.each { xml.'args'(it) }
        CapturingUtils.captureOutputNode(xml, captureOutput)
      }
    }
  }
}
