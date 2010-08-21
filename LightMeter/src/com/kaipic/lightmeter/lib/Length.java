package com.kaipic.lightmeter.lib;

public class Length {
  private final LengthUnit defaultUnit = LengthUnit.mm;
  private final float value;

  public Length(float valueInmm) {
    this.value = valueInmm;
  }

  public static Length from(String stringVal, LengthUnit unit) {
    return unit.toLength(Float.parseFloat(stringVal));
  }

  float getValue() {
    return value;
  }

  public String toString() {
    return toString(defaultUnit);
  }

  public String toString(LengthUnit unit) {
    if(value < 0)
      return "Infinity";
    return unit.toString(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Length length = (Length) o;

    if (Float.compare(length.value, value) != 0) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return (value != +0.0f ? Float.floatToIntBits(value) : 0);
  }
}
