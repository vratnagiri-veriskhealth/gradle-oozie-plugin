package org.github.mansur.oozie.beans

import java.io.Serializable;
import java.util.Map;

abstract class WorkflowNode implements Serializable {
  String name
  String type

  protected Map<String, String> prune(Map<String, String> map) {
    map.findAll { it.value != null }
  }

  Map<String, String> toMap() {
    prune([name: name, type: type])
  }
}
