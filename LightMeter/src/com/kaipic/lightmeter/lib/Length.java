package com.kaipic.lightmeter.lib;

public class Length {
  final float value;

  public Length(float valueInmm) {
    this.value = valueInmm;
  }

  float getValue() {
    return value;
  }

  public String toString() {
    return Util.format(value) + "mm";
  }
}
