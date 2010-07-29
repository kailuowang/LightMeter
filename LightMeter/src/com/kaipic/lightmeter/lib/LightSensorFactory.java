package com.kaipic.lightmeter.lib;

import android.content.Context;

public class LightSensorFactory {
  private Context context;

  public LightSensorFactory(Context context) {
    this.context = context;
  }

  LightSensorFactory() {
  }

  public LightSensor createSensor(LightSensorType type) {
    LightSensor sensor;
    switch (type) {
      case AUTO:
        sensor = createAutoLightSensor();
        break;
      case MANUAL:
        sensor = new ManualLightSensor();
        break;
      default:
        throw new UnsupportedOperationException(type + " creation not implemented in LightSensorRepo");
    }
    return sensor;
  }

  protected LightSensor createAutoLightSensor() {
    return new AmbientLightSensor(context);
  }

}
