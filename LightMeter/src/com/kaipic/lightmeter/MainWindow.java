package com.kaipic.lightmeter;

import com.kaipic.lightmeter.lib.AmbientLightSensor;
import com.kaipic.lightmeter.lib.LightMeter;
import com.kaipic.lightmeter.lib.LightSensor;
import com.kaipic.lightmeter.lib.LightSensorListener;
import com.kaipic.lightmeter.lib.LightSensorSimulator;

import android.app.Activity;
import android.app.KeyguardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainWindow extends Activity implements LightSensorListener {
	private LightMeter mLightMeter;
	private Button mPauseButton;
	private TextView mSensorReadTextView;
	private TextView mApertureTextView;
	private TextView mISOTextView;
	private TextView mShutterSpeedTextView;
    public static boolean isTesting = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(isTesting) disableKeyGuardForTesting();
		setContentView(R.layout.main);
		initializeFields();
		registerEvents();
	}

	public void setLightMeter(LightMeter lightMeter){
		mLightMeter = lightMeter;
		getLightSensor().register(this);
		startSensor();
	}

	private void startSensor() {
		getLightSensor().start();
	}

	public void onLightSensorChange() {
		display();
	}

	public void display() {
		mSensorReadTextView.setText(((Float)getLightSensor().read()).toString() + " lux");
		mApertureTextView.setText("f"+((Float)mLightMeter.getAperture()).toString() );
		mISOTextView.setText(((Integer)mLightMeter.getISO()).toString() );
		mShutterSpeedTextView.setText(mLightMeter.calculateShutterSpeed().toString());
	}
	
	private void initializeFields() {
		mPauseButton = (Button) findViewById(R.id.pause_button);
		mSensorReadTextView = (TextView) findViewById(R.id.illumination);
		mApertureTextView = (TextView) findViewById(R.id.aperture);
		mISOTextView = (TextView) findViewById(R.id.iso);
		mShutterSpeedTextView = (TextView) findViewById(R.id.shutterSpeed);
		mLightMeter = new LightMeter(getSensor());
	}

	private LightSensor getSensor() {
		return isTesting ? new LightSensorSimulator() : new AmbientLightSensor(getApplicationContext());
	}

	private void registerEvents() {
		mPauseButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			  toggleSensor();
			  display();
			}
		});
	}

	private void toggleSensor() {
		LightSensor lightSensor = getLightSensor();
		lightSensor .togglePause();
		int resId = lightSensor.isPaused() ? R.string.continue_btn : R.string.pause;
		mPauseButton.setText(getString(resId));
		TextView textView = (TextView) findViewById(R.id.status_text_view);
		textView.setText(lightSensor.getStatus());
	}

	private void disableKeyGuardForTesting() {
		KeyguardManager keyGuardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		keyGuardManager.newKeyguardLock("com.kaipic.lightmeter.MainWindow") .disableKeyguard();
	}

	protected void onResume() {
		super.onResume();
		startSensor();
	}

	private LightSensor getLightSensor() {
		return mLightMeter.getLightSensor();
	}

	protected void onStop() {
		stopSensor();
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		stopSensor();
		super.onDestroy();
	}

	private void stopSensor() {
		getLightSensor().stop();
	}

}