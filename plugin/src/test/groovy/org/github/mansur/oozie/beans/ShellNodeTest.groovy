package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;
import static BuilderTestUtils.assertXml
import org.junit.Test;

class ShellNodeTest extends AbstractHadoopActionNodeTest {
  def shellArgs = [ exec: '/bin/ls', args: ['/foo', '/bar'], env: ['a=b'] ]
  def args = baseArgs + shellArgs;

  @Test
  public void testBuildXml() {
    assertXml(
      new ShellNode(args),
      actionXml("shell", "uri:oozie:shell-action:0.1", """
        <exec>/bin/ls</exec>
        <argument>/foo</argument>
        <argument>/bar</argument>
        <env-var>a=b</env-var>
"""))
  }

  @Test
  public void testBuildXmlCapture() {
    assertXml(
      new ShellNode(args + [captureOutput: true]),
      actionXml("shell", "uri:oozie:shell-action:0.1", """
        <exec>/bin/ls</exec>
        <argument>/foo</argument>
        <argument>/bar</argument>
        <env-var>a=b</env-var>
        <capture-output />
"""))
  }

}
