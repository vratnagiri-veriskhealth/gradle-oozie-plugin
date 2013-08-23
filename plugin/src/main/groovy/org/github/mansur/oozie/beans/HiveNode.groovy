package org.github.mansur.oozie.beans

import java.util.Map

class HiveNode extends HadoopActionNode {
  private static final long serialVersionUID = 1L

  HiveNode() {
    type = 'hive'
  }

  String script
  Map<String, String> params

  @Override
  public Map<String, String> toMap() {
    prune(super.toMap() + [ script: script, params: params ])
  }
}
