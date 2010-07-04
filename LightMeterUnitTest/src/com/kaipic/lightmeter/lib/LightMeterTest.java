package com.kaipic.lightmeter.lib;

import org.junit.Test;

import static com.kaipic.lightmeter.lib.ShutterSpeedTest.assertShutterSpeedEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LightMeterTest {

  @Test
  public void shouldCalculateShutterSpeed() throws Exception {

    LightSensor lightSensor = mock(LightSensor.class);
    when(lightSensor.getEV()).thenReturn(new ExposureValue(10f));
    ShutterSpeed result = new LightMeter(lightSensor).setAperture(4f)
        .calculateShutterSpeed();
    assertShutterSpeedEquals(new ShutterSpeed(1f / 64f), result);
  }

  @Test
  public void shouldCreateWithInitialSettings() throws Exception {
    LightMeter lightMeter = new LightMeter(mock(LightSensor.class));
    assertFalse(lightMeter.getAperture() == null);
  }

}
