package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;

import org.junit.Test;

class SshNodeTest {

  @Test
  public void testToMap() {
    def args = [name: 'remote', ok: 'next', error: 'fail', host: 'example.com', command: 'ls', args: ['/foo']]
    assertEquals(
      [type: 'ssh'] + args,
      new SshNode(args).toMap())
  }

  @Test
  public void testCaptureToMap() {
    def args = [name: 'remote', ok: 'next', error: 'fail', host: 'example.com', command: 'ls', args: ['/foo']]
    assertEquals(
      [type: 'ssh', captureOutput: true] + args,
      new SshNode(args + [captureOutput: true]).toMap())
  }

}
