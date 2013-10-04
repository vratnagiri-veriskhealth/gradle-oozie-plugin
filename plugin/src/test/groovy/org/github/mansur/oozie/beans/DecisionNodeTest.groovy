package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;
import static BuilderTestUtils.assertXml

import org.junit.Test;

class DecisionNodeTest {
  private final static DecisionNode node = new DecisionNode(
        name: 'choose',
        cases: [ new DecisionCaseNode(to: 'one', condition: 'odd'),
                 new DecisionCaseNode(to: 'zero', condition: 'even') ],
        defaultCase: 'huh?')

  @Test
  public void testBuildXml() {
    assertXml(node, """
  <decision name='choose'>
    <switch>
      <case to='one'>odd</case>
      <case to='zero'>even</case>
      <default to="huh?"/>
    </switch>
  </decision>
""")

  }
}
