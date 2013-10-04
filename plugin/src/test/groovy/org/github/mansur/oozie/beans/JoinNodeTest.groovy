package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;
import static BuilderTestUtils.assertXml
import org.junit.Test;

class JoinNodeTest {

  @Test
  public void testBuildXml() {
    assertXml(new JoinNode(name: 'myJoin', to: 'andThen'), "<join name='myJoin' to='andThen'/>")
  }

}
