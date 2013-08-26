package org.github.mansur.oozie.beans

import java.io.Serializable;
import java.util.Map;

abstract class WorkflowNode implements Serializable {
  private static final long serialVersionUID = 1L

  protected WorkflowNode(String type) {
    this.type = type;
  }

  String name
  String type

  protected Map<String, String> prune(Map<String, String> map) {
    map.findAll { it.value != null }
  }

  protected Map<String, String> rawMap() {
    [name: name, type: type]
  }

  final Map<String, String> toMap() {
    prune(rawMap())
  }
}
