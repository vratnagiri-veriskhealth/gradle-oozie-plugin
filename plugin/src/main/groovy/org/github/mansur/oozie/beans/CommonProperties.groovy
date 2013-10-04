package org.github.mansur.oozie.beans;

import java.util.List;
import java.util.Map;

public class CommonProperties implements Serializable {
  private static final long serialVersionUID = 1L

  String cred
  String jobTracker
  String nameNode
  String jobXml
  String error
  List<String> file
  List<String> archive
  Map<String, String> configuration
  Map<String, String> params
  List<String> env
  String sshHost
  String emailTo
  String emailCc
  Boolean propagateConfiguration
  String javaOpts
}
