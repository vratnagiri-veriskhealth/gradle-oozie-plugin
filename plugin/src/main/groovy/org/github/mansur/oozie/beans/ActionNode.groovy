package org.github.mansur.oozie.beans

import groovy.lang.Closure;
import groovy.xml.MarkupBuilder;

import java.util.HashMap;
import java.util.List;

abstract class ActionNode extends WorkflowNode {
  private static final long serialVersionUID = 1L

  String cred
  String ok
  String error
  
  protected void actionXml(MarkupBuilder xml, Closure actionContents) {
	  Map<String, String> actionAttributes = [name : name];
	  if (cred != null && cred.length() > 0) {
		  actionAttributes += [cred: cred];
	  }
	  xml.action(actionAttributes) {
		  actionContents.call()
		  xml.ok(to: ok)
		  xml.error(to: error)
	  }
  }

}
