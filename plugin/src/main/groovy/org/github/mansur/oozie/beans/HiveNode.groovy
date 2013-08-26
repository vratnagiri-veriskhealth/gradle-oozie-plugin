package org.github.mansur.oozie.beans

import java.util.Map

class HiveNode extends HadoopActionNode {
  private static final long serialVersionUID = 1L

  HiveNode() { super('hive'); }

  String script
  Map<String, String> params

  @Override
  protected Map<String, String> rawMap() {
    super.rawMap() + [ script: script, params: params ]
  }
}
