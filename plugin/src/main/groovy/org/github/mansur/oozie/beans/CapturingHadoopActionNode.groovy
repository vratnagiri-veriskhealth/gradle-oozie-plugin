package org.github.mansur.oozie.beans

import java.util.Map;

abstract class CapturingHadoopActionNode extends HadoopActionNode {
  private static final long serialVersionUID = 1L

  protected CapturingHadoopActionNode(String type) { super(type) }

  Boolean captureOutput

  @Override
  protected Map<String, String> rawMap() {
    return super.rawMap() + (captureOutput == null ? [:] : [captureOutput: captureOutput])
  }
}
