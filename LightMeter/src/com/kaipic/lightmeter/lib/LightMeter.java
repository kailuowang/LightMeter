package com.kaipic.lightmeter.lib;

import java.util.HashSet;
import java.util.Set;

public class LightMeter implements LightSensorListener {
  private LightSensor lightSensor;
  private Aperture aperture = new Aperture(8.0f);
  private Set<LightMeterListener> subscribers = new HashSet<LightMeterListener>();


  public LightMeter(LightSensor lightSensor) {
    setLightSensor(lightSensor);
  }

  public void setLightSensor(LightSensor lightSensor) {
    if(this.lightSensor != null)
      this.lightSensor.unsubscribe(this);
    this.lightSensor = lightSensor;
    this.lightSensor.subscribe(this);
  }

  public Aperture getAperture() {
    return aperture;
  }

  public LightMeter setAperture(Aperture aperture) {
    this.aperture = aperture;
    return this;
  }

  public LightMeter setAperture(float apertureValue) {
    return setAperture(new Aperture(apertureValue));
  }

  public LightMeter setISO(int iso) {
    lightSensor.setISO(iso);
    return this;
  }

  public LightMeter setCalibration(int calibration) {
    lightSensor.setCalibration(calibration);
    return this;
  }

  public ShutterSpeed calculateShutterSpeed() {
    return new ShutterSpeed(aperture, lightSensor.getEV());
  }

  public void subscribe(LightMeterListener listener) {
    subscribers.add(listener);
  }

  public void unsubscribe(LightMeterListener listener) {
    subscribers.remove(listener);
  }

  public void togglePause(){
    lightSensor.togglePause();
  }

  public void start() {
    lightSensor.start();
  }

  public ExposureValue getISO100EV() {
    return lightSensor.getISO100EV();
  }

  public void stop() {
    lightSensor.stop();
  }

  public boolean isPaused() {
    return lightSensor.isPaused();
  }

  public void onLightSensorChange() {
    for (LightMeterListener subscriber : subscribers) {
      subscriber.onLightMeterChange();
    }
  }

  public CharSequence getStatus() {
    return lightSensor.getStatus();
  }
}
