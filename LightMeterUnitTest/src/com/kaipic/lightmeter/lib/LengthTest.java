package com.kaipic.lightmeter.lib;

import static org.junit.Assert.assertEquals;

public class LengthTest {
  public static void assertLengthEquals(Length lengthA, Length lengthB, float tolerance ) {
    assertEquals(lengthA.getValue(), lengthB.getValue(), tolerance);
  }
}
