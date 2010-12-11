package com.kaipic.lightmeter.lib;

import org.junit.Test;

import static com.kaipic.lightmeter.lib.ExposureValueTest.assertEVEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ManualModeTest {

  @Test
  public void shouldCalculateEVWhenAsked() throws Exception {
    LightMeter lightMeter = mock(LightMeter.class);
    Aperture aperture = new Aperture(4f);
    when(lightMeter.getAperture()).thenReturn(aperture);
    when(lightMeter.getISO()).thenReturn(new Iso(200));
    ShutterSpeed shutterSpeed = new ShutterSpeed(1f / 64f);
    when(lightMeter.getShutterSpeed()).thenReturn(shutterSpeed);
    ManualMode manualMode = new ManualMode(lightMeter);
    assertEVEquals(new ExposureValue(9), manualMode.getExposure());
  }
}
