package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

interface CredentialNode {
  String name;
  String type;
  Map<String, String> properties;
}
