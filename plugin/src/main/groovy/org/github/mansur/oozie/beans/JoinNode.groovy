package org.github.mansur.oozie.beans

import java.util.Map;

class JoinNode extends WorkflowNode {
  String to

  public JoinNode() { super('join') }

  @Override
  protected Map<String, String> rawMap() {
    return super.rawMap() + [to: to]
  }
}
