package com.kaipic.lightmeter.lib;

import org.junit.Test;

import static com.kaipic.lightmeter.lib.ExposureValueTest.assertEVEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ManualModeTest {

  @Test
  public void shouldCalculateEVWhenAsked() throws Exception {
    LightMeter lightMeter = mock(LightMeter.class);
    Aperture aperture = new Aperture(0.1f);
    when(lightMeter.getAperture()).thenReturn(aperture);
    ShutterSpeed shutterSpeed = new ShutterSpeed(100f);
    when(lightMeter.getShutterSpeed()).thenReturn(shutterSpeed);
    ManualMode manualMode = new ManualMode(lightMeter);
    assertEVEquals(new ExposureValue(aperture, shutterSpeed), manualMode.getExposureAtISO100());
  }
}
