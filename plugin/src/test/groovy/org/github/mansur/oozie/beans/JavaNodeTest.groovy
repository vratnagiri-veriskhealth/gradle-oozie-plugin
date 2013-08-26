package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;

import org.junit.Test;

class JavaNodeTest extends AbstractHadoopActionNodeTest {

  @Test
  public void testToMap() {
    def javaArgs = [ mainClass: 'com.example.Main', javaOpts: '-XrunFast', args: ['a', 'b'] ]
    def args = baseArgs + javaArgs;
    assertEquals(baseResult + javaArgs + [type: 'java'], new JavaNode(baseArgs + javaArgs).toMap());
  }

  @Test
  public void testCaptureOutput() {
    def javaArgs = [ mainClass: 'com.example.Main', javaOpts: '-XrunFast', args: ['a', 'b'] ]
        def args = baseArgs + javaArgs;
    assertEquals(
      baseResult + javaArgs + [type: 'java', captureOutput: true],
      new JavaNode(baseArgs + javaArgs + [captureOutput: true]).toMap());
  }
}
