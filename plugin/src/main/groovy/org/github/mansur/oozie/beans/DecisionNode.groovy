package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

import java.util.Map;

class DecisionNode extends WorkflowNode {
  private static final long serialVersionUID = 1L

  List<DecisionCaseNode> cases;
  String defaultCase;

  @Override
  protected Map<String, String> rawMap() {
    return super.rawMap() + [type: 'decision', switch: cases.collect{ it.toMap() }, default: defaultCase];
  }

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
