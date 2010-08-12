package com.kaipic.lightmeter.lib;

import java.text.DecimalFormat;

import static com.kaipic.lightmeter.lib.Util.log2;

public class ExposureValue {
  private float value;

  public static final String[] DETAIL_STRINGS = new String[]{
    "Distant view of lighted skyline.",
    "Lightning (with time exposure).",
    "Fireworks (with time exposure).",
    "Subjects under bright street lamps.",
    "Night home interiors, average light.",
    "Brightly lit home interiors at night.",
    "Bottom of rainforest canopy.",
    "Las Vegas or Times Square at night.",
    "Landscapes 10 minutes after sunset.",
    "Landscapes immediately after sunset.",
    "Sunsets. Subjects in open shade.",
    "Subject in heavy overcast.",
    "Subjects in cloudy-bright light.",
    "Subjects in weak, hazy sun.",
    "Subjects in bright or hazy sun.",
    "Subjects in bright daylight on sand."
  };

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
    if (tooLowLight())
      return "N/A";
    DecimalFormat df = new DecimalFormat("#.#");
    return "EV" + df.format(value);
  }

  private boolean tooLowLight() {
    return value < -4.9;
  }


  public String toDetailString() {

    if(tooLowLight())
      return toString();
    String detailString = "Uncommon in nature";
    if( value > 0 && (int)value < DETAIL_STRINGS.length ) {
      detailString =  DETAIL_STRINGS[(int) value];
    }
    return toString() + " " + detailString;

  }

  public ExposureValue getISO100EV(int currentISO) {
    return multiply(100f / currentISO);
  }

  public float toIllumination(int iso, float calibration) {
    return (float) (Math.pow(2, value) * calibration / iso);
  }
}
