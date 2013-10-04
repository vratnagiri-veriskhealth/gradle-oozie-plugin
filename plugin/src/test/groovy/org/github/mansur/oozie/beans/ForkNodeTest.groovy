package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;

import org.junit.Test;
import static BuilderTestUtils.assertXml

class ForkNodeTest {

  @Test
  public void testBuildXml() {
    assertXml(
      new ForkNode([name: 'myFork', paths: ['p1', 'p2']]),
      """
  <fork name='myFork'>
    <path start='p1' />
    <path start='p2' />
  </fork>
""")
  }
}
