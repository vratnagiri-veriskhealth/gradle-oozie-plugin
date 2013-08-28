package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

import java.util.Map


class ShellNode extends CapturingHadoopActionNode {
  private static final long serialVersionUID = 1L

  String exec
  List<String> args
  List<String> env

  @Override
  protected Map<String, String> rawMap() {
    super.rawMap() + [ type: 'shell', exec: exec, args: args, envVar: env ]
  }

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
