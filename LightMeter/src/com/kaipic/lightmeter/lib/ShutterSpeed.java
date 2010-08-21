package com.kaipic.lightmeter.lib;

public class ShutterSpeed {
  private float value;
  private static final float UNREASONABLY_LARGE_VALUE = 1999f;

  public ShutterSpeed(float value) {
    this.value = value;
  }

  public ShutterSpeed(Aperture aperture, int calibration, int iso, float illumination) {
    this(calibration * aperture.getValue() * aperture.getValue() / (illumination * iso));
  }

  public ShutterSpeed(Aperture aperture, ExposureValue exposureValue) {
    this((float) (aperture.getValue() * aperture.getValue() / Math.pow(2d, exposureValue.getValue())));
  }

  public ShutterSpeed(String stringValue) {
    this(parse(stringValue));
  }

  private static float parse(String stringValue) {
    float value = 0;
    if (stringValue.contains("1/")) {
      value = 1f / Float.valueOf(stringValue.replace("1/", ""));
    } else if (stringValue.contains("s")) {
      value = Float.valueOf(stringValue.replace("s", ""));
    }
    return value;
  }

  public String toString() {
    if (value < 1)
      return "1/" + (int) (1f / value);
    else if (value > UNREASONABLY_LARGE_VALUE)
      return "N/A";
    else
      return ((int) value) + "s";
  }

  float getValue() {
    return value;
  }
}
