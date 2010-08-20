package com.kaipic.lightmeter.lib;

public class ManualLightSensor extends LightSensor {
  private float read;

  public float read() {
    return read;
  }

  public void setEVByEVAt100(ExposureValue exposureValue) {
    read = exposureValue.multiply(getISO().getValue() / 100f).toIllumination(getISO(), getCalibration());
  }

  public String getStatus() {
    return "Using Manual Settings";
  }

  public LightSensorType getType() {
    return LightSensorType.MANUAL;
  }

}
