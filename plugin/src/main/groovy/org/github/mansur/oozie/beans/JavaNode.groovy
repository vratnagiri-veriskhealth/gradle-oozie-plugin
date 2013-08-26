package org.github.mansur.oozie.beans

import java.util.Map;

class JavaNode extends CapturingHadoopActionNode {
  private static final long serialVersionUID = 1L

  String mainClass
  String javaOpts
  List<String> args

  @Override
  protected Map<String, String> rawMap() {
    return super.rawMap() + [type: 'java', mainClass: mainClass, javaOpts: javaOpts, args: args]
  }
}
