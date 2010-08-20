package com.kaipic.lightmeter.lib;

public class Iso {
  private final int value;

  public Iso(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Iso iso = (Iso) o;

    if (value != iso.value) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return value;
  }
}
