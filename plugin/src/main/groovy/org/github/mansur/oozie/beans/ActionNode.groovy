package org.github.mansur.oozie.beans

abstract class ActionNode extends WorkflowNode {
  private static final long serialVersionUID = 1L

  String cred
  String ok
  String error

  Map<String, String> toMap() {
    prune(super.toMap() + [cred: cred, ok: ok, error: error])
  }
}
