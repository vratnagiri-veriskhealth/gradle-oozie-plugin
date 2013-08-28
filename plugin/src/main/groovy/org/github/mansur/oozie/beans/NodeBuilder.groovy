package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

interface NodeBuilder {
  void buildXml(MarkupBuilder xml, CommonProperties common);
}
