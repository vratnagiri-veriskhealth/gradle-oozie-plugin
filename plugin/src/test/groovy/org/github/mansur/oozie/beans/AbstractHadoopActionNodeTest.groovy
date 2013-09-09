package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;

import org.junit.Test;

class AbstractHadoopActionNodeTest {
  private Map<String, Object> common =
  [
    name: 'myAction',
    cred: 'trust me',
    ok: 'next',
    error: 'ack!!',
    jobTracker: 'the job tracker',
    delete: ['a', 'b'],
    mkdir: ['make me'],
    file: ['a file'],
    archive: ['an archive'],
    configuration: [c: 'd']
  ]

  protected Map<String, Object> baseArgs = common + [nameNode: 'a name node', jobXml: 'job.xml'];
  protected Map<String, Object> baseResult = common + [namenode: 'a name node', jobXML: 'job.xml'];

  protected String actionXml(String actionName, String uri, String customXml) {
    """
  <action name='myAction' cred='trust me'>
    <${actionName} xmlns='${uri}'>
      <job-tracker>the job tracker</job-tracker>
      <name-node>a name node</name-node>
      <prepare>
        <delete path="a"/>
        <delete path="b"/>
        <mkdir path="make me"/>
      </prepare>
      <job-xml>job.xml</job-xml>
      <configuration>
        <property>
          <name>c</name>
          <value>d</value>
        </property>
      </configuration>
      ${customXml}
      <file>
        a file
      </file>
      <archive>
        an archive
      </archive>
    </${actionName}>
    <ok to='next' />
    <error to='ack!!' />
  </action>
"""

  }
}
