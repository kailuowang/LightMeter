package com.kaipic.lightmeter.test;

import android.app.Instrumentation;
import android.app.KeyguardManager;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
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
  private Spinner mIsoSpinner;
  private Spinner mExposureSpinner;
  private Spinner mApertureSpinner;
  private Spinner mShutterSpeedSpinner;
  private RadioButton mAvRadioButton;
  private RadioButton mMRadioButton;
  private View mApertureSpinnerRow;
  private View mShutterSpeedSpinnerRow;
  private View mShutterSpeedResultRow;
  private View mExposureSpinnerRow;
  private View mExposureDisplayRow;

  public MainWindowTest() {
    super("com.kaipic.lightmeter", MainWindow.class);
  }

  protected void setUp() throws Exception {
    super.setUp();
    mInstrumentation = getInstrumentation();
    setActivityInitialTouchMode(false);
    mActivity = getActivity();
    mButton = (Button) mActivity.findViewById(R.id.pause_button);
    mExposureValueView = (TextView) mActivity.findViewById(R.id.exposureValue);
    mIsoSpinner = (Spinner) mActivity.findViewById(R.id.isoSpinner);
    mExposureSpinner = (Spinner) mActivity.findViewById(R.id.exposureSpinner);
    mApertureSpinner = (Spinner) mActivity.findViewById(R.id.apertureSpinner);
    mShutterSpeedSpinner = (Spinner) mActivity.findViewById(R.id.shutterSpeedSpinner);
    mAvRadioButton = (RadioButton) mActivity.findViewById(R.id.radio_Av);
    mMRadioButton = (RadioButton) mActivity.findViewById(R.id.radio_Manual);
    mApertureSpinnerRow = mActivity.findViewById(R.id.apertureSpinnerRow);
    mShutterSpeedSpinnerRow = mActivity.findViewById(R.id.shutterSpeedSpinnerRow);
    mShutterSpeedResultRow = mActivity.findViewById(R.id.shutterSpeedResultRow);
    mExposureSpinnerRow = mActivity.findViewById(R.id.exposureSpinnerRow);
    mExposureDisplayRow = mActivity.findViewById(R.id.exposureDisplayRow);
    disableKeyGuardForTesting();
  }

  private void disableKeyGuardForTesting() {
    KeyguardManager keyGuardManager = (KeyguardManager) mActivity.getSystemService(mActivity.KEYGUARD_SERVICE);
    keyGuardManager.newKeyguardLock("com.kaipic.lightmeter.MainWindow").disableKeyguard();
  }


  protected void tearDown() throws Exception {
    runOnUiThread(new Runnable() {
      public void run() {
        mIsoSpinner.setSelection(0, false);
        mExposureSpinner.setSelection(0, false);
        mApertureSpinner.setSelection(0, false);
      }
    });

    super.tearDown();
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

  public void testSetISOShouldSetISOtoLightMeter() {
    runOnUiThread(new Runnable() {
      public void run() {
        mIsoSpinner.requestFocus();
        mIsoSpinner.setSelection(2);
      }
    });
    String expectedISO = mActivity.getResources().getStringArray(R.array.isos)[2];
    String actual = String.valueOf(mActivity.getLightMeter().getISO());
    assertEquals(expectedISO, actual);
  }


  public void testSetShutterSpeedShouldSetShutterSpeedToLightMeter() {
    click(mMRadioButton);
    runOnUiThread(new Runnable() {
      public void run() {
        mShutterSpeedSpinner.requestFocus();
        mShutterSpeedSpinner.setSelection(2);
      }
    });
    ShutterSpeed expected = new ShutterSpeed(mActivity.getResources().getStringArray(R.array.shutterSpeeds)[2]);
    assertEquals(expected.toString(), mActivity.getWorkMode().getShutterSpeed().toString());
  }

  public void testSwitchModeUsingRadioButton() {
    click(mMRadioButton);
    assertEquals(mActivity.getWorkMode().getClass(), ManualMode.class);
    click(mAvRadioButton);
    assertEquals(mActivity.getWorkMode().getClass(), AvMode.class);
  }

  public void testAvMode() {
    click(mAvRadioButton);
    assertTrue(mApertureSpinnerRow.isShown());
    assertTrue(mShutterSpeedResultRow.isShown());
    assertFalse(mShutterSpeedSpinnerRow.isShown());
    assertTrue(mExposureSpinnerRow.isShown());
  }

  public void testMMode() {
    click(mMRadioButton);
    assertTrue(mApertureSpinnerRow.isShown());
    assertFalse(mShutterSpeedResultRow.isShown());
    assertTrue(mShutterSpeedSpinnerRow.isShown());
    assertFalse(mExposureSpinnerRow.isShown());
    assertTrue(mExposureDisplayRow.isShown());
  }


  public void testSetExposureValueShouldResultInSuchExposureValueInLightMeter() {
    setExposureValueSpinnerTo(3);
    assertEquals(3f, mActivity.getWorkMode().getExposureAtISO100().getValue(), 0.001f);
  }

  public void testSetManualExposureValueShouldHideExposureDisplayRow() {
    setExposureValueSpinnerTo(3);
    assertFalse(mActivity.findViewById(R.id.exposureDisplayRow).isShown());
  }

  public void testSetExposureValueToAutoShouldResultInAutomaticLightSensor() {
    setExposureValueSpinnerToAuto();
    assertEquals(LightSensorType.AUTO, mActivity.getLightMeter().getLightSensor().getType());
  }

  public void testSetExposureValueToAutoShouldShowLockButton() {
    setExposureValueSpinnerToAuto();
    assertTrue(mButton.isShown());
  }

  public void testSetExposureValueToManualShouldHideLockButton() {
    setExposureValueSpinnerTo(1);
    assertFalse(mButton.isShown());
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

  public void testSetupSpinnerShouldRememberLastPosition() {
    runOnUiThread(new Runnable() {
      public void run() {
        mIsoSpinner.requestFocus();
        mIsoSpinner.setSelection(2);
      }
    });
    mActivity.saveSettings();
    runOnUiThread(new Runnable() {
      public void run() {
        mActivity.setupSpinner(mIsoSpinner, R.array.isos);
        assertEquals(2, mIsoSpinner.getSelectedItemPosition());
        mIsoSpinner.setSelection(0);
      }
    });
  }

  private void setExposureValueSpinnerTo(final int position) {
    runOnUiThread(new Runnable() {
      public void run() {
        mExposureSpinner.requestFocus();
        mExposureSpinner.setSelection(position);
      }
    });
  }

  private void setExposureValueSpinnerToAuto() {
    setExposureValueSpinnerTo(0);
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

  private void click(final View button) {
    mActivity.runOnUiThread(new Runnable() {
      public void run() {
        button.requestFocus();
      }
    });

    mInstrumentation.waitForIdleSync();
    this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
  }
}
