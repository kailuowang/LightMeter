package com.kaipic.lightmeter.lib;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Double.*;

public class LightSensorRepo {
  private Context context;
  private Map<LightSensorType, LightSensor> sensors = new HashMap<LightSensorType, LightSensor>();

  public LightSensorRepo(Context context) {
    this.context = context;
  }

  public LightSensor createSensor(LightSensorType type) {
    switch (type) {
      case AUTO:
        return new AmbientLightSensor(context);

      case MANUAL:
        return new ManualLightSensor();
    }
    throw new UnsupportedOperationException(type + " creation not implemented in LightSensorRepo");
  }

  public LightSensor getSensor(String sensorInfo) {
    LightSensorType type = sensorInfo == LightSensorType.AUTO.toString() ? LightSensorType.AUTO : LightSensorType.MANUAL;
    LightSensor sensor = sensors.get(type);
    if (sensor == null) {
      sensor = createSensor(type);
      sensors.put(type, sensor);
    }
    if (type == LightSensorType.MANUAL) {
      ((ManualLightSensor) sensor).setEVByEVAt100(new ExposureValue(valueOf(sensorInfo).floatValue()));
    }
    return sensor;
  }
}
