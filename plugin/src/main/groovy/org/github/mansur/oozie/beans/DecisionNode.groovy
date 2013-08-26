package org.github.mansur.oozie.beans

import java.util.Map;

class DecisionNode extends WorkflowNode {
  private static final long serialVersionUID = 1L

  List<DecisionCaseNode> cases;
  String defaultCase;

  DecisionNode() { super('decision') }

  @Override
  protected Map<String, String> rawMap() {
    return super.rawMap() + [switch: cases.collect{ it.toMap() }, default: defaultCase];
  }
}
