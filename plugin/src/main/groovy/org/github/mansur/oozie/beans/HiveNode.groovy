package org.github.mansur.oozie.beans

import java.util.Map

class HiveNode extends HadoopActionNode {
  private static final long serialVersionUID = 1L

  String script
  Map<String, String> params

  @Override
  protected Map<String, String> rawMap() {
    super.rawMap() + [ type: 'hive', script: script, params: params ]
  }
}
