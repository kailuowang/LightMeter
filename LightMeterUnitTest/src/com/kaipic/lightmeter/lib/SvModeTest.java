package com.kaipic.lightmeter.lib;

import org.junit.Test;

import static com.kaipic.lightmeter.lib.ApertureTest.assertApertureEquals;
import static com.kaipic.lightmeter.lib.ShutterSpeedTest.assertShutterSpeedEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SvModeTest {

  @Test
  public void shouldCalculateApertureWhenAsked() throws Exception {
    LightMeter lightMeter = mock(LightMeter.class);
    Aperture aperture = new Aperture(4f);
    when(lightMeter.calculateAperture()).thenReturn(aperture);
    WorkMode mode = new SvMode(lightMeter);
    assertApertureEquals(aperture, mode.getAperture());
    verify(lightMeter).calculateAperture();
  }
}
