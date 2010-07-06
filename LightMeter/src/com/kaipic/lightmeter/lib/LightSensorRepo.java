package com.kaipic.lightmeter.lib;

import android.content.Context;

public class LightSensorRepo {
  private Context context;

  public LightSensorRepo(Context context) {
    this.context = context;
  }

  public LightSensor createSensor(LightSensorType type) {
    switch (type) {
      case AUTO :
        return new AmbientLightSensor(context);

      case MANUAL:
        return new ManualLightSensor();
    }
    throw new UnsupportedOperationException(type + " creation not implemented in LightSensorRepo");
  }
}
