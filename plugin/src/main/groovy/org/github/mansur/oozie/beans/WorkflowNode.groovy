package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

abstract class WorkflowNode implements Serializable {
  private static final long serialVersionUID = 1L

  String name

  public abstract void buildXml(MarkupBuilder xml, CommonProperties common);

  protected Map<String, String> prune(Map<String, String> map) {
    map.findAll { it.value != null }
  }

  protected Map<String, String> rawMap() {
    [name: name]
  }

  final Map<String, String> toMap() {
    prune(rawMap())
  }

  protected void addNode(MarkupBuilder xml, String node, String value) {
    if (value != null) {
      xml."$node"(value)
    }
  }

  protected void addProperties(MarkupBuilder xml, String nodeName, Map<String, Object> properties) {
    if (properties != null) {
      xml."$nodeName" {
        properties.each { k, v ->
          xml.property {
            name(k)
            value(v)
          }
        }
      }
    }
  }
}
