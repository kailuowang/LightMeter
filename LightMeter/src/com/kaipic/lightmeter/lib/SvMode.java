package com.kaipic.lightmeter.lib;

public class SvMode extends WorkMode {
  public SvMode(LightMeter lightMeter) {
    super(lightMeter);
  }


  public boolean isApertureChangeable() {
    return false;
  }

  public void setAperture(Aperture aperture) {
    
  }

  public Aperture getAperture() {
    return lightMeter.calculateAperture();
  }
}
