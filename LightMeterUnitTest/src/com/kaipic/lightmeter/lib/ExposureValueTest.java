package com.kaipic.lightmeter.lib;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExposureValueTest {
  @Test
  public void shouldGenerateFormattedStringAsToString() throws Exception {
    assertEquals("EV1.1", new ExposureValue(1.10001f).toString());
  }

  @Test
  public void shouldBeAbleToMultiplyToFloatLargerThan1() throws Exception {
    ExposureValue ev = new ExposureValue(2f);
    assertEVEquals(new ExposureValue(3f), ev.multiply(2f));
  }

  @Test
  public void shouldGetEV100Value() throws Exception {
    ExposureValue ev = new ExposureValue(8f);
    assertEVEquals(new ExposureValue(7f), ev.getISO100EV(200));
  }

  @Test
  public void shouldCreateEVfromISOCalibrationAndIllumination() throws Exception {
    assertEVEquals(new ExposureValue(6f), new ExposureValue(160f, 100, 250));
  }

  @Test
  public void shouldConvertToIllumination() throws Exception {
    assertEquals(160f, new ExposureValue(6f).toIllumination(100, 250), 0.0001f);
  }

  public static void assertEVEquals(ExposureValue expected, ExposureValue actual) throws Exception {
    assertEquals(expected.getValue(), actual.getValue(), 0.001f);
  }
}
