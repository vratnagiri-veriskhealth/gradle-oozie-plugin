package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;

import org.junit.Test;

class ForkNodeTest {

  @Test
  public void testToMap() {
    assertEquals(
      [name: 'myFork', type: 'fork', paths: ['p1', 'p2']],
      new ForkNode([name: 'myFork', paths: ['p1', 'p2']]).toMap())
  }

}
