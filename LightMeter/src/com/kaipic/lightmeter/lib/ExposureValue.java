package com.kaipic.lightmeter.lib;

import java.util.Collection;
import java.util.List;

import static com.kaipic.lightmeter.lib.Util.log2;

public class ExposureValue implements Comparable<ExposureValue>{
  private float value;

  public ExposureValue(float value) {
    this.value = value;
  }

  public ExposureValue(String value) {
    this(Float.valueOf(value.replace("EV ", "")));
  }

  public ExposureValue(float illumination, Iso iso, float calibration) {
    this(log2(illumination * iso.getValue() / calibration));
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
    return "EV " + Util.format(value);
  }

  public String toDetailString() {

    String detailString = "Unknown EV";
    List<LightScenario> scenarios = LightScenario.find(new ExposureValue(Math.round(value)));
    if(scenarios.size() > 0){
      detailString = scenarios.get(0).getDescription();
    }
    return toString() + " " + detailString;
  }

  public ExposureValue getISO100EV(Iso currentISO) {
    return multiply(100f / currentISO.getValue());
  }

  public float toIllumination(Iso iso, float calibration) {
    return (float) (Math.pow(2, value) * calibration / iso.getValue());
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ExposureValue that = (ExposureValue) o;

    if (Float.compare(that.value, value) != 0) return false;

    return true;
  }

  public int hashCode() {
    return (value != +0.0f ? Float.floatToIntBits(value) : 0);
  }

  @Override
  public int compareTo(ExposureValue that) {
    return new Float(this.getValue()).compareTo(new Float(that.getValue()));
  }
}
