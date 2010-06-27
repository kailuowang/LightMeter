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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainWindow extends Activity implements LightSensorListener {
	private LightMeter mLightMeter;
	private Button mPauseButton;
	private TextView mSensorReadTextView;
	private TextView mShutterSpeedTextView;
	private Spinner mAppertureSpinner;
	private Spinner mISOSpinner;
    public static boolean isTesting = false;
	
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
	
	public LightMeter getLightMeter(){
		return mLightMeter;
	}

	private void startSensor() {
		getLightSensor().start();
	}

	public void onLightSensorChange() {
		display();
	}

	public void display() {
		mSensorReadTextView.setText(((Float)mLightMeter.readLight()).toString() + " lux");
		mShutterSpeedTextView.setText(mLightMeter.calculateShutterSpeed().toString());
	}
	
	private void initializeFields() {
		mPauseButton = (Button) findViewById(R.id.pause_button);
		mSensorReadTextView = (TextView) findViewById(R.id.illumination);
		mShutterSpeedTextView = (TextView) findViewById(R.id.shutterSpeed);
		setLightMeter(new LightMeter(getSensor()));
		mAppertureSpinner = (Spinner) findViewById(R.id.apertureSpinner);
	    mISOSpinner = (Spinner) findViewById(R.id.isoSpinner);
	    setupSpinner(mISOSpinner, R.array.isos);
	    setupSpinner(mAppertureSpinner, R.array.appertures);
	}

	private void setupSpinner(Spinner spinner, int itemArray) {
		ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(
	            this, itemArray, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	private LightSensor getSensor() {
		return isTesting ? new LightSensorSimulator() : new AmbientLightSensor(getApplicationContext());
	}

	private void registerEvents() {
		mPauseButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			  toggleLock();
			  display();
			}
		});
		
		registerSpinnerListenner(mAppertureSpinner, new SpinnerItemSelectListenner() {
			public void onSpinnerItemSelected(Object selectedValue) {
				setAperture((String)selectedValue);
				display();
			}
		});
		registerSpinnerListenner(mISOSpinner, new SpinnerItemSelectListenner() {
			public void onSpinnerItemSelected(Object selectedValue) {
				mLightMeter.setISO(Integer.parseInt((String)selectedValue));
				display();
			}
		});
	}

	private void registerSpinnerListenner(Spinner spinner, final SpinnerItemSelectListenner listener) {
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Object aperture = arg0.getItemAtPosition(arg2);
				listener.onSpinnerItemSelected(aperture);
			}
			public void onNothingSelected(AdapterView<?> arg0) { }
		});
	}
	
	public void setAperture(String aperture) {
	   mLightMeter.setAperture(Aperture.fromString(aperture));
	}

	private void toggleLock() {
		LightSensor lightSensor = getLightSensor();
		lightSensor.togglePause();
		boolean locked = lightSensor.isPaused();
		if(locked) mLightMeter.lock(); else mLightMeter.unlock();
		int resId = locked ? R.string.continue_btn : R.string.pause;
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

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
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