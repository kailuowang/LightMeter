package com.kaipic.lightmeter.lib;

import android.content.Context;

public class LightSensorFactory {
  private Context context;

  public LightSensorFactory(Context context) {
    this.context = context;
  }

  public LightSensor createSensor(LightSensorType type) {
    LightSensor sensor;
    switch (type) {
      case AUTO:
        sensor = new AmbientLightSensor(context);
        break;
      case MANUAL:
        sensor = new ManualLightSensor();
        break;
      default:
        throw new UnsupportedOperationException(type + " creation not implemented in LightSensorRepo");
    }
    return sensor;
  }

}
