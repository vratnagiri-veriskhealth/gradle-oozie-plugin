package org.github.mansur.oozie.beans

import java.util.Map

class ShellNode extends CapturingHadoopActionNode {
  private static final long serialVersionUID = 1L

  String exec
  List<String> args
  List<String> env

  @Override
  protected Map<String, String> rawMap() {
    super.rawMap() + [ type: 'shell', exec: exec, args: args, envVar: env ]
  }
}
