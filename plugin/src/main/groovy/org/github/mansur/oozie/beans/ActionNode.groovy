package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

import java.util.HashMap;
import java.util.List;

abstract class ActionNode extends WorkflowNode {
  private static final long serialVersionUID = 1L

  String cred
  String ok
  String error

  @Override
  protected Map<String, String> rawMap() {
    super.rawMap() + [cred: cred, ok: ok, error: error]
  }

  protected void prepareNodes(MarkupBuilder xml, List<String> delete, List<String> mkdir) {
    if ((delete ?: mkdir) != null) {
      xml.prepare {
        fileWork(xml, delete, mkdir)
      }
    }
  }

  protected fileWork(MarkupBuilder xml, List<String> delete, List<String> mkdir) {
    delete?.each { xml.delete(path: it) }
    mkdir?.each { xml.mkdir(path: it) }
  }

  protected void actionXml(MarkupBuilder xml, CommonProperties common, Closure actionContents) {
    Map<String, String> actionAttributes = [name : name];
    String cred = this.cred ?: common.cred
    if (cred != null && cred.length() > 0) {
      actionAttributes += [cred: cred];
    }
    xml.action(actionAttributes) {
      actionContents.call()
      xml.ok(to: ok)
      xml.error(to: error ?: common.error)
    }
  }
}
