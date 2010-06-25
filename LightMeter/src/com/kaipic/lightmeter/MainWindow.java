package com.kaipic.lightmeter;

import com.kaipic.lightmeter.lib.AmbientLightSensor;
import com.kaipic.lightmeter.lib.Aperture;
import com.kaipic.lightmeter.lib.LightMeter;
import com.kaipic.lightmeter.lib.LightSensor;
import com.kaipic.lightmeter.lib.LightSensorListener;
import com.kaipic.lightmeter.lib.LightSensorSimulator;

import android.app.Activity;
import android.app.KeyguardManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainWindow extends Activity implements LightSensorListener {
	private LightMeter mLightMeter;
	private Button mPauseButton;
	private TextView mSensorReadTextView;
	private TextView mISOTextView;
	private TextView mShutterSpeedTextView;
	private Spinner mAppertureSpinner;
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
		mISOTextView.setText(((Integer)mLightMeter.getISO()).toString() );
		mShutterSpeedTextView.setText(mLightMeter.calculateShutterSpeed().toString());
	}
	
	private void initializeFields() {
		mPauseButton = (Button) findViewById(R.id.pause_button);
		mSensorReadTextView = (TextView) findViewById(R.id.illumination);
		mISOTextView = (TextView) findViewById(R.id.iso);
		mShutterSpeedTextView = (TextView) findViewById(R.id.shutterSpeed);
		mLightMeter = new LightMeter(getSensor());
	    mAppertureSpinner = (Spinner) findViewById(R.id.apertureSpinner);
	    ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(
	            this, R.array.appertures, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    mAppertureSpinner.setAdapter(adapter);
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
		
		mAppertureSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				CharSequence aperture = (CharSequence) arg0.getItemAtPosition(arg2);
				setAperture(aperture);
				display();
			}
			public void onNothingSelected(AdapterView<?> arg0) { }
		});
	}

	public void setAperture(CharSequence aperture) {
	   mLightMeter.setAperture(Aperture.fromString((String)aperture));
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