package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;

import groovy.xml.MarkupBuilder
import org.junit.Test
import static BuilderTestUtils.assertXml


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
  public void testBuildXml() {
    assertXml( NODE, BEGINNING + END)
  }

  @Test
  public void testBuildXmlWithPropagtion() {
    NODE.setPropagateConfiguration(true)
    assertXml(NODE, BEGINNING + "<propagate-configuration/>" + END)
  }
}
