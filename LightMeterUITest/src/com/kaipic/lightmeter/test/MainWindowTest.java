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
    mActivity.setLightMeter(new LightMeter(sensor));
    assertFalse(sensor.isPaused());
    click(mButton);
    assertTrue(sensor.isPaused());
  }


  public void testPauseButtonClickShouldToggleButtonLabel() {
    mActivity.setLightMeter(new LightMeter(new MockLightSensor()));
    assertEquals(getString(R.string.pause), mButton.getText());
    click(mButton);
    assertEquals(getString(R.string.continue_btn), mButton.getText());
  }

  public void testDisplayShouldDisplayLightMeter() {
    LightMeter lightMeter = createTestLightMeter(10f);
    lightMeter.setAperture(3.5f).setCalibration(250).setISO(100);
    mActivity.setLightMeter(lightMeter);
    runOnUiThread(new Runnable() {
      public void run() {
        mActivity.display();
      }
    });
    assertEquals("EV10.0", ((TextView) mActivity.findViewById(R.id.exposureValue)).getText());
    assertTrue(((TextView) mActivity.findViewById(R.id.shutterSpeed)).getText().length() > 0);
  }

  private LightMeter createTestLightMeter(final float ev) {
    LightMeter lightMeter = new LightMeter(new LightSensor() {
      public float read() {
        return 0;
      }

      public ExposureValue getEV() {
        return new ExposureValue(ev);
      }
    });
    return lightMeter;
  }

  public void testSetAperture() {
    LightMeter lightMeter = new LightMeter(new MockLightSensor());
    mActivity.setLightMeter(lightMeter);
    mActivity.setAperture("5.6");
    assertEquals(new Aperture(5.6f), lightMeter.getAperture());
  }

  private void runOnUiThread(Runnable runnable) {
    mActivity.runOnUiThread(runnable);
    mInstrumentation.waitForIdleSync();
  }

  public void testListenToSensorAndDisplayRead() {
    final LightMeter lightMeter = createTestLightMeter(10f);
    mActivity.setLightMeter(lightMeter);
    runOnUiThread(new Runnable() {
      public void run() {
        lightMeter.getLightSensor().broadcast();
      }
    });
    assertEquals("EV10.0", mExposureValueView.getText());
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
