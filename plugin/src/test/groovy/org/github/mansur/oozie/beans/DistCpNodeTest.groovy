package org.github.mansur.oozie.beans;

import static org.junit.Assert.*
import org.junit.Test
import static BuilderTestUtils.assertXml

class DistCpNodeTest {
  def args = [name: 'copystuff', ok: 'next', error: 'fail', jobTracker: 'jobtracker.domain.xyz', nameNode: 'namenode.domain.xyz', args: ['hdfs://nn1:8020/user/me/input.txt', 'hdfs://nn2/path/to/output.txt']]

  @Test
  public void testBuildXml() {
    assertXml(
      new DistCpNode(args), """
  <action name="copystuff">
    <distcp xmlns="uri:oozie:distcp-action:0.2">
      <job-tracker>jobtracker.domain.xyz</job-tracker>
      <name-node>namenode.domain.xyz</name-node>
      <args>hdfs://nn1:8020/user/me/input.txt</args>
      <args>hdfs://nn2/path/to/output.txt</args>
    </distcp>
    <ok to="next"/>
    <error to="fail"/>
  </action>
""")
  }

  @Test
  public void testBuildXmlCaptureOuptut() {
    assertXml(
      new DistCpNode(args + [captureOutput: true]), """
  <action name="copystuff">
    <distcp xmlns="uri:oozie:distcp-action:0.2">
      <job-tracker>jobtracker.domain.xyz</job-tracker>
      <name-node>namenode.domain.xyz</name-node>
      <args>hdfs://nn1:8020/user/me/input.txt</args>
      <args>hdfs://nn2/path/to/output.txt</args>
      <capture-output/>
    </distcp>
    <ok to="next"/>
    <error to="fail"/>
  </action>
""")
  }
}
