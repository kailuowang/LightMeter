package com.kaipic.lightmeter.lib;

import org.junit.Test;

import static com.kaipic.lightmeter.lib.ShutterSpeedTest.assertShutterSpeedEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class AvModeTest {
  @Test
  public void shouldAllowApertureChange() throws Exception {
    WorkMode mode = new AvMode(null);
    assertTrue(mode.isApertureChangeable());
  }

  @Test
  public void shouldForbidShutterSpeedChange() throws Exception {
    WorkMode mode = new AvMode(null);
    assertTrue(!mode.isShutterSpeedChangeable());
  }

  @Test
  public void shouldCalculateShutterSpeedWhenAsked() throws Exception {
    LightMeter lightMeter = mock(LightMeter.class);
    ShutterSpeed shutterSpeed = new ShutterSpeed(0.1f);
    when(lightMeter.calculateShutterSpeed()).thenReturn(shutterSpeed);
    WorkMode mode = new AvMode(lightMeter);
    assertShutterSpeedEquals(shutterSpeed, mode.getShutterSpeed());
    verify(lightMeter).calculateShutterSpeed();
  }

  @Test
  public void shouldSetAperture() throws Exception {
    LightMeter lightMeter = mock(LightMeter.class);
    WorkMode mode = new AvMode(lightMeter);
    Aperture aperture = new Aperture(12f);
    mode.setAperture(aperture);
    verify(lightMeter).setAperture(aperture);

  }

}
