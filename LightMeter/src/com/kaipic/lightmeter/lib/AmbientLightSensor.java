package com.kaipic.lightmeter.lib;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.List;

public class AmbientLightSensor extends LightSensor implements SensorEventListener {
  SensorManager mSensorManager;
  private float mRead;
  String mStatus = "Initialized";

  public AmbientLightSensor(Context context) {
    mSensorManager = (SensorManager) context.getSystemService(
        Context.SENSOR_SERVICE);
  }

  AmbientLightSensor() {
  }

  public void onAccuracyChanged(Sensor arg0, int arg1) {
  }

  public void onSensorChanged(SensorEvent arg0) {
    mRead = arg0.values[0];
    broadcast();
  }

  @Override
  public void start() {
    stop();
    Sensor sensor = getAndroidSensor();
    if (sensor != null) {
      mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
      mStatus = "Using " + sensor.getName();
    } else {
      mStatus = "No ambient light sensor found on the phone.";
    }
  }

  boolean hasAmbientLightSensorOnDevice() {
    return getAndroidSensor() != null;
  }

  private Sensor getAndroidSensor() {
    List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_LIGHT);
    Sensor sensor = sensors.size() > 0 ? sensors.get(0) : null;
    return sensor;
  }

  @Override
  public void stop() {
    mSensorManager.unregisterListener(this);
    mStatus = "Stopped";
  }

  @Override
  public String getStatus() {
    return mStatus;
  }

  @Override
  public LightSensorType getType() {
    return LightSensorType.AUTO;
  }

  @Override
  public float read() {
    return mRead;
  }


}
