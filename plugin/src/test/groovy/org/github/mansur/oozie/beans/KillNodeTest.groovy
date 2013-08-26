package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;

import org.junit.Test;

class KillNodeTest {

  @Test
  public void testToMap() {
    assertEquals([type: 'kill', name: 'fail', message: 'hello'], new KillNode(name: 'fail', message: 'hello').toMap())
  }

}
