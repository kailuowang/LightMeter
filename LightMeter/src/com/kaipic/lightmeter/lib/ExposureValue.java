package com.kaipic.lightmeter.lib;

import static com.kaipic.lightmeter.lib.Util.log2;

public class ExposureValue {
  private float value;


  public ExposureValue(float value) {
    this.value = value;
  }

  public float getValue() {
    return value;
  }

  public ExposureValue multiply(float multiplier) {
    return new ExposureValue(value + log2(multiplier));
  }

  public String toString() {
    return String.format("EV%.1f", value);
  }

  public ExposureValue getISO100EV(int currentISO) {
    return multiply(100f / currentISO);
  }
}
