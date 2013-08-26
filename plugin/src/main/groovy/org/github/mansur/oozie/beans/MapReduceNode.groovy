package org.github.mansur.oozie.beans

import java.util.Map;

class MapReduceNode extends HadoopActionNode {
  private static final long serialVersionUID = 1L

  @Override
  protected Map<String, String> rawMap() {
    return super.rawMap() + [type: 'mapreduce']
  }
}
