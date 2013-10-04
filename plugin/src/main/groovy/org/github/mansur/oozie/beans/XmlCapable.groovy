package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

import java.io.Serializable;


abstract class XmlCapable implements Serializable {
  private static final long serialVersionUID = 1L

  public abstract void buildXml(MarkupBuilder xml, CommonProperties common);

  protected void addNode(MarkupBuilder xml, String node, String value) {
    if (value != null) {
      xml."$node"(value)
    }
  }
}
