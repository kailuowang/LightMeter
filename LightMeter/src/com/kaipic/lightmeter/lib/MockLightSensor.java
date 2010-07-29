package com.kaipic.lightmeter.lib;

import java.util.Date;
import java.util.Random;

public class MockLightSensor extends LightSensor {
  Date lastReadAt = new Date();
  Random r = new Random();
  private float mRead;

  public float read() {
    long sinceLastRead = (new Date().getTime() - lastReadAt.getTime()) / 1000;
    if (sinceLastRead > 1) {
      mRead = (float) Math.pow(2f, (float) r.nextInt(14));
      lastReadAt = new Date();
    }
    return mRead;
  }

  public MockLightSensor setRead(float read) {
    mRead = read;
    lastReadAt = new Date();
    return this;
  }

  public LightSensorType getType() {
    return LightSensorType.AUTO;
  }
}
