package com.kaipic.lightmeter.lib;

public class AvMode extends WorkMode {

  public AvMode(LightMeter lightMeter) {
    super(lightMeter);
  }

  public boolean isShutterSpeedChangeable() {
    return false;
  }

  public ShutterSpeed getShutterSpeed() {
    return lightMeter.calculateShutterSpeed();
  }

}
