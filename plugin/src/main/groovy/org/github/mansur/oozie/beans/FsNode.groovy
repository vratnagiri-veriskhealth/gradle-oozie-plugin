package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

import java.util.Map;

class FsNode extends ActionNode {
  private static final long serialVersionUID = 1L

  List<String> delete
  List<String> mkdir
  List<FsMoveNode> move
  List<FsChmodNode> chmod

  @Override
  protected Map<String, String> rawMap() {
    return super.rawMap() + [
      type: 'fs',
      delete: delete,
      mkdir: mkdir,
      move: move == null ? null : move.collect { it.toMap() },
      chmod: chmod == null ? null : chmod.collect { it.toMap() }]
  }

  @Override
  public void buildXml(MarkupBuilder xml, CommonProperties common) {
    actionXml(xml, common) {
      xml.'fs' {
        fileWork(xml, delete, mkdir)
        move?.each {
          xml.'move'(source: it.source, target: it.target)
        }
        chmod?.each {
          xml.'chmod'(path: it.path, permissions: it.permissions, 'dir-files': it.dirFiles)
        }
      }
    }
  }
}
