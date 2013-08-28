package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

import java.util.Map

import org.github.mansur.oozie.builders.BaseBuilder

class SubWorkflowNode extends ActionNode implements NodeBuilder {
  private static final long serialVersionUID = 1L

  String appPath
  Boolean propagateConfiguration;
  Map<String, String> configuration

  @Override
  protected Map<String, String> rawMap() {
    super.rawMap() +
      [ type: 'sub-workflow', appPath : appPath, propagateConfiguration: propagateConfiguration, configuration: configuration ]
  }
  @Override
  public void buildXml(MarkupBuilder xml, CommonProperties common) {
    actionXml(xml, common) {
      xml.'sub-workflow' {
        'app-path'(appPath)
        if (propagateConfiguration ?: common.propagateConfiguration ?: false) {
          xml.'propagate-configuration'
        }
        addProperties(xml, 'configuration', configuration ?: common.configuration)
      }
    }
  }
}
