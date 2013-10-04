package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

class ForkNode extends WorkflowNode {
  private static final long serialVersionUID = 1L

  List<String> paths

  @Override
  public void buildXml(MarkupBuilder xml, CommonProperties common) {
    xml.fork(name: name) {
      paths.each { xml.path(start: it) }
    }
  }
}
