package org.github.mansur.oozie.beans

import java.util.Map

class PigNode extends HadoopActionNode {
  private static final long serialVersionUID = 1L

  PigNode() { super('pig'); }

  String script
  Map<String, String> params

  @Override
  protected Map<String, String> rawMap() {
    super.rawMap() + [ script: script, params: params ]
  }
}
