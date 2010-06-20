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
	protected float mRead;
	String mStatus = "UNKNOWN";
	Set<LightSensorListener> listeners = new HashSet<LightSensorListener>();
	boolean mPaused = false;
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

	void broadcast() {
		if(isPaused()) return;
		for (LightSensorListener listener : listeners) {
			listener.onLightSensorChange();
		}
	}

	public void start() {
		List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_LIGHT);
		Integer size = sensors.size();
		if (size > 0) {
			Sensor sensor = sensors.get(0);
			mSensorManager.registerListener(this, sensor,
					SensorManager.SENSOR_DELAY_FASTEST);
			mStatus = "listening to " + sensor.getName() + "  " + sensor.getVendor(); 
		}
	}

	public void stop() {
		mSensorManager.unregisterListener(this);
	}

	public String getStatus(){
		return mStatus; 
	}
	public float read() {
		return mRead;
	}

	public void register(LightSensorListener listener) {
	   listeners.add(listener);
	}

	public void togglePause() {
		mPaused = !mPaused;
	}


	public boolean isPaused() {
		return mPaused;
	}
}
