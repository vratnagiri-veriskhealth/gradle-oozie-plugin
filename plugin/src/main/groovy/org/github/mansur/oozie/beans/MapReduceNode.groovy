package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

import java.util.Map;

class MapReduceNode extends HadoopActionNode {
  private static final long serialVersionUID = 1L

  @Override
  protected Map<String, String> rawMap() {
    return super.rawMap() + [type: 'mapreduce']
  }

  @Override
  public void buildXml(MarkupBuilder xml, CommonProperties common) {
    actionXml(xml, common) {
      xml.'map-reduce' {
        hadoopActionXml(xml, common) {}
      }
    }
  }
}
