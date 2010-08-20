package com.kaipic.lightmeter.lib;


import java.util.HashSet;
import java.util.Set;

public abstract class LightSensor {
  private static final int DEFAULT_CALIBRATION = 100;
  private boolean paused = false;
  private float lastRead = 0;
  private Iso iso = new Iso(100);
  Set<LightSensorListener> listeners = new HashSet<LightSensorListener>();
  private float calibration = DEFAULT_CALIBRATION;

  public LightSensor setISO(Iso iso) {
    this.iso = iso;
    return this;
  }

  public LightSensor setCalibration(float calibration) {
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


  public Iso getISO() {
    return iso;
  }

  public float getCalibration() {
    return calibration;
  }

  public void resetCalibration() {
    setCalibration(DEFAULT_CALIBRATION);
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

  public void calibrate(ExposureValue affirmedISO100EV) {
    setCalibration((float) (100f * read() / (Math.pow(2f, affirmedISO100EV.getValue()))));
  }
}
