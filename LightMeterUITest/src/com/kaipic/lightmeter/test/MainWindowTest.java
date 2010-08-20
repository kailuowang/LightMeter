package com.kaipic.lightmeter.test;

import android.app.Instrumentation;
import android.app.KeyguardManager;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import com.kaipic.lightmeter.MainWindow;
import com.kaipic.lightmeter.R;
import com.kaipic.lightmeter.lib.*;

import java.util.Arrays;

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
  private RadioButton mSvRadioButton;
  private TextView mApertureTextView;
  private RadioButton mAutoExposureRadioButton;
  private RadioButton mManualExposureRadioButton;
  private View mExposureSettingRadioGroup;
  private TextView mShutterSpeedTextView;
  private EditText mSubjectDistanceEditText;
  private Spinner mFocalLengthSpinner;
  private Spinner mCirclesOfConfusionSpinner;
  private Spinner mLengthUnitSpinner;

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
    mApertureTextView = (TextView) mActivity.findViewById(R.id.aperture);
    mShutterSpeedTextView = (TextView) mActivity.findViewById(R.id.shutterSpeed);
    mIsoSpinner = (Spinner) mActivity.findViewById(R.id.isoSpinner);
    mExposureSpinner = (Spinner) mActivity.findViewById(R.id.exposureSpinner);
    mApertureSpinner = (Spinner) mActivity.findViewById(R.id.apertureSpinner);
    mFocalLengthSpinner = (Spinner) mActivity.findViewById(R.id.focalLengthSpinner);
    mShutterSpeedSpinner = (Spinner) mActivity.findViewById(R.id.shutterSpeedSpinner);
    mCirclesOfConfusionSpinner = (Spinner) mActivity.findViewById(R.id.circlesOfConfusionSpinner);
    mLengthUnitSpinner = (Spinner) mActivity.findViewById(R.id.lengthUnitSpinner);
    mAvRadioButton = (RadioButton) mActivity.findViewById(R.id.radio_Av);
    mSvRadioButton = (RadioButton) mActivity.findViewById(R.id.radio_Sv);
    mMRadioButton = (RadioButton) mActivity.findViewById(R.id.radio_Manual);
    mAutoExposureRadioButton = (RadioButton) mActivity.findViewById(R.id.radioAutoExposure);
    mManualExposureRadioButton = (RadioButton) mActivity.findViewById(R.id.radioManualExposure);
    mExposureSettingRadioGroup = mActivity.findViewById(R.id.exposureSettingRadioGroup);
    mSubjectDistanceEditText = (EditText) mActivity.findViewById(R.id.subjectDistanceEditText);
    disableKeyGuardForTesting();
  }

  private void disableKeyGuardForTesting() {
    KeyguardManager keyGuardManager = (KeyguardManager) mActivity.getSystemService(mActivity.KEYGUARD_SERVICE);
    keyGuardManager.newKeyguardLock("com.kaipic.lightmeter.MainWindow").disableKeyguard();
  }


  protected void tearDown() throws Exception {
    setSpinnerSelection(mApertureSpinner, 0);
    setSpinnerSelection(mExposureSpinner, 0);
    setSpinnerSelection(mIsoSpinner, 0);
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
    lightMeter.setAperture(3.5f).setCalibration(250).setISO(new Iso(100));
    runOnUiThread(new Runnable() {
      public void run() {
        mActivity.display();
      }
    });
    assertEquals(new ExposureValue(10).toDetailString(), ((TextView) mActivity.findViewById(R.id.exposureValue)).getText());
    assertTrue(((TextView) mActivity.findViewById(R.id.shutterSpeed)).getText().length() > 0);
  }

  public void testExposureSpinnerItems() {
    String[] items = mActivity.exposureSpinnerItems();
    assertEquals(new ExposureValue(2).toDetailString(), items[2]);
  }

  public void testSetISOShouldSetISOtoLightMeter() {
    runOnUiThread(new Runnable() {
      public void run() {
        mIsoSpinner.requestFocus();
        mIsoSpinner.setSelection(2);
      }
    });
    Iso expectedISO = CameraSettingsRepository.isos[2];
    Iso actual = mActivity.getLightMeter().getISO();
    assertEquals(expectedISO, actual);
  }


  public void testSetShutterSpeedShouldSetShutterSpeedToLightMeter() {
    click(mMRadioButton);
    setSpinnerSelection(mShutterSpeedSpinner, 3);
    ShutterSpeed expected = CameraSettingsRepository.shutterSpeeds[3];
    assertEquals(expected.toString(), mActivity.getWorkMode().getShutterSpeed().toString());
  }

  public void testSetApertureSpinnerShouldSetApertureToLightMeter() {
    click(mMRadioButton);
    setSpinnerSelection(mApertureSpinner, 2);
    Aperture expected = CameraSettingsRepository.apertures[2];
    assertEquals(expected.toString(), mActivity.getWorkMode().getAperture().toString());
  }

  public void testSwitchModeUsingRadioButton() {
    click(mMRadioButton);
    assertEquals(mActivity.getWorkMode().getClass(), ManualMode.class);
    click(mAvRadioButton);
    assertEquals(mActivity.getWorkMode().getClass(), AvMode.class);
    click(mSvRadioButton);
    assertEquals(mActivity.getWorkMode().getClass(), SvMode.class);
  }

  public void testAvMode() {
    click(mAvRadioButton);
    assertTrue(mApertureSpinner.isShown());
    assertTrue(mShutterSpeedTextView.isShown());
    assertFalse(mShutterSpeedSpinner.isShown());
    assertTrue(mExposureSettingRadioGroup.isShown());
    assertFalse(mApertureTextView.isShown());
  }

  public void testSvMode() {
    click(mSvRadioButton);
    assertFalse(mApertureSpinner.isShown());
    assertFalse(mShutterSpeedTextView.isShown());
    assertTrue(mShutterSpeedSpinner.isShown());
    assertTrue(mApertureTextView.isShown());
    assertTrue(mExposureSettingRadioGroup.isShown());
    assertEquals(mApertureTextView.getText(), mActivity.getWorkMode().getAperture().toString());
  }

  public void testMMode() {
    click(mMRadioButton);
    assertTrue(mApertureSpinner.isShown());
    assertFalse(mShutterSpeedTextView.isShown());
    assertTrue(mShutterSpeedSpinner.isShown());
    assertFalse(mExposureSpinner.isShown());
    assertFalse(mExposureSettingRadioGroup.isShown());
    assertTrue(mExposureValueView.isShown());
    assertFalse(mApertureTextView.isShown());
  }

  public void testSetExposureValueShouldResultInSuchExposureValueInLightMeter() {
    setExposureValueSpinnerTo(3);
    assertEquals(4f, mActivity.getWorkMode().getExposure().getValue(), 0.001f);
  }

  public void testSetManualExposureValueShouldHideExposureDisplayRow() {
    setExposureValueSpinnerTo(3);
    assertFalse(mExposureValueView.isShown());
  }

  public void testSetExposureValueToAutoShouldResultInAutomaticLightSensor() {
    click(mAutoExposureRadioButton);
    assertEquals(LightSensorType.AUTO, mActivity.getLightMeter().getLightSensor().getType());
    assertFalse(mExposureSpinner.isShown());
    assertTrue(mExposureValueView.isShown());
    assertTrue(mShutterSpeedTextView.isShown());
  }

  public void testSetExposureValueToManualShouldResultInManualLightSensor() {
    click(mManualExposureRadioButton);
    assertEquals(LightSensorType.MANUAL, mActivity.getLightMeter().getLightSensor().getType());
    assertTrue(!mActivity.getLightMeter().usingAutoLightSensor());
    assertTrue(mExposureSpinner.isShown());
    assertFalse(mExposureValueView.isShown());
  }

  public void testSetExposureValueToAutoShouldShowLockButton() {
    click(mAutoExposureRadioButton);
    assertTrue(mButton.isShown());
  }

  public void testSetExposureValueToManualShouldHideLockButton() {
    setExposureValueSpinnerTo(1);
    assertFalse(mButton.isShown());
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
    assertEquals(new ExposureValue(10).toDetailString(), mExposureValueView.getText());
  }

  public void testSetupSpinnerShouldRememberLastPosition() {
    setSpinnerSelection(mIsoSpinner, 2);
    mActivity.saveSettings();
    runOnUiThread(new Runnable() {
      public void run() {
        mActivity.setupSpinner(mIsoSpinner, CameraSettingsRepository.isos);
        assertEquals(2, mIsoSpinner.getSelectedItemPosition());
        mIsoSpinner.setSelection(0);
      }
    });
  }

  public void testTypeInSubjectDistanceShouldHideTitle() {
    assertTrue(mActivity.findViewById(R.id.depthOfFieldTitleTextView).isShown());
    assertFalse(mActivity.findViewById(R.id.depthOfFieldResultTable).isShown());
    runOnUiThread(new Runnable() {
      public void run() {
        mSubjectDistanceEditText.setText("343.3");
      }
    });
    assertFalse(mActivity.findViewById(R.id.depthOfFieldTitleTextView).isShown());
    assertTrue(mActivity.findViewById(R.id.depthOfFieldResultTable).isShown());
  }
  public void testSubjectDistanceAndUnitShouldSetInDofCalculator() {
    runOnUiThread(new Runnable() {
      public void run() {
        mSubjectDistanceEditText.setText("1.2");
      }
    });
    setSpinnerSelection(mLengthUnitSpinner, indexOf(LengthUnit.values(), LengthUnit.m));
    assertEquals(new Length(1200), mActivity.getDoFCalculator().getSubjectDistance());
  }

  public void testSetFocalLengthShouldSetItInDoFCalculator() {
    Length focalLength = new Length(35);
    setSpinnerSelection(mFocalLengthSpinner, indexOf(CameraSettingsRepository.focalLengths, focalLength));
    assertEquals(focalLength, mActivity.getDoFCalculator().getFocalLength());
  }

  public void testSetApertureShouldSetItInDoFCalculator() {
    Aperture aperture = new Aperture(2.8f);
    setSpinnerSelection(mApertureSpinner, indexOf(CameraSettingsRepository.apertures, aperture));
    assertEquals(aperture, mActivity.getDoFCalculator().getAperture());
  }

  public void testSetCameraFormatShouldSetItInDoFCalculator() {
    CirclesOfConfusion coc = CirclesOfConfusion.LF4x5;
    setSpinnerSelection(mCirclesOfConfusionSpinner, indexOf(CirclesOfConfusion.values(), coc));
    assertEquals(coc, mActivity.getDoFCalculator().getCirclesOfConfusion());
  }

  public void testIndexOf() {
    assertTrue(indexOf(CameraSettingsRepository.focalLengths, new Length(35)) > 1);
  }

  private int indexOf(Object[] items, Object item) {
    return Arrays.asList(items).indexOf(item);
  }

  private void setExposureValueSpinnerTo(final int position) {
    click(mManualExposureRadioButton);
    setSpinnerSelection(mExposureSpinner, position);
  }

  private void setSpinnerSelection(final Spinner spinner, final int position) {
    runOnUiThread(new Runnable() {
      public void run() {
        spinner.requestFocus();
        spinner.setSelection(position);
      }
    });
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
