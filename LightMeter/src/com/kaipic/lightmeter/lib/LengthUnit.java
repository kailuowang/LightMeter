package com.kaipic.lightmeter.lib;

public enum LengthUnit {

  mm(1f),
  m(1000f),
  ft(304.8f);

  private float ratio_to_mm;

  private LengthUnit(float ratio_to_mm) {
    this.ratio_to_mm = ratio_to_mm;
  }


  public Length toLength(float value) {
    return new Length(value * ratio_to_mm);
  }

  public String toString(Length length) {
    return length.getValue() / ratio_to_mm + toString();
  }


}
