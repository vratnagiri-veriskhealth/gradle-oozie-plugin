package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;

import org.junit.Test;

class JoinNodeTest {

  @Test
  public void test() {
    assertEquals(
      [type: 'join', name: 'my join', to: 'andThen'],
      new JoinNode(name: 'my join', to: 'andThen').toMap()
    )
  }

}
