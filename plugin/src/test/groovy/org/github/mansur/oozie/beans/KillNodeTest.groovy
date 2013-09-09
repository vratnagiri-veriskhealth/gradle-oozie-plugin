package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;
import static BuilderTestUtils.assertXml

import org.junit.Test;

class KillNodeTest {

  @Test
  public void testToMap() {
    assertEquals([type: 'kill', name: 'fail', message: 'hello'], new KillNode(name: 'fail', message: 'hello').toMap())
  }

  @Test
  public void testBuildXml() {
    assertXml(new KillNode(name: 'fail', message: 'hello'), "<kill name='fail'> <message>hello</message> </kill>")
  }
}
