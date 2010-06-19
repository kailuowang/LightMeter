package com.kaipic.lightmeter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AmbientLightSensor implements LightSensor, SensorEventListener {
	SensorManager mSensorManager;
	private float mRead;
	private Set<LightSensorListener> listeners = new HashSet<LightSensorListener>();
	public AmbientLightSensor(Context context) {
		mSensorManager = (SensorManager) context.getSystemService(
				Context.SENSOR_SERVICE);
	}
	
	AmbientLightSensor() {
	}

	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

	public void onSensorChanged(SensorEvent arg0) {
		synchronized (this) {
			mRead = arg0.values[0];
			broadcast();
		}
	}

	void broadcast() {
		for (LightSensorListener listener : listeners) {
			listener.onLightSensorChange();
		}
	}

	public void start() {
		List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_LIGHT);
		Integer size = sensors.size();
		if (size > 0) {
			mSensorManager.registerListener(this, sensors.get(0),
					SensorManager.SENSOR_DELAY_FASTEST);
		}
	}

	public void stop() {
		mSensorManager.unregisterListener(this);
	}

	public float read() {
		return mRead;
	}

	@Override
	public void register(LightSensorListener listener) {
	   listeners.add(listener);
	}
}
