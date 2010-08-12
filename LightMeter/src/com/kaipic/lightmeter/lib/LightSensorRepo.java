package com.kaipic.lightmeter.lib;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Double.valueOf;

public class LightSensorRepo {
  private LightSensorFactory lightSensorFactory;
  private Map<LightSensorType, LightSensor> sensors = new HashMap<LightSensorType, LightSensor>();

  public LightSensorRepo(LightSensorFactory lightSensorFactory) {
    this.lightSensorFactory = lightSensorFactory;
  }

  public LightSensor getSensor(String sensorInfo) {
    LightSensorType type = LightSensorType.AUTO.toString().equals(sensorInfo) ? LightSensorType.AUTO : LightSensorType.MANUAL;
    LightSensor sensor = sensors.get(type);
    if (sensor == null) {
      sensor = lightSensorFactory.createSensor(type);
      sensor.start();
      sensors.put(type, sensor);
    }
    if (type == LightSensorType.MANUAL) {
      ((ManualLightSensor) sensor).setEVByEVAt100(new ExposureValue(valueOf(sensorInfo).floatValue()));
    }
    return sensor;
  }

  public void start() {
    for (LightSensor sensor : sensors.values()) {
      sensor.start();
    }
  }

  public void stop() {
    for (LightSensor sensor : sensors.values()) {
      sensor.stop();
    }
  }
}
