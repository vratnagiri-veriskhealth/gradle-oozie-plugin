package org.github.mansur.oozie.beans

import java.util.List;

import groovy.lang.Closure;
import groovy.xml.MarkupBuilder
import static org.github.mansur.oozie.beans.NodeXmlUtils.*;
final class JavaNode extends HadoopActionNode {
  private static final long serialVersionUID = 1L

  String mainClass
  String javaOpts
  List<String> args
  Boolean captureOutput;
  @Override
  public void buildXml(MarkupBuilder xml) {
    actionXml(xml) {
      xml.'java' {
        hadoopActionXml(xml) {
          xml.'main-class'(mainClass)
          addNode(xml, 'java-opts', javaOpts)
          args?.each { xml.'arg'(it) }
        }
        addBooleanEmptyNode(xml,'capture-output', captureOutput)
      }
    }
  }
}
