package org.github.mansur.oozie.beans

class DecisionCaseNode implements Serializable {
  private static final long serialVersionUID = 1L

  String to
  String condition

  Map<String, String> toMap() {
    return [to: to, if: condition]
  }
}
