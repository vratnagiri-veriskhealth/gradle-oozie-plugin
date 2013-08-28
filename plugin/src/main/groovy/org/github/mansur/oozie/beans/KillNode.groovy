package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

import java.util.Map;

class KillNode extends WorkflowNode {
  private static final long serialVersionUID = 1L

  String message

  @Override
  protected Map<String, String> rawMap() {
    super.rawMap() + [type: 'kill', message: message]
  }

  @Override
  public void buildXml(MarkupBuilder xml, CommonProperties common) {
    xml.kill(name: name) {
      message(message)
    }
  }

}
