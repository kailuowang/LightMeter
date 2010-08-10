package com.kaipic.lightmeter.lib;

import static com.kaipic.lightmeter.lib.Util.log2;

public class ExposureValue {
  private float value;


  public ExposureValue(float value) {
    this.value = value;
  }

  public ExposureValue(float illumination, int iso, float calibration) {
    this(log2(illumination * iso / calibration));
  }

  public ExposureValue(Aperture aperture, ShutterSpeed shutterSpeed) {
    this(log2(aperture.getValue() * aperture.getValue() / shutterSpeed.getValue()));
  }

  public float getValue() {
    return value;
  }

  public ExposureValue multiply(float multiplier) {
    return new ExposureValue(value + log2(multiplier));
  }

  public String toString() {
    if (value < -4.9)
      return "N/A";
    return String.format("EV%.1f", value);
  }

  public ExposureValue getISO100EV(int currentISO) {
    return multiply(100f / currentISO);
  }

  public float toIllumination(int iso, float calibration) {
    return (float) (Math.pow(2, value) * calibration / iso);
  }
}
