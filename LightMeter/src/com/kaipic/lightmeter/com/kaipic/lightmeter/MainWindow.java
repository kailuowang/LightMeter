package com.kaipic.lightmeter.com.kaipic.lightmeter;

import java.lang.reflect.Method;
import java.util.List;

import com.kaipic.lightmeter.R;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainWindow extends Activity implements SensorEventListener {

	SensorManager manager = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		manager = (SensorManager) getApplicationContext().getSystemService(
				Context.SENSOR_SERVICE);
		

	}

	public TextView getMainTextView() {
		return (TextView) findViewById(R.id.maintextview);
	}

	public void onAccuracyChanged(Sensor arg0, int arg1) {

	}

	public void onSensorChanged(SensorEvent arg0) {
		synchronized (this) {
			Float value = arg0.values[0];
			getMainTextView().setText(value.toString());
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_LIGHT);
		Integer size = sensors.size();
		if (size > 0) {
			manager.registerListener(this, sensors.get(0),
					SensorManager.SENSOR_DELAY_FASTEST);
		}
	}

	@Override
	protected void onStop() {
		// unregister listener
		manager.unregisterListener(this);
		super.onStop();
	}

}