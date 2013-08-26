package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;

import org.junit.Test;

class AbstractHadoopActionNodeTest {
  private Map<String, Object> common =
  [
    name: 'my action',
    cred: 'trust me',
    ok: 'next',
    error: 'ack!!',
    jobTracker: 'the job tracker',
    delete: ['a', 'b'],
    mkdir: ['make me'],
    file: 'a file',
    archive: 'an archive',
    configuration: [c: 'd']
  ]

  protected Map<String, Object> baseArgs = common + [nameNode: 'a name node'];
  protected Map<String, Object> baseResult = common + [namenode: 'a name node'];
}
