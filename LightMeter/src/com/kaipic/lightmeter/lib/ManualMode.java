package com.kaipic.lightmeter.lib;

public class ManualMode extends WorkMode {
  public ManualMode(LightMeter lightMeter) {
    super(lightMeter);
  }

  public boolean isExposureValueChangeable() {
    return false;
  }

  public ExposureValue getExposureAtISO100() {
    return new ExposureValue(getAperture(), getShutterSpeed());
  }
}
