package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

class MapReduceNode extends HadoopActionNode {
  private static final long serialVersionUID = 1L

  @Override
  public void buildXml(MarkupBuilder xml, CommonProperties common) {
    actionXml(xml, common) {
      xml.'map-reduce' {
        hadoopActionXml(xml, common) {}
      }
    }
  }
}
