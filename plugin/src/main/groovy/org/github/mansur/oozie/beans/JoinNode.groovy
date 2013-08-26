package org.github.mansur.oozie.beans

import java.util.Map;

class JoinNode extends WorkflowNode {
  String to

  @Override
  protected Map<String, String> rawMap() {
    return super.rawMap() + [type: 'join', to: to]
  }
}
