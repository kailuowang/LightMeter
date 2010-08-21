package com.kaipic.lightmeter.lib;

public class CameraSettingsRepository {
  public static final Length[] focalLengths = initFocalLengths();
  public static final Aperture[] apertures = initApertures();
  public static final ShutterSpeed[] shutterSpeeds = initShutterSpeed();
  public static final Iso[] isos = initISOs();
  public static Length defaultFocalLength = new Length(50);

  private static Iso[] initISOs() {
    int[] values = new int[]{50, 100, 160, 200, 400, 800, 1600, 3200};
    Iso[] list = new Iso[values.length];
    for (int i = 0; i < values.length; i++) {
      list[i] = new Iso(values[i]);
    }
    return list;
  }

  private static ShutterSpeed[] initShutterSpeed() {
    float[] values = new float[]{4, 2, 1, 1f / 2, 1f / 4, 1f / 8, 1f / 15, 1f / 30, 1f / 60, 1f / 125, 1f / 250, 1f / 500, 1f / 1000, 1f / 2000, 1f / 4000};
    ShutterSpeed[] list = new ShutterSpeed[values.length];
    for (int i = 0; i < values.length; i++) {
      list[i] = new ShutterSpeed(values[i]);
    }
    return list;
  }

  private static Aperture[] initApertures() {
    float[] values = new float[]{0.95f, 1.0f, 1.2f, 1.4f, 1.8f, 2.0f, 2.4f, 2.8f, 3.3f, 4.0f, 4.8f, 5.6f, 6.7f, 8.0f, 9.5f, 11, 13, 16, 19, 22};
    Aperture[] list = new Aperture[values.length];
    for (int i = 0; i < values.length; i++) {
      list[i] = new Aperture(values[i]);
    }
    return list;
  }

  private static Length[] initFocalLengths() {
    float[] values = new float[]{7, 10, 12, 14, 18, 20, 24, 28, 30, 32, 35, 38, 40, 42, 50, 55, 60, 70, 80, 85, 90, 100, 110, 120, 135, 150, 200, 210, 300, 360, 400};
    Length[] list = new Length[values.length];
    for (int i = 0; i < values.length; i++) {
      list[i] = new Length(values[i]);
    }
    return list;
  }
}
