package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;

import org.junit.Test;

class EmailNodeTest {

  @Test
  public void testToMap() {
    assertEquals(
      [type: 'email', name: 'send', to: 'Ian', subject: 'sub', body: 'message'],
      new EmailNode(name: 'send', to: 'Ian', subject: 'sub', body: 'message').toMap());
  }

  @Test
  public void testCcToMap() {
    assertEquals(
        [type: 'email', name: 'send', to: 'Ian', subject: 'sub', body: 'message', cc: 'other'],
        new EmailNode(name: 'send', to: 'Ian', subject: 'sub', body: 'message', cc: 'other').toMap());
  }

}
