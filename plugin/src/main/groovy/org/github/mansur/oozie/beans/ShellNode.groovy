package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

class ShellNode extends HadoopActionNode {
  private static final long serialVersionUID = 1L

  String exec
  List<String> args
  List<String> env

  Boolean captureOutput;
  @Override
  public void buildXml(MarkupBuilder xml, CommonProperties common) {
    actionXml(xml, common) {
      xml.'shell'(xmlns: "uri:oozie:shell-action:0.1") {
        hadoopActionXml(xml, common) {
          xml.'exec'(exec)
          args?.each { xml.argument(it) }
          env?.each { xml.'env-var'(it) }
        }
        CapturingUtils.captureOutputNode(xml, captureOutput)
      }
    }
  }
}
