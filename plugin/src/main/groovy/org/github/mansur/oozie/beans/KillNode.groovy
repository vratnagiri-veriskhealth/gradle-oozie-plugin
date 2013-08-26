package org.github.mansur.oozie.beans

import java.util.Map;

class KillNode extends WorkflowNode {
  private static final long serialVersionUID = 1L

  String message

  @Override
  protected Map<String, String> rawMap() {
    super.rawMap() + [type: 'kill', message: message]
  }
}
