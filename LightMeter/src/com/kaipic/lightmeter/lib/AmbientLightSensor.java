package com.kaipic.lightmeter.lib;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AmbientLightSensor extends LightSensor implements SensorEventListener {
	SensorManager mSensorManager;
	private float mRead;
	String mStatus = "UNKNOWN";

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
		List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_LIGHT);
		Integer size = sensors.size();
		if (size > 0) {
			Sensor sensor = sensors.get(0);
			mSensorManager.registerListener(this, sensor,
					SensorManager.SENSOR_DELAY_FASTEST);
			mStatus = "listening to " + sensor.getName() + "  " + sensor.getVendor(); 
		}
	}

	@Override
    public void stop() {
		mSensorManager.unregisterListener(this);
	}

	@Override
    public String getStatus(){
		return mStatus; 
	}
	@Override
    public float read() {
		return mRead;
	}


}
