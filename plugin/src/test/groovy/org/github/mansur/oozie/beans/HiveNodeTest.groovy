package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;
import static BuilderTestUtils.assertXml

import org.junit.Test;

class HiveNodeTest extends AbstractHadoopActionNodeTest {

  def hiveArgs = [ script: 'some.hql', params: [a:'b']]
  def args = baseArgs + hiveArgs;

    @Test
  public void testToMap() {
    assertEquals(baseResult + hiveArgs + [type: 'hive'], new HiveNode(args).toMap());
  }

  @Test
  public void testBuildXml() {
    assertXml(
      new HiveNode(args),
      actionXml("hive", "uri:oozie:hive-action:0.2", "<script>some.hql</script> <param>a=b</param>"))
  }
}
