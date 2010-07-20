package com.kaipic.lightmeter.lib;


import java.util.HashSet;
import java.util.Set;

public abstract class LightSensor {
  private boolean paused = false;
  private float lastRead = 0;
  private int iso = 100;
  Set<LightSensorListener> listeners = new HashSet<LightSensorListener>();
  private int calibration = 100;

  public LightSensor setISO(int iso) {
    this.iso = iso;
    return this;
  }

  public LightSensor setCalibration(int calibration) {
    this.calibration = calibration;
    return this;
  }

  public ExposureValue getEV() {
    if (!isPaused())
      lastRead = read();
    return new ExposureValue(lastRead, iso, calibration);
  }

  public void broadcast() {
    if (isPaused()) return;
    for (LightSensorListener listener : listeners) {
      listener.onLightSensorChange();
    }
  }

  public abstract float read();

  public void subscribe(LightSensorListener listener) {
    listeners.add(listener);
  }

  public void togglePause() {
    paused = !paused;
    if (!paused)
      start();
  }

  public boolean isPaused() {
    return paused;
  }

  public void start() {
  }

  public void stop() {
  }

  public String getStatus() {
    return isPaused() ? "Paused" : "Monitoring";
  }


  public int getISO() {
    return iso;
  }

  public int getCalibration() {
    return calibration;
  }


  public ExposureValue getISO100EV() {
    return getEV().getISO100EV(iso);
  }

  public void unsubscribe(LightSensorListener listener) {
    listeners.remove(listener);
  }

  public LightSensorType getType() {
    return LightSensorType.UNKNOWN;
  }
}
