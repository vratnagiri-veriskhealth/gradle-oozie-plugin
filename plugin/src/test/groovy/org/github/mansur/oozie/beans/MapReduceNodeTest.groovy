package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;
import static BuilderTestUtils.assertXml

import org.junit.Test;

class MapReduceNodeTest extends AbstractHadoopActionNodeTest {

  @Test
  public void testBuildXml() {
    assertXml(
      new MapReduceNode(baseArgs),
      actionXml("map-reduce", "", ""))
  }

}
