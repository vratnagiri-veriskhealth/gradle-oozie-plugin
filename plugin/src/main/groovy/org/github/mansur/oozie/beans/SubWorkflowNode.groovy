package org.github.mansur.oozie.beans

import java.util.Map
import org.github.mansur.oozie.builders.BaseBuilder

class SubWorkflowNode extends ActionNode {
  private static final long serialVersionUID = 1L

  String appPath
  Boolean propagateConfiguration;
  Map<String, String> configuration

  @Override
  protected Map<String, String> rawMap() {
    super.rawMap() +
      [ type: 'sub-workflow', appPath : appPath, propagateConfiguration: propagateConfiguration, configuration: configuration ]
  }
}
