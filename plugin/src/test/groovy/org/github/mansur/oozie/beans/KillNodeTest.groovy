package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;
import static BuilderTestUtils.assertXml

import org.junit.Test;

class KillNodeTest {

  @Test
  public void testBuildXml() {
    assertXml(new KillNode(name: 'fail', message: 'hello'), "<kill name='fail'> <message>hello</message> </kill>")
  }
}
