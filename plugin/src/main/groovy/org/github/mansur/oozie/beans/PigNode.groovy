package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

import java.util.Map

class PigNode extends HadoopActionNode {
  private static final long serialVersionUID = 1L

  String script
  List<String> params

  @Override
  protected Map<String, String> rawMap() {
    super.rawMap() + [ type: 'pig', script: script, params: params ]
  }

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
