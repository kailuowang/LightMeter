package com.kaipic.lightmeter.lib;

import java.util.HashSet;
import java.util.Set;

public class LightMeter implements LightSensorListener {
  private LightSensor lightSensor;
  private LightSensorRepo lightSensorRepo;
  private Aperture aperture = new Aperture(8.0f);
  private Set<LightMeterListener> subscribers = new HashSet<LightMeterListener>();
  private ShutterSpeed shutterSpeed;

  public LightMeter(LightSensorRepo lightSensorRepo) {
    this.lightSensorRepo = lightSensorRepo;
  }

  LightMeter(LightSensor lightSensor) {
    setLightSensor(lightSensor);
  }

  public void setLightSensor(String lightSensorInfo) {
    setLightSensor(lightSensorRepo.getSensor(lightSensorInfo));
  }

  public LightSensor getLightSensor() {
    return lightSensor;
  }

  public void setLightSensor(LightSensor lightSensor) {
    if (this.lightSensor != null)
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


  public void subscribe(LightMeterListener listener) {
    subscribers.add(listener);
  }

  public void unsubscribe(LightMeterListener listener) {
    subscribers.remove(listener);
  }

  public void togglePause() {
    lightSensor.togglePause();
  }

  public void start() {
    lightSensorRepo.start();
  }

  public int getISO() {
    return lightSensor.getISO();
  }

  public void stop() {
    lightSensorRepo.stop();
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

  public void calibrate() {
    if (!usingAutoLightSensor()) {
      getAutoLightSensor().calibrate(getISO100EV());
    }
  }

  public void resetCalibration() {
    getAutoLightSensor().resetCalibration();
  }

  private LightSensor getAutoLightSensor() {
    if (lightSensor.getType() == LightSensorType.AUTO)
      return lightSensor;
    return lightSensorRepo.getSensor(LightSensorType.AUTO.toString());
  }

  public float getCalibration() {
    return getAutoLightSensor().getCalibration();
  }

  public void setCalibration(float calibration) {
    getAutoLightSensor().setCalibration(calibration);
  }

  public boolean usingAutoLightSensor() {
    return lightSensor.getType().equals(LightSensorType.AUTO);
  }

  public void setShutterSpeed(ShutterSpeed shutterSpeed) {
    this.shutterSpeed = shutterSpeed;
  }

  ShutterSpeed getShutterSpeed() {
    return shutterSpeed;
  }

  ExposureValue getISO100EV() {
    return lightSensor.getISO100EV();
  }

  ShutterSpeed calculateShutterSpeed() {
    return new ShutterSpeed(aperture, lightSensor.getEV());
  }

}
