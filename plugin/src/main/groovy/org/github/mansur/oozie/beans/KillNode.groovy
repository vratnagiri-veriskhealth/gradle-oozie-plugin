package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

class KillNode extends WorkflowNode {
  private static final long serialVersionUID = 1L

  String message

  @Override
  public void buildXml(MarkupBuilder xml, CommonProperties common) {
    xml.kill(name: name) {
      message(message)
    }
  }

}
