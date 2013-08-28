package org.github.mansur.oozie.beans;

import java.util.List;
import java.util.Map;

public class CommonProperties {
  String cred
  String jobTracker
  String nameNode
  String jobXml
  String error
  List<String> delete
  List<String> mkdir
  List<String> file
  List<String> archive
  Map<String, String> configuration
  Map<String, String> params
  List<String> env
  String sshHost
  String emailTo
  String emailCc
  Boolean propagateConfiguration

  final Map<String, String> toMap() {
    [
      cred: cred,
      jobTracker: jobTracker,
      namenode: nameNode,
      jobXML: jobXml,
      error: error,
      delete: delete,
      mkdir: mkdir,
      file: file,
      archive: archive,
      configuration: configuration,
      params: params,
      host: sshHost,
      to: emailTo,
      cc: emailCc
    ].findAll { it.value != null }
  }
}
