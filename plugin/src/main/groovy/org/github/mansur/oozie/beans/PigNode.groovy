package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;
import static org.github.mansur.oozie.beans.NodeXmlUtils.*;


final class PigNode extends HadoopActionNode {
  private static final long serialVersionUID = 1L

  String script
  List<String> params

  @Override
  public void buildXml(MarkupBuilder xml) {
    actionXml(xml) {
      xml.'pig' {
        hadoopActionXml(xml) {
          xml.script(script)
          params?.each { xml.param(it) }
        }
      }
    }
  }
}
