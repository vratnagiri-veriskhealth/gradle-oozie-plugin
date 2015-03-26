package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;
import static BuilderTestUtils.assertXml

import org.junit.Test;

class DecisionNodeTest {
	EndNode alt1=[name:"one"]
	EndNode alt2=[name:"zero"]
	EndNode nothing=[name:"nothing"]
  private final DecisionNode node = new DecisionNode(
        name: 'choose',
        decisions: [ 'odd':'one',
			         'even':'zero' ],
        defaultDecision: 'nothing')

  @Test
  public void testBuildXml() {
    assertXml(node, """
  <decision name='choose'>
    <switch>
      <case to='one'>odd</case>
      <case to='zero'>even</case>
      <default to="nothing"/>
    </switch>
  </decision>
""")

  }
}
