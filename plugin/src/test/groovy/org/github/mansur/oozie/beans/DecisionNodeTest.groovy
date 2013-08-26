package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;

import org.junit.Test;

class DecisionNodeTest {

  @Test
  public void testToMap() {
    assertEquals(
      [type: 'decision', name: 'choose', switch: [[to: 'one', if: 'odd'], [to: 'zero', if: 'even']], default: 'huh?'],
      new DecisionNode(
        name: 'choose',
        cases: [ new DecisionCaseNode(to: 'one', condition: 'odd'),
                 new DecisionCaseNode(to: 'zero', condition: 'even') ],
        defaultCase: 'huh?').toMap()
    )
  }

}
