package org.github.mansur.oozie.builders;

import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit;
import org.github.mansur.oozie.SAMPLE_XML;
import org.github.mansur.oozie.beans.CommonProperties
import org.gradle.api.Project;

import static org.junit.Assert.*;

import org.junit.Test;

class WorkFlowBuilderTest {
  @Test
  public void testBuildJobXml() {
    def builder = new WorkFlowBuilder()
    def jobXML = builder.buildJobXML([
      "mapred.map.output.compress": "false",
      "mapred.job.queue.name": "queuename"
    ])
   XMLUnit.setIgnoreWhitespace(true)
   def xmlDiff = new Diff(jobXML, SAMPLE_XML.EXPECTED_JOB_XML)
   if (!xmlDiff.similar()) {
      println "expected:"
      new XmlNodePrinter(new PrintWriter(System.out)).print(new XmlParser().parseText(SAMPLE_XML.EXPECTED_JOB_XML))
      println "actual:"
      new XmlNodePrinter(new PrintWriter(System.out)).print(new XmlParser().parseText(jobXML))
      fail("xml different: ${xmlDiff}")
    }
  }


}
