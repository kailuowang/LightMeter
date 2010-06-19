package com.kaipic.lightmeter;

import android.app.Activity;
import android.app.KeyguardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainWindow extends Activity {
	private LightSensor mSensor;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		disableKeyGuardForTesting();
		setContentView(R.layout.main);
		Button readButton = (Button) findViewById(R.id.read_button);
		readButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView sensor_read_text_view = (TextView) findViewById(R.id.sensor_read_text_view);
				sensor_read_text_view.setText("1");
			}
		});
	}

	//TODO: make this configurable
	private void disableKeyGuardForTesting() {
		KeyguardManager keyGuardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		keyGuardManager.newKeyguardLock("com.kaipic.lightmeter.MainWindow").disableKeyguard();
	}

	public void setSensor(LightSensor sensor) {
		this.mSensor = sensor;
	}

	public LightSensor getSensor() {
		return mSensor;
	}

}