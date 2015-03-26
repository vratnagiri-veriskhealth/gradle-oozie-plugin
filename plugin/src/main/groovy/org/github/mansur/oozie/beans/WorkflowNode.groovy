package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

abstract class WorkflowNode extends XmlCapable {
  private static final long serialVersionUID = 1L

  String name

  public void setName(String name) {
    this.name = NameChecker.verify(name)
  }

}
