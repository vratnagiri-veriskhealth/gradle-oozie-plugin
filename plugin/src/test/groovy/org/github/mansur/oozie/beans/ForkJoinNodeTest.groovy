package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;

import groovy.xml.MarkupBuilder
import org.junit.Test;
import static BuilderTestUtils.assertXml

class ForkJoinNodeTest {

	EndNode one=[name:"one"]
	EndNode two=[name:"two"]
	EndNode end=[name:"end"]
	
  @Test
  public void testBuildXml() {
    assertXml(
      new ForkJoinNode([name: 'myFork', actions: [one, two], to:'end']),
      """
  <fork name='myFork_fork'>
    <path start='one' />
    <path start='two' />
  </fork>
""")
  }
  @Test
  public void testBuildJoinXml(){
	  def stringWriter = new StringWriter()
	  new ForkJoinNode([name: 'myFork', actions: [one, two], to:'end']).buildJoinXml(new MarkupBuilder(new PrintWriter(stringWriter)))
	  assertXml(
		  stringWriter.toString(),
		  "<join name='myFork_join' to='end'/>"
)
  }
}
