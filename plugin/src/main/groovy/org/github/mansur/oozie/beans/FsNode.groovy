package org.github.mansur.oozie.beans

import java.util.Map;

class FsNode extends ActionNode {
  List<String> delete
  List<String> mkdir
  List<FsMoveNode> move
  List<FsChmodNode> chmod

  FsNode() { super('fs') }

  @Override
  protected Map<String, String> rawMap() {
    return super.rawMap() + [
      delete: delete,
      mkdir: mkdir,
      move: move == null ? null : move.collect { it.toMap() },
      chmod: chmod == null ? null : chmod.collect { it.toMap() }]
  }
}
