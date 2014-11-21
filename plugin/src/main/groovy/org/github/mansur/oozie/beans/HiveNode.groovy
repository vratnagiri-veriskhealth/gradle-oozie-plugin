package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;


final class HiveNode extends HadoopActionNode {
  private static final long serialVersionUID = 1L

  String script
  Map<String, String> params

  @Override
  public void buildXml(MarkupBuilder xml) {
    actionXml(xml) {
      xml.'hive'(xmlns:"uri:oozie:hive-action:0.2") {
        hadoopActionXml(xml) {
          xml.script(script)
          params?.each { xml.param("${it.key}=${it.value}") }
        }
      }
    }
  }
}
