package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;

import org.junit.Test;

class ShellNodeTest extends AbstractHadoopActionNodeTest {

  @Test
  public void testToMap() {
    def shellArgs = [ exec: '/bin/ls', args: ['/foo', '/bar'], env: ['a=b'] ]
    def args = baseArgs + shellArgs;
    assertEquals(
      baseResult + [ exec: '/bin/ls', args: ['/foo', '/bar'], envVar: ['a=b'], type: 'shell'],
      new ShellNode(args).toMap());
  }

  @Test
  public void testCapture() {
    def shellArgs = [ exec: '/bin/ls', args: ['/foo', '/bar'], env: ['a=b'], captureOutput: true ]
        def args = baseArgs + shellArgs;
    assertEquals(
        baseResult + [ exec: '/bin/ls', args: ['/foo', '/bar'], envVar: ['a=b'], type: 'shell', captureOutput: true ],
        new ShellNode(args).toMap());
  }
}
