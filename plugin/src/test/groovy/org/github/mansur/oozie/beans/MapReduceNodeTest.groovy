package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;

import org.junit.Test;

class MapReduceNodeTest extends AbstractHadoopActionNodeTest {

  @Test
  public void testToMap() {
    assertEquals(baseResult + [type: 'mapreduce'], new MapReduceNode(baseArgs).toMap());
  }
}
