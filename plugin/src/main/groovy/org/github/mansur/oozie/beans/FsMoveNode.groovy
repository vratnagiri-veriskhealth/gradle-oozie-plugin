package org.github.mansur.oozie.beans

class FsMoveNode implements Serializable {
  private static final long serialVersionUID = 1L

  String source
  String target

  Map<String, String> toMap() {
    [source: source, target: target]
  }
}
