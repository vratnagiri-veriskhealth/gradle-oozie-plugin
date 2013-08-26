package org.github.mansur.oozie.beans

abstract class ActionNode extends WorkflowNode {
  private static final long serialVersionUID = 1L

  String cred
  String ok
  String error

  protected ActionNode(String type) { super(type) }

  @Override
  protected Map<String, String> rawMap() {
    super.rawMap() + [cred: cred, ok: ok, error: error]
  }
}
