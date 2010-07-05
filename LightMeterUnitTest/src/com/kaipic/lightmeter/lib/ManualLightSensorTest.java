package com.kaipic.lightmeter.lib;

import org.junit.Test;

import static com.kaipic.lightmeter.lib.ExposureValueTest.assertEVEquals;

public class ManualLightSensorTest {

  @Test
  public void shouldBeAbleToSetEVbyEVAt100() throws Exception {
    ManualLightSensor sensor = new ManualLightSensor();
    sensor.setISO(200);
    sensor.setEVByEVAt100(new ExposureValue(10f));
    assertEVEquals(new ExposureValue(11f), sensor.getEV());
  }
}
