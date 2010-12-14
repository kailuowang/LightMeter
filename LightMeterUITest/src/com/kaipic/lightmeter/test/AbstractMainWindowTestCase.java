package com.kaipic.lightmeter.test;

import android.app.Instrumentation;
import android.app.KeyguardManager;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import com.kaipic.lightmeter.MainWindow;
import com.kaipic.lightmeter.R;
import com.kaipic.lightmeter.lib.CameraSettingsRepository;
import com.kaipic.lightmeter.lib.ExposureValue;

import java.util.Arrays;

public abstract class AbstractMainWindowTestCase extends ActivityInstrumentationTestCase2<MainWindow> {
  private Instrumentation mInstrumentation;
  protected MainWindow mActivity;
  protected Button mLockButton;
  protected TextView mExposureValueView;
  protected Spinner mIsoSpinner;
  protected Spinner mExposureSpinner;
  protected Spinner mApertureSpinner;
  protected Spinner mShutterSpeedSpinner;
  private RadioButton mAvRadioButton;
  protected RadioButton mMRadioButton;
  protected RadioButton mSvRadioButton;
  protected TextView mApertureTextView;
  private RadioButton mAutoExposureRadioButton;
  protected RadioButton mManualExposureRadioButton;
  protected View mExposureSettingRadioGroup;
  protected TextView mShutterSpeedTextView;
  protected EditText mSubjectDistanceEditText;
  protected Spinner mFocalLengthSpinner;
  protected Spinner mCirclesOfConfusionSpinner;
  protected Spinner mLengthUnitSpinner;

  public AbstractMainWindowTestCase() {
    super("com.kaipic.lightmeter", MainWindow.class);
  }

  protected void setUp() throws Exception {
    super.setUp();
    mInstrumentation = getInstrumentation();
    setActivityInitialTouchMode(false);
    mActivity = getActivity();
    mLockButton = (Button) mActivity.findViewById(com.kaipic.lightmeter.R.id.pause_button);
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
    mActivity.clearSettings();
  }

  private void disableKeyGuardForTesting() {
    KeyguardManager keyGuardManager = (KeyguardManager) mActivity.getSystemService(mActivity.KEYGUARD_SERVICE);
    keyGuardManager.newKeyguardLock("com.kaipic.lightmeter.MainWindow").disableKeyguard();
  }

  protected void tearDown() throws Exception {
    super.tearDown();
  }

  protected void switchToAv() {
    click(mAvRadioButton);
  }

  protected void switchToAutoExposure() {
    click(mAutoExposureRadioButton);
  }

  protected void switchToManualExposure() {
    click(mManualExposureRadioButton);
  }

  protected String getTextViewText(int viewId) {
    return ((TextView) mActivity.findViewById(viewId)).getText().toString();
  }

  protected void setExposureValueSpinnerTo(final int position) {
    click(mManualExposureRadioButton);
    setSpinnerSelection(mExposureSpinner, position);
  }

  protected void setExposureValueSpinnerTo(ExposureValue exposureValue) {
    setExposureValueSpinnerTo(Arrays.asList(CameraSettingsRepository.exposureValues).indexOf(exposureValue));
  }

  protected void setSpinnerSelection(final Spinner spinner, final int position) {
    runOnUiThread(new Runnable() {
      public void run() {
        spinner.requestFocus();
        spinner.setSelection(position);
      }
    });
    mInstrumentation.waitForIdleSync();
  }

  protected void runOnUiThread(Runnable runnable) {
    mActivity.runOnUiThread(runnable);
    mInstrumentation.waitForIdleSync();
  }

  protected void click(final View button) {
    mActivity.runOnUiThread(new Runnable() {
      public void run() {
        button.requestFocus();
      }
    });

    mInstrumentation.waitForIdleSync();
    this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
  }

  protected String getString(int name) {
    return mActivity.getString(name);
  }

  protected void setSubjectDistance(final String value) {
    runOnUiThread(new Runnable() {
      public void run() {
        mSubjectDistanceEditText.setText(value);
      }
    });
  }
}
