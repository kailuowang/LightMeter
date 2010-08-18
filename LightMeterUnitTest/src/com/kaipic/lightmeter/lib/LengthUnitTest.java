package com.kaipic.lightmeter.lib;

import org.junit.Test;

import static com.kaipic.lightmeter.lib.LengthTest.assertLengthEquals;


public class LengthUnitTest {
  @Test
  public void shouldCreateLengthFrom_m() throws Exception {
    assertLengthEquals(new Length(1500f), LengthUnit.m.toLength(1.5f));
  }

  @Test
  public void shouldCreateLengthFrom_mm() throws Exception {
    assertLengthEquals(new Length(15f), LengthUnit.mm.toLength(15f));
  }

  @Test
  public void shouldCreateLengthFrom_ft() throws Exception {
    assertLengthEquals(new Length(304.8f), LengthUnit.ft.toLength(1f));
  }
}
