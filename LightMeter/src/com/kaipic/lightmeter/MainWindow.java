package com.kaipic.lightmeter;

import android.app.Activity;
import android.app.KeyguardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainWindow extends Activity {
	private LightSensor mSensor;
	private Button mReadButton;
	private TextView mSensorReadTextView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		disableKeyGuardForTesting();
		setContentView(R.layout.main);
		initializeFields();
		registerEvents();
	}

	public void setSensor(LightSensor sensor) {
		this.mSensor = sensor;
	}

	public LightSensor getSensor() {
		return mSensor;
	}

	private void initializeFields() {
		mReadButton = (Button) findViewById(R.id.read_button);
		mSensorReadTextView = (TextView) findViewById(R.id.sensor_read_text_view);
		mSensor = new AmbientLightSensor(getApplicationContext());
	}

	private void registerEvents() {
		mReadButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayRead();
			}

		});
	}

	private void displayRead() {
		Float read = (Float) mSensor.read();
		mSensorReadTextView.setText(read.toString());
	}

	// TODO: make this configurable
	private void disableKeyGuardForTesting() {
		KeyguardManager keyGuardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		keyGuardManager.newKeyguardLock("com.kaipic.lightmeter.MainWindow")
				.disableKeyguard();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensor.start();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mSensor.stop();
	}

}