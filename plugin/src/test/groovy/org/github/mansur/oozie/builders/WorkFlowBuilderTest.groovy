package org.github.mansur.oozie.builders;

import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit;
import org.github.mansur.oozie.beans.CommonProperties
import org.github.mansur.oozie.beans.Workflow
import org.gradle.api.Project;

import static org.junit.Assert.*;

import org.junit.Test;

class WorkFlowBuilderTest {

  @Test
  public void testBuildWorkflow() {
    def wf = new Workflow(
      actions: [],
      end: "end_node",
      name: 'oozie_flow',
      common: new CommonProperties(),
      namespace: 'uri:oozie:workflow:0.1'
    )

    String xml = new WorkFlowBuilder().buildWorkflow(wf);

    XMLUnit.setIgnoreWhitespace(true)
    def xmlDiff = new Diff("""
<workflow-app xmlns='uri:oozie:workflow:0.1' name='oozie_flow'>
  <start to='end_node' />
  <end name='end_node' />
</workflow-app>
""",
      xml)
    if (!xmlDiff.similar()) {
      println "expected:"
      new XmlNodePrinter(new PrintWriter(System.out)).print(new XmlParser().parseText(expectedXml))
      println "actual:"
      new XmlNodePrinter(new PrintWriter(System.out)).print(new XmlParser().parseText(stringWriter.toString()))
      fail("xml different: ${xmlDiff}")
    }
  }


}
