package org.github.mansur.oozie.beans;

import static org.junit.Assert.*
import org.junit.Test
import static BuilderTestUtils.assertXml

class SshNodeTest {
  def args = [name: 'remote', ok: 'next', error: 'fail', host: 'example.com', command: 'ls', args: ['/foo']]

  @Test
  public void testToMap() {
    assertEquals(
      [type: 'ssh'] + args,
      new SshNode(args).toMap())
  }

  @Test
  public void testCaptureToMap() {
    assertEquals(
      [type: 'ssh', captureOutput: true] + args,
      new SshNode(args + [captureOutput: true]).toMap())
  }

  @Test
  public void testBuildXml() {
    assertXml(
      new SshNode(args), """
  <action name="remote">
    <ssh>
      <host>example.com</host>
      <command>ls</command>
      <args>/foo</args>
    </ssh>
    <ok to="next"/>
    <error to="fail"/>
  </action>
""")
  }

  @Test
  public void testBuildXmlCaptureOuptut() {
    assertXml(
      new SshNode(args + [captureOutput: true]), """
  <action name="remote">
    <ssh>
      <host>example.com</host>
      <command>ls</command>
      <args>/foo</args>
      <capture-output/>
    </ssh>
    <ok to="next"/>
    <error to="fail"/>
  </action>
""")
  }
}
