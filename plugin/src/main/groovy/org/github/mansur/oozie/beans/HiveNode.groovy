package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

import java.util.Map

class HiveNode extends HadoopActionNode implements NodeBuilder {
  private static final long serialVersionUID = 1L

  String script
  Map<String, String> params

  @Override
  protected Map<String, String> rawMap() {
    super.rawMap() + [ type: 'hive', script: script, params: params ]
  }
  @Override
  public void buildXml(MarkupBuilder xml, CommonProperties common) {
    actionXml(xml, common) {
      xml.'hive'(xmlns:"uri:oozie:hive-action:0.2") {
        hadoopActionXml(xml, common) {
          xml.script(script)
          (params ?: common.params)?.each { xml.param(it) }
        }
      }
    }
  }
}
