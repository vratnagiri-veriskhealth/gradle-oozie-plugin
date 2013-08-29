package org.github.mansur.oozie.beans;

import static org.junit.Assert.*;

import org.junit.Test;

class NameCheckerTest {

  @Test
  public void testVerify() {
    assertEquals("x", NameChecker.verify("x"))
    NameChecker.verify("aValidOozieName-12_3")
  }

  @Test(expected = IllegalArgumentException)
  void testIllegalChars() {
    NameChecker.verify("goodUntil*")
  }

  @Test(expected = IllegalArgumentException)
  void testBadFirstChar() {
    NameChecker.verify("9IsABadWayToStart")
  }
}
