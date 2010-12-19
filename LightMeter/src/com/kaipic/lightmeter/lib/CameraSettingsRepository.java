package com.kaipic.lightmeter.lib;

import java.util.*;

public class CameraSettingsRepository {
  public static final Length[] focalLengths = initFocalLengths();
  public static final Aperture[] apertures = initApertures();
  public static final ShutterSpeed[] shutterSpeeds = initShutterSpeed();
  public static final Iso[] isos = initISOs();
  public static LightScenarioCategory[] lightScenarioCategories = initLightScenarioCategories();
  public static final ExposureValue[] exposureValues = initExposureValues();
  public static Length defaultFocalLength = new Length(50);


  private static ExposureValue[] initExposureValues() {
    SortedSet<ExposureValue> evs = new TreeSet<ExposureValue>();
    for (LightScenario lightScenario : LightScenario.all()) {
      evs.addAll(lightScenario.getLightValues());
    }
    return evs.toArray(new ExposureValue[0]);

  }

  private static Iso[] initISOs() {
    int[] values = new int[]{50, 100, 160, 200, 400, 800, 1600, 3200};
    Iso[] list = new Iso[values.length];
    for (int i = 0; i < values.length; i++) {
      list[i] = new Iso(values[i]);
    }
    return list;
  }

  private static ShutterSpeed[] initShutterSpeed() {
    float[] values = new float[]{4, 2, 1, 1f / 2f, 1f / 4f, 1f / 8, 1f / 15, 1f / 30, 1f / 60, 1f / 125f, 1f / 250f, 1f / 500f, 1f / 1000f, 1f / 2000f, 1f / 4000f};
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

  private static LightScenarioCategory[] initLightScenarioCategories() {
    LightScenarioCategory outdoorNaturalLight = new LightScenarioCategory("Outdoor, Natural Light");
    outdoorNaturalLight.addScenario("Bright scene in full sunlight", 16, 17);
    outdoorNaturalLight.addScenario("Typical scene in full sunlight", 15);
    outdoorNaturalLight.addScenario("Typical scene in hazy sunlight", 14);
    outdoorNaturalLight.addScenario("Typical scene, cloudy bright", 13);
    outdoorNaturalLight.addScenario("Typical scene, heavy overcast", 12);
    outdoorNaturalLight.addScenario("Areas in open shade, clear sunlight", 12);
    outdoorNaturalLight.addScenario("Areas in deep shade", 11);
    outdoorNaturalLight.addScenario("Areas just after sunset", 10);
    outdoorNaturalLight.addScenario("Areas just before sunrise", 10);
    outdoorNaturalLight.addScenario("Areas 10 minutes after sunset", 9);
    outdoorNaturalLight.addScenario("Areas 10 minutes before sunrise", 9);
    outdoorNaturalLight.addScenario("Bottom of rainforest canopy", 7);
    outdoorNaturalLight.addScenario("Distant view of lighted skyline", 1);
    outdoorNaturalLight.addScenario("Bright scene under full moon",  -2);
    outdoorNaturalLight.addScenario("Rural Areas under full moon", -3);
    outdoorNaturalLight.addScenario("Rural Areas under half moon", -4);
    outdoorNaturalLight.addScenario("Rural Areas under quarter moon", -5);
    outdoorNaturalLight.addScenario("Rural Areas under star light", -7, -6);

    LightScenarioCategory outdoorArtificialLight = new LightScenarioCategory("Outdoor, Night Artifical Light");
    outdoorArtificialLight.addScenario("Neon and other bright signs",	9, 10);
    outdoorArtificialLight.addScenario("Night sports",	9);
    outdoorArtificialLight.addScenario("Fires and burning buildings",9);
    outdoorArtificialLight.addScenario("Stadium lighting",8);
    outdoorArtificialLight.addScenario("Bright street scenes",	8);
    outdoorArtificialLight.addScenario("Night street scenes, window displays",	7,8);
    outdoorArtificialLight.addScenario("Night vehicle traffic",	5);
    outdoorArtificialLight.addScenario("Fairs and amusement parks",	7);
    outdoorArtificialLight.addScenario("Christmas tree lights",	4,5);
    outdoorArtificialLight.addScenario("Floodlit buildings, monuments",	3,5);
    outdoorArtificialLight.addScenario("Distant views of lighted buildings",	2);

    LightScenarioCategory indoor = new LightScenarioCategory("Indoor, Artifical Light");
    indoor.addScenario("Galleries", 8, 9, 10);
    indoor.addScenario("Stage shows with bright lighting", 9);
    indoor.addScenario("Circuses, floodlit", 8);
    indoor.addScenario("Interior with window light", 8);
    indoor.addScenario("Sports events, stage shows", 8, 9);
    indoor.addScenario("Ice shows, floodlit", 9);
    indoor.addScenario("Offices and work areas", 7, 8);
    indoor.addScenario("Home interiors", 5, 6, 7);
    indoor.addScenario("Christmas tree lights", 4, 5);
    indoor.addScenario("Subjects lit by dim ambient light", -1, 0);

    return new LightScenarioCategory[]{outdoorNaturalLight, outdoorArtificialLight, indoor};


  }
}
