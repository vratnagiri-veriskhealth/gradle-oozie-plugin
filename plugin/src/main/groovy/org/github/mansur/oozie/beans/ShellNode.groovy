package org.github.mansur.oozie.beans

import org.codehaus.groovy.control.customizers.ImportCustomizer.Import;

import groovy.xml.MarkupBuilder;
import static org.github.mansur.oozie.beans.NodeXmlUtils.*; 

final class ShellNode extends HadoopActionNode {
  private static final long serialVersionUID = 1L

  String exec
  List<String> args
  List<String> env

  Boolean captureOutput;
 
  @Override
  public void buildXml(MarkupBuilder xml) {
    actionXml(xml) {
      xml.'shell'(xmlns: "uri:oozie:shell-action:0.1") {
        hadoopActionXml(xml) {
          xml.'exec'(exec)
          args?.each { xml.argument(it) }
          env?.each { xml.'env-var'(it) }
        }
        addBooleanEmptyNode(xml,'capture-output', captureOutput)
      }
    }
  }
}
