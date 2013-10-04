package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

class PigNode extends HadoopActionNode {
  private static final long serialVersionUID = 1L

  String script
  List<String> params

  @Override
  public void buildXml(MarkupBuilder xml, CommonProperties common) {
    actionXml(xml, common) {
      xml.'pig' {
        hadoopActionXml(xml, common) {
          xml.script(script)
          (params ?: common.params)?.each { xml.param(it) }
        }
      }
    }
  }
}
