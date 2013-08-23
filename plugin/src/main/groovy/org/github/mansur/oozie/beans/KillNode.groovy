package org.github.mansur.oozie.beans

import java.util.Map;

class KillNode extends WorkflowNode {
  private static final long serialVersionUID = 1L

  KillNode() { type = 'kill' }

  String message

  Map<String, String> toMap() {
    prune(super.toMap() + [message: message])
  }
}
