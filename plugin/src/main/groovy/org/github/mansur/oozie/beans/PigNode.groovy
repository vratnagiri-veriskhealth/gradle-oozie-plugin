package org.github.mansur.oozie.beans

import java.util.Map

class PigNode extends HadoopActionNode {
  private static final long serialVersionUID = 1L

  String script
  List<String> params

  @Override
  protected Map<String, String> rawMap() {
    super.rawMap() + [ type: 'pig', script: script, params: params ]
  }
}
