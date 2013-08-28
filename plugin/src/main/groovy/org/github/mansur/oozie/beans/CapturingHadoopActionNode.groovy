package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

import java.util.Map;

abstract class CapturingHadoopActionNode extends HadoopActionNode {
  private static final long serialVersionUID = 1L

  Boolean captureOutput

  @Override
  protected Map<String, String> rawMap() {
    return super.rawMap() + CapturingUtils.captureMapEntry(captureOutput)
  }
}
