package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

class FsNode extends ActionNode {
  private static final long serialVersionUID = 1L

  List<String> delete
  List<String> mkdir
  List<FsMoveNode> move
  List<FsChmodNode> chmod
  List<String> touchz

  @Override
  public void buildXml(MarkupBuilder xml, CommonProperties common) {
    actionXml(xml, common) {
      xml.'fs' {
        fileWork(xml, delete, mkdir)
        move?.each {
          xml.'move'(source: it.source, target: it.target)
        }
        chmod?.each { chmod ->
          xml.'chmod'(path: chmod.path, permissions: chmod.permissions, 'dir-files': chmod.dirFiles) {
            if (chmod.recursive) {
              xml.'recursive'()
            }
          }
        }
        touchz?.each { xml.'touchz'(path: it) }
      }
    }
  }
}
