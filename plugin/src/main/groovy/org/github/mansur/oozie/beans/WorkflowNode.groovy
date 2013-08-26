package org.github.mansur.oozie.beans

import java.io.Serializable;
import java.util.Map;

abstract class WorkflowNode implements Serializable {
  private static final long serialVersionUID = 1L

  String name

  protected Map<String, String> prune(Map<String, String> map) {
    map.findAll { it.value != null }
  }

  protected Map<String, String> rawMap() {
    [name: name]
  }

  final Map<String, String> toMap() {
    prune(rawMap())
  }
}
