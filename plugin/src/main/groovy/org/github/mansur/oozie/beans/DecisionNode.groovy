package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

class DecisionNode extends WorkflowNode {
  private static final long serialVersionUID = 1L

  List<DecisionCaseNode> cases;
  String defaultCase;

  @Override
  public void buildXml(MarkupBuilder xml, CommonProperties common) {
    xml.decision(name: name) {
      'switch' {
        cases.each { c ->
          xml.case(to: c.to, c.condition)
        }
        xml.'default'(to: defaultCase)
      }
    }
  }
}
