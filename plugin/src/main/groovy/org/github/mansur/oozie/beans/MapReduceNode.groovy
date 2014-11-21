package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;
import static org.github.mansur.oozie.beans.NodeXmlUtils.*;


final class MapReduceNode extends HadoopActionNode {
  private static final long serialVersionUID = 1L

  @Override
  public void buildXml(MarkupBuilder xml) {
    actionXml(xml) {
      xml.'map-reduce' {
        hadoopActionXml(xml) {}
      }
    }
  }
}
