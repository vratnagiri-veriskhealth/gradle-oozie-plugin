package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;

import org.junit.Test;

class SubWorkflowNodeTest {

  @Test
  public void testToMap() {
    assertEquals(
      [ok: 'yes', error: 'no', type: 'sub-workflow', appPath: '/some/path', configuration: [foo: 'bar'] ],
      new SubWorkflowNode(ok: 'yes', error: 'no', appPath: '/some/path', configuration: [foo: 'bar']).toMap());
  }
}
