package org.github.mansur.oozie.beans

abstract class ActionNode extends WorkflowNode {
  String cred
  String ok
  String error

  Map<String, String> toMap() {
    prune(super.toMap() + [cred: cred, ok: ok, error: error])
  }
}
