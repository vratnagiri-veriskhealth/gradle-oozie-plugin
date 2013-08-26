package org.github.mansur.oozie.beans

import java.util.Map

abstract class HadoopActionNode extends ActionNode {
  private static final long serialVersionUID = 1L

  protected HadoopActionNode(String type) { super(type); }

  String jobTracker
  String nameNode
  String jobXml
  List<String> delete
  List<String> mkdir
  String file
  String archive
  Map<String, String> configuration

  @Override
  protected Map<String, String> rawMap() {
    super.rawMap() + [
      configuration: configuration,
      jobTracker: jobTracker,
      namenode: nameNode,
      delete: delete,
      mkdir: mkdir,
      file: file,
      archive: archive]
  }
}
