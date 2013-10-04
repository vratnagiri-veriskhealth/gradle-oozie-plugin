package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;

import org.junit.Test;
import static BuilderTestUtils.assertXml

class FsNodeTest {

  @Test
  public void testBuildXml() {
    assertXml(
      new FsNode(
        name: 'fileStuff',
        delete: ['a', 'b'],
        mkdir: ['c', 'd'],
        move: [new FsMoveNode(source: 'from', target: 'to')],
        chmod: [new FsChmodNode(path: 'change', permissions: 'rwx', recursive: true),
                new FsChmodNode(path: 'simple', permissions: '666', dirFiles: true)],
        ok: 'next',
        error: 'fail'),
      """
  <action name='fileStuff'>
    <fs>
      <delete path='a' />
      <delete path='b' />
      <mkdir path='c' />
      <mkdir path='d' />
      <move source='from' target='to' />
      <chmod path='change' permissions='rwx' dir-files='false'><recursive /></chmod>
      <chmod path='simple' permissions='666' dir-files='true' />
    </fs>
    <ok to='next' />
    <error to='fail' />
  </action>
"""
      )
  }
}
