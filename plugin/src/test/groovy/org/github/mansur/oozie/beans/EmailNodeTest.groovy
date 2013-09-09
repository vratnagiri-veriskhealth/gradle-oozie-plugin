package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;
import static BuilderTestUtils.assertXml

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

  @Test
  public void testBuildXml() {
    assertXml(
      new EmailNode(name: 'send', to: 'Ian', subject: 'sub', body: 'message', cc: 'other', ok: 'next', error: 'fail'),
      """
  <action name='send'>
    <email xmlns='uri:oozie:email-action:0.1'>
      <to>Ian</to>
      <cc>other</cc>
      <subject>sub</subject>
      <body>message</body>
    </email>
    <ok to='next' />
    <error to='fail' />
  </action>
"""
      )
  }
}
