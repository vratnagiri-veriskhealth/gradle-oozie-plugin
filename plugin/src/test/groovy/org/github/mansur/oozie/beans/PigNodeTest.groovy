package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;

import org.junit.Test;

class PigNodeTest extends AbstractHadoopActionNodeTest {

  @Test
  public void testToMap() {
    def pigArgs = [ script: 'some.pig', params: ['a', 'b'] ]
    def args = baseArgs + pigArgs;
    assertEquals(baseResult + pigArgs + [type: 'pig'], new PigNode(args).toMap());
  }
}
