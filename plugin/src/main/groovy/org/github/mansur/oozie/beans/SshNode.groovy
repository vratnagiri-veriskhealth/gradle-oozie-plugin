package org.github.mansur.oozie.beans

import java.util.Map

class SshNode extends ActionNode {
  private static final long serialVersionUID = 1L

  SshNode() { super('ssh'); }

  String host
  String command
  List<String> args
  Boolean captureOutput

  @Override
  protected Map<String, String> rawMap() {
    super.rawMap() + [ host: host, command: command, args: args ] +
      (captureOutput == null ? [:] : [captureOutput : captureOutput ? 'true' : 'false'])
  }
}
