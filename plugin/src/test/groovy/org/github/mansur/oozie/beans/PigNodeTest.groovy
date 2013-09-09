package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;
import org.junit.Test;
import static BuilderTestUtils.assertXml

class PigNodeTest extends AbstractHadoopActionNodeTest {
  def pigArgs = [ script: 'some.pig', params: ['a', 'b'] ]
  def args = baseArgs + pigArgs;

  @Test
  public void testToMap() {
    assertEquals(baseResult + pigArgs + [type: 'pig'], new PigNode(args).toMap());
  }

  @Test
  public void testBuildXml() {
    assertXml(
      new PigNode(args),
      actionXml("pig", "", "<script>some.pig</script> <param>a</param><param>b</param>"))
  }

}
