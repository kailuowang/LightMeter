package com.kaipic.lightmeter.test;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;
import com.kaipic.lightmeter.MainWindow;
import com.kaipic.lightmeter.R;
import com.kaipic.lightmeter.lib.LightMeter;
import com.kaipic.lightmeter.lib.MockLightSensor;

public class MainWindowTest extends
		ActivityInstrumentationTestCase2<MainWindow> {
	private Instrumentation mInstrumentation;
	private MainWindow mActivity;
	private Button mButton;
	private TextView mSensorReadView;

	public MainWindowTest() {
		super("com.kaipic.lightmeter", MainWindow.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		mInstrumentation = getInstrumentation();
		MainWindow.isTesting = true;
		setActivityInitialTouchMode(false);
		mActivity = getActivity();
		mButton = (Button) mActivity.findViewById(R.id.pause_button);
		mSensorReadView = (TextView) mActivity.findViewById(R.id.illumination);
	}

	public void testCreateActivity() {
		assertNotNull(mActivity);
		assertNotNull(mButton);
		assertNotNull(mSensorReadView);
		assertEquals("", mSensorReadView.getText());
	}

	public void testPauseButtonClickShouldPauseSensor() {
		MockLightSensor sensor = new MockLightSensor();
		mActivity.setLightMeter(new LightMeter(sensor));
		assertFalse(sensor.isPaused());
		click(mButton);
		assertTrue(sensor.isPaused());
	}
	
	public void testPauseButtonClickShouldToggleButtonLable() {
		mActivity.setLightMeter(new LightMeter(new MockLightSensor()));
		assertEquals(getString(R.string.pause), mButton.getText());
		click(mButton);
		assertEquals( getString(R.string.continue_btn), mButton.getText());
	}
	
	public void testDisplayShouldDisplayLightMeter(){
		LightMeter lightMeter = new LightMeter(new MockLightSensor().setRead(14.3f));
		lightMeter.setAperture(3.5f).setCalibration(250).setISO(100);
		mActivity.setLightMeter(lightMeter);
		runOnUiThread(new Runnable() {
			public void run() { mActivity.display(); }
		});
		assertEquals("f3.5", ((TextView)mActivity.findViewById(R.id.aperture)).getText());
		assertEquals("100", ((TextView)mActivity.findViewById(R.id.iso)).getText());
		assertEquals("14.3 lux", ((TextView)mActivity.findViewById(R.id.illumination)).getText());
		assertTrue(((TextView)mActivity.findViewById(R.id.shutterSpeed)).getText().length() > 0 );
	}
	
	

	private void runOnUiThread(Runnable runable) {
		mActivity.runOnUiThread(runable);
		mInstrumentation.waitForIdleSync();
	}
	
	public void testListenToSensorAndDisplayRead(){
		final MockLightSensor sensor = new MockLightSensor().setRead(14.3f);
		mActivity.setLightMeter(new LightMeter(sensor));
		runOnUiThread(new Runnable() {
			public void run() { sensor.broadCast(); }
		});
		assertEquals("14.3 lux", mSensorReadView.getText());
	} 

	private String getString(int name) {
		return mActivity.getString(name);
	}
	
	private void click(final Button button) {
		mActivity.runOnUiThread(new Runnable() {
			public void run() {
				button.requestFocus();
			}
		});
		
		mInstrumentation.waitForIdleSync();
		this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
	}

}
