package com.kaipic.lightmeter.lib;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ApertureTest {

  @Test
  public void shouldCreateFromString() throws Exception {
    assertApertureEquals(new Aperture(4f), Aperture.fromString("4.0"));
  }

  @Test
  public void shouldCreateFromShutterSpeedAndExposure() throws Exception {
    assertApertureEquals(new Aperture(4f), new Aperture(new ShutterSpeed(1f / 64f), new ExposureValue(10f)));
  }

  @Test
  public void shouldReturn0_95AsString() throws Exception {
    assertEquals("0.95", new Aperture(0.95f).toString());
  }


  public static void assertApertureEquals(Aperture expected, Aperture actual) {
    assertEquals(expected.getValue(), actual.getValue(), 0.0001f);
  }
}
