package com.kaipic.lightmeter.lib;

import org.junit.Test;
import org.junit.internal.ExactComparisonCriteria;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ExposureValueTest {
  @Test
  public void shouldGenerateFormattedStringAsToString() throws Exception {
    assertEquals("EV1.1", new ExposureValue(1.10001f).toString());
  }


  @Test
  public void detailStringShouldHandleEVWithoutLightScenario() throws Exception {
    assertEquals("EV39 Unknown EV", new ExposureValue(39).toDetailString());
  }

  @Test
  public void shouldUseLightScenarioToGenerateDetailString() {
    ExposureValue ev = new ExposureValue(10);
    new LightScenario("testScene", null, ev);
    assertEquals("EV10 testScene", ev.toDetailString());
  }


  @Test
  public void shouldGenerateReasonableStringIfValueIsTooLow() throws Exception {
    assertEquals("Exposure Value Not Available", new ExposureValue(-5f).toString());
  }

  @Test
  public void shouldBeAbleToMultiplyToFloatLargerThan1() throws Exception {
    ExposureValue ev = new ExposureValue(2f);
    assertEVEquals(new ExposureValue(3f), ev.multiply(2f));
  }

  @Test
  public void shouldGetEV100Value() throws Exception {
    ExposureValue ev = new ExposureValue(8f);
    assertEVEquals(new ExposureValue(7f), ev.getISO100EV(new Iso(200)));
  }

  @Test
  public void shouldCreateEVfromISOCalibrationAndIllumination() throws Exception {
    assertEVEquals(new ExposureValue(6f), new ExposureValue(160f, new Iso(100), 250));
  }

  @Test
  public void shouldCreateFromISOShutterSpeedAndAperture() throws Exception {
    ShutterSpeed shutterSpeed = new ShutterSpeed(1f / 64f);
    Aperture aperture = new Aperture(4f);
    assertEVEquals(new ExposureValue(10), new ExposureValue(aperture, shutterSpeed));
  }

  @Test
  public void shouldConvertToIllumination() throws Exception {
    assertEquals(160f, new ExposureValue(6f).toIllumination(new Iso(100), 250), 0.0001f);
  }

  public static void assertEVEquals(ExposureValue expected, ExposureValue actual) throws Exception {
    assertEquals(expected.getValue(), actual.getValue(), 0.001f);
  }
}
