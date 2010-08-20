package com.kaipic.lightmeter.lib;

public class DoFCalculator {
  private Length focalLength;
  private CirclesOfConfusion circleOfConfusion;
  private Aperture aperture;
  private Length subjectDistance;

  public DoFCalculator setFocalLength(Length focalLength) {
    this.focalLength = focalLength;
    return this;
  }

  public DoFCalculator setCircleOfConfusion(CirclesOfConfusion circleOfConfusion) {
    this.circleOfConfusion = circleOfConfusion;
    return this;
  }

  public DoFCalculator setAperture(Aperture aperture) {
    this.aperture = aperture;
    return this;
  }

  public DoFCalculator setSubjectDistance(Length subjectDistance) {
    this.subjectDistance = subjectDistance;
    return this;
  }

  public Length hyperFocalDistance() {
    float f = focalLength.getValue();
    float N = aperture.getValue();
    float c = circleOfConfusion.getValue().getValue();
    return new Length(f + f * f / (N * c));
  }

  public Length nearLimit() {
    float H = hyperFocalDistance().getValue();
    float f = focalLength.getValue();
    float s = subjectDistance.getValue();
    return new Length(s * (H - f) / (H + s - 2 * f));
  }

  public Length farLimit() {
    float H = hyperFocalDistance().getValue();
    float f = focalLength.getValue();
    float s = subjectDistance.getValue();
    return new Length(s * (H - f) / (H - s));
  }

  public Length getFocalLength() {
    return focalLength;
  }

  public CirclesOfConfusion getCirclesOfConfusion() {
    return circleOfConfusion;
  }
}
