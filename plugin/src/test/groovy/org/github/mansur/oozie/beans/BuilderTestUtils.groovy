package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import static org.junit.Assert.fail;

class BuilderTestUtils {

  static void assertXml(WorkflowNode node, String expectedXml) {
    def stringWriter = new StringWriter()
    node.buildXml(new MarkupBuilder(new PrintWriter(stringWriter)), new CommonProperties());

    XMLUnit.setIgnoreWhitespace(true)
    def xmlDiff = new Diff(
      expectedXml,
      stringWriter.toString())
    if (!xmlDiff.similar()) {
      println "expected:"
      new XmlNodePrinter(new PrintWriter(System.out)).print(new XmlParser().parseText(expectedXml))
      println "actual:"
      new XmlNodePrinter(new PrintWriter(System.out)).print(new XmlParser().parseText(stringWriter.toString()))
      fail("xml different: ${xmlDiff}")
    }
  }

}
