package com.kaipic.lightmeter.lib;

import android.content.Context;

public class TestLightSensorFactory extends LightSensorFactory {
  public TestLightSensorFactory(Context applicationContext) {
    super(applicationContext);
  }

  protected LightSensor createAutoLightSensor() {
    AmbientLightSensor sensor = (AmbientLightSensor) super.createAutoLightSensor();
    if (sensor.hasAmbientLightSensorOnDevice()) {
      return sensor;
    } else
      return new MockLightSensor();
  }
}
