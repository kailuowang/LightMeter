package com.kaipic.lightmeter;

import com.kaipic.lightmeter.lib.AmbientLightSensor;
import com.kaipic.lightmeter.lib.LightSensor;
import com.kaipic.lightmeter.lib.LightSensorListener;

import android.app.Activity;
import android.app.KeyguardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainWindow extends Activity implements LightSensorListener {
	private LightSensor mSensor;
	private Button mPauseButton;
	private TextView mSensorReadTextView;
    public static boolean disableKeyGruarding = false;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(disableKeyGruarding) disableKeyGuardForTesting();
		setContentView(R.layout.main);
		initializeFields();
		registerEvents();
	}

	public void setSensor(LightSensor sensor) {
		this.mSensor = sensor;
		mSensor.register(this);
	}

	public LightSensor getSensor() {
		return mSensor;
	}

	private void initializeFields() {
		mPauseButton = (Button) findViewById(R.id.pause_button);
		mSensorReadTextView = (TextView) findViewById(R.id.sensor_read_text_view);
		setSensor(new AmbientLightSensor(getApplicationContext()));
	}

	private void registerEvents() {
		mPauseButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			  toggleSensor();
			}
		});
	}

	private void toggleSensor() {
		mSensor.togglePause();
		int resId = mSensor.isPaused() ? R.string.continue_btn : R.string.pause;
		mPauseButton.setText(getString(resId));
		TextView textView = (TextView) findViewById(R.id.status_text_view);
		textView.setText(mSensor.getStatus());
	}

	public void onLightSensorChange() {
		Float read = (Float) mSensor.read();
		mSensorReadTextView.setText(read.toString());
	}

	// TODO: make this configurable
	private void disableKeyGuardForTesting() {
		KeyguardManager keyGuardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		keyGuardManager.newKeyguardLock("com.kaipic.lightmeter.MainWindow")
				.disableKeyguard();
	}

	protected void onResume() {
		super.onResume();
		if(mSensor != null)  mSensor.start();
	}

	protected void onStop() {
		super.onStop();
		if(mSensor != null) mSensor.stop();
	}

}