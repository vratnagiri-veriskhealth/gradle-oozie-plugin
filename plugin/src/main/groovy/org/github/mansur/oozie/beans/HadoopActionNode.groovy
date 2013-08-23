package org.github.mansur.oozie.beans

import java.util.Map

abstract class HadoopActionNode extends ActionNode {
  String jobTracker
  String nameNode
  String jobXml
  List<String> delete
  List<String> mkdir
  String file
  String archive
  Map<String, String> configuration

  @Override
  public Map<String, String> toMap() {
    prune(super.toMap() + [
      configuration: configuration,
      jobTracker: jobTracker,
      namenode: nameNode,
      delete: delete,
      mkdir: mkdir,
      file: file,
      arhchive: archive])
  }
}
