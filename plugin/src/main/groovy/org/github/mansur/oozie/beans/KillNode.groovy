package org.github.mansur.oozie.beans

import java.util.Map;

class KillNode extends WorkflowNode {
  KillNode() { type = 'kill' }

  String message

  Map<String, String> toMap() {
    prune(super.toMap() + [message: message])
  }
}
