package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;

import org.junit.Test;

import static BuilderTestUtils.assertXml
import static SlaEvents.*;

class SlaNodeTest {

  @Test
  public void testBasicBuildXml() {
    assertXml(
      new SlaNode(shouldEnd: '${30 * MINUTES}'),
      '''
<info xmlns="uri:oozie:sla:0.2">
  <nominal-time>${nominal_time}</nominal-time>
  <should-end>${30 * MINUTES}</should-end>
</info>
''')
  }

  @Test
  public void testFullBuildXml() {
    assertXml(
      new SlaNode(
        shouldStart: 'soon',
        shouldEnd: 'sometime',
        maxDuration: 'not too long',
        alertEvents: [DURATION_MISS, START_MISS],
        alertContact: "foo@bar.com",
        notificationMessage: "houston, we have a problem",
        upstreamApps: 'sewage treatment center'),
      '''
<info xmlns="uri:oozie:sla:0.2">
  <nominal-time>${nominal_time}</nominal-time>
  <should-start>soon</should-start>
  <should-end>sometime</should-end>
  <max-duration>not too long</max-duration>
  <alert-events>duration_miss,start_miss</alert-events>
  <alert-contact>foo@bar.com</alert-contact>
  <notification-msg>houston, we have a problem</notification-msg>
  <upstream-apps>sewage treatment center</upstream-apps>
</info>
''')
  }

}
