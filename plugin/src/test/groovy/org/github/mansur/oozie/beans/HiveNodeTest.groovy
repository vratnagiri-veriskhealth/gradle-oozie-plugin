package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;

import org.junit.Test;

class HiveNodeTest extends AbstractHadoopActionNodeTest {

  @Test
  public void testToMap() {
    def hiveArgs = [ script: 'some.hql', params: [a:'b'] ]
    def args = baseArgs + hiveArgs;
    assertEquals(baseResult + hiveArgs + [type: 'hive'], new HiveNode(args).toMap());
  }
}
