package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;
import static BuilderTestUtils.assertXml
import org.junit.Test;

class JoinNodeTest {

  @Test
  public void test() {
    assertEquals(
      [type: 'join', name: 'myJoin', to: 'andThen'],
      new JoinNode(name: 'myJoin', to: 'andThen').toMap()
    )
  }

  @Test
  public void testBuildXml() {
    assertXml(new JoinNode(name: 'myJoin', to: 'andThen'), "<join name='myJoin' to='andThen'/>")
  }

}
