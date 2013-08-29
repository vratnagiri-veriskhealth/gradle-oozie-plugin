package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;

import groovy.xml.MarkupBuilder
import org.junit.Test;
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit

class SubWorkflowNodeTest {
  private SubWorkflowNode NODE =
   new SubWorkflowNode(name: 'callout', ok: 'yes', error: 'no', appPath: '/some/path', configuration: [foo: 'bar']);

  private final static String BEGINNING = """
<action name="callout">
  <sub-workflow>
    <app-path>/some/path</app-path>
"""
  private final static String END= """
    <configuration><property><name>foo</name><value>bar</value></property></configuration>
  </sub-workflow>
  <ok to="yes"/>
  <error to="no"/>
</action>
"""

  @Test
  public void testToMap() {
    assertEquals(
      [name: 'callout', ok: 'yes', error: 'no', type: 'sub-workflow', appPath: '/some/path', configuration: [foo: 'bar'] ],
      NODE.toMap());
  }

  @Test
  public void testBuildXml() {
    assertXml( NODE, BEGINNING + END)
  }

  @Test
  public void testBuildXmlWithPropagtion() {
    NODE.setPropagateConfiguration(true)
    assertXml(NODE, BEGINNING + "<propagate-configuration/>" + END)
  }

  protected void assertXml(WorkflowNode node, String expectedXml) {
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
