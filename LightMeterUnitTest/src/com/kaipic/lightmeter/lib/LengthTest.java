package com.kaipic.lightmeter.lib;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LengthTest {
  public static void assertLengthEquals(Length lengthA, Length lengthB) {
    assertLengthEquals(lengthA, lengthB, 0.01f);
  }

  public static void assertLengthEquals(Length lengthA, Length lengthB, float tolerance) {
    assertEquals(lengthA.getValue(), lengthB.getValue(), tolerance);
  }

  @Test
  public void shouldToUnitMString() throws Exception {
    assertEquals("1.5m", new Length(1500).toString(LengthUnit.m));

  }

  @Test
  public void shouldCreateFromStringAndUnit() throws Exception {
    assertEquals(new Length(1500), Length.from("1.5", LengthUnit.m));
  }


}
