package com.kaipic.lightmeter.lib;

public class CameraSettingsRepository {
  public static final Length[] focalLengths = initFocalLengths();

  private static Length[] initFocalLengths() {
    float[] values = new float[]{7, 10, 12, 14, 18, 20, 24, 28, 30, 32, 35, 38, 40, 42, 50, 55, 60, 70, 80, 85, 90, 100, 110, 120, 135, 150, 200, 210, 300, 360, 400};
    Length[] list = new Length[values.length];
    for (int i = 0; i < values.length; i++) {
      list[i] = new Length(values[i]);
    }
    return list;
  }
}
