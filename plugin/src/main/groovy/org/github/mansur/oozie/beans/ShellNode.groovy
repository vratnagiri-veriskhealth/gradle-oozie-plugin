package org.github.mansur.oozie.beans

import java.util.Map

class ShellNode extends CapturingHadoopActionNode {
  private static final long serialVersionUID = 1L

  ShellNode() { super('shell'); }

  String exec
  List<String> args
  List<String> env

  @Override
  protected Map<String, String> rawMap() {
    super.rawMap() + [ exec: exec, args: args, envVar: env ]
  }
}
