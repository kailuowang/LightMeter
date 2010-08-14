package com.kaipic.lightmeter.lib;

public class DoFCalculator {
  private Length focalLength;
  private Length circleOfConfusion;
  private Aperture aperture;

  public void setFocalLength(Length focalLength) {
    this.focalLength = focalLength;
  }

  public void setCircleOfConfusion(Length circleOfConfusion) {
    this.circleOfConfusion = circleOfConfusion;
  }

  public void setAperture(Aperture aperture) {
    this.aperture = aperture;
  }

  public Length calculateHyperFocalDistance() {
    float value = focalLength.getValue() + focalLength.getValue() * focalLength.getValue()
                                          /(aperture.getValue() * circleOfConfusion.getValue());
    return new Length(value);
  }
}
