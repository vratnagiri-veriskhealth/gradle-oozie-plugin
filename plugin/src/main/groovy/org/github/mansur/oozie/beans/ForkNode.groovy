package org.github.mansur.oozie.beans

import java.util.Map;

class ForkNode extends WorkflowNode {
  private static final long serialVersionUID = 1L

  public ForkNode() { super('fork') }
  List<String> paths

  @Override
  protected Map<String, String> rawMap() {
    super.rawMap() + [paths: paths]
  }
}
