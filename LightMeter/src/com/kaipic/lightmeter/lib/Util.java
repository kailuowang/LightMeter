package com.kaipic.lightmeter.lib;

import java.text.DecimalFormat;

public class Util {
  public static float log2(float x) {
    return (float) (Math.log(x) / Math.log(2));
  }

  public static String format(float value) {
    DecimalFormat df = new DecimalFormat("#.#");
    return df.format(value);
  }
}
