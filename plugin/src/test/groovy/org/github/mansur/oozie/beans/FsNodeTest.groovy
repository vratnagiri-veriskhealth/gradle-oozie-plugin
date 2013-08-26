package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;

import org.junit.Test;

class FsNodeTest {

  @Test
  public void testToMap() {
    assertEquals(
      [ type: 'fs',
        name: 'fileStuff',
        delete: ['a', 'b'],
        mkdir: ['c', 'd'],
        move: [[source: 'from', target: 'to']],
        chmod: [[path: 'change', permissions: 'rwx', dir_files: 'false'],
                [path: 'simple', permissions: '666']]],
     new FsNode(
       name: 'fileStuff',
       delete: ['a', 'b'],
       mkdir: ['c', 'd'],
       move: [new FsMoveNode(source: 'from', target: 'to')],
       chmod: [new FsChmodNode(path: 'change', permissions: 'rwx', dirFiles: false),
               new FsChmodNode(path: 'simple', permissions: '666')]).toMap())
  }

}
