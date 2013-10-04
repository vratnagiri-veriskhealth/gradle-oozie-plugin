package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder

class JavaNode extends CapturingHadoopActionNode {
  private static final long serialVersionUID = 1L

  String mainClass
  String javaOpts
  List<String> args

  @Override
  public void buildXml(MarkupBuilder xml, CommonProperties common) {
    actionXml(xml, common) {
      xml.'java' {
        hadoopActionXml(xml, common) {
          xml.'main-class'(mainClass)
          addNode(xml, 'java-opts', javaOpts ?: common.javaOpts)
          args?.each { xml.'arg'(it) }
        }
        CapturingUtils.captureOutputNode(xml, captureOutput)
      }
    }
  }

}
