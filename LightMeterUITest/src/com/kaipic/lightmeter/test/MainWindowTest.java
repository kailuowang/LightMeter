package com.kaipic.lightmeter.test;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;
import com.kaipic.lightmeter.MainWindow;
import com.kaipic.lightmeter.R;
import com.kaipic.lightmeter.lib.*;

public class MainWindowTest extends
    ActivityInstrumentationTestCase2<MainWindow> {
  private Instrumentation mInstrumentation;
  private MainWindow mActivity;
  private Button mButton;
  private TextView mExposureValueView;

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
    mExposureValueView = (TextView) mActivity.findViewById(R.id.exposureValue);
  }

  public void testCreateActivity() {
    assertNotNull(mActivity);
    assertNotNull(mButton);
    assertNotNull(mExposureValueView);
  }

  public void testPauseButtonClickShouldPauseSensor() {
    MockLightSensor sensor = new MockLightSensor();
    mActivity.getLightMeter().setLightSensor(sensor);
    assertFalse(sensor.isPaused());
    click(mButton);
    assertTrue(sensor.isPaused());
  }


  public void testPauseButtonClickShouldToggleButtonLabel() {
    mActivity.getLightMeter().setLightSensor(new MockLightSensor());
    assertEquals(getString(R.string.pause), mButton.getText());
    click(mButton);
    assertEquals(getString(R.string.continue_btn), mButton.getText());
  }

  public void testDisplayShouldDisplayLightMeter() {
    LightMeter lightMeter = mActivity.getLightMeter();
    lightMeter.setLightSensor(createMockLightSensor(10f));
    lightMeter.setAperture(3.5f).setCalibration(250).setISO(100);
    runOnUiThread(new Runnable() {
      public void run() {
        mActivity.display();
      }
    });
    assertEquals("EV10.0", ((TextView) mActivity.findViewById(R.id.exposureValue)).getText());
    assertTrue(((TextView) mActivity.findViewById(R.id.shutterSpeed)).getText().length() > 0);
  }

  public void testSetAperture() {
    mActivity.setAperture("5.6");
    assertEquals(new Aperture(5.6f), mActivity.getLightMeter().getAperture());
  }

  public void testListenToSensorAndDisplayRead() {
    final ManualLightSensor lightSensor = new ManualLightSensor();
    lightSensor.setEVByEVAt100(new ExposureValue(9f));
    mActivity.getLightMeter().setLightSensor(lightSensor);

    lightSensor.setEVByEVAt100(new ExposureValue(10f));
    runOnUiThread(new Runnable() {
      public void run() {
        lightSensor.broadcast();
      }
    });
    assertEquals("EV10.0", mExposureValueView.getText());
  }

  private LightSensor createMockLightSensor(final float ev) {
    return new LightSensor() {
      public float read() {
        return 0;
      }

      public ExposureValue getEV() {
        return new ExposureValue(ev);
      }
    };
  }

  private void runOnUiThread(Runnable runnable) {
    mActivity.runOnUiThread(runnable);
    mInstrumentation.waitForIdleSync();
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
