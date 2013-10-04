package org.github.mansur.oozie.beans;

import groovy.xml.MarkupBuilder;

public class CapturingUtils {
  static captureOutputNode(MarkupBuilder xml, Boolean captureOutput) {
    if (captureOutput ?: false) {
      xml.'capture-output'()
    }
  }
}
