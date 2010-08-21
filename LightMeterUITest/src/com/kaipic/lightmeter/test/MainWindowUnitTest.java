package com.kaipic.lightmeter.test;

import com.kaipic.lightmeter.R;
import com.kaipic.lightmeter.lib.*;

import static com.kaipic.lightmeter.lib.Util.indexOf;

public class MainWindowUnitTest extends AbstractMainWindowTestCase {


  public void testPauseButtonClickShouldPauseSensor() {
    switchToAv();
    switchToAutoExposure();
    LightSensor sensor = mActivity.getLightMeter().getLightSensor();
    assertFalse(sensor.isPaused());
    click(mLockButton);
    assertTrue(sensor.isPaused());
  }

  public void testPauseButtonClickShouldToggleButtonLabel() {
    switchToAv();
    switchToAutoExposure();
    assertEquals(getString(R.string.pause), mLockButton.getText());
    click(mLockButton);
    assertEquals(getString(R.string.continue_btn), mLockButton.getText());
  }

  public void testSetISOShouldSetISOtoLightMeter() {
    Iso iso = new Iso(200);
    setSpinnerSelection(mIsoSpinner, indexOf(CameraSettingsRepository.isos, iso));
    Iso actual = mActivity.getLightMeter().getISO();
    assertEquals(iso, actual);
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
    switchToAv();
    assertEquals(mActivity.getWorkMode().getClass(), AvMode.class);
    click(mSvRadioButton);
    assertEquals(mActivity.getWorkMode().getClass(), SvMode.class);
  }

  public void testAvModeVisibilities() {
    switchToAv();
    assertTrue(mApertureSpinner.isShown());
    assertTrue(mShutterSpeedTextView.isShown());
    assertFalse(mShutterSpeedSpinner.isShown());
    assertTrue(mExposureSettingRadioGroup.isShown());
    assertFalse(mApertureTextView.isShown());
  }

  public void testSvModeVisibilities() {
    click(mSvRadioButton);
    assertFalse(mApertureSpinner.isShown());
    assertFalse(mShutterSpeedTextView.isShown());
    assertTrue(mShutterSpeedSpinner.isShown());
    assertTrue(mApertureTextView.isShown());
    assertTrue(mExposureSettingRadioGroup.isShown());
    assertEquals(mApertureTextView.getText(), mActivity.getWorkMode().getAperture().toString());
  }

  public void testMModeVisibilities() {
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
    assertEquals(3f, mActivity.getWorkMode().getExposure().getValue(), 0.001f);
  }

  public void testSetAutoExposureShouldResultInAutomaticLightSensor() {
    switchToAutoExposure();
    assertEquals(LightSensorType.AUTO, mActivity.getLightMeter().getLightSensor().getType());
  }

  public void testAutoExposureVisibilities() {
    switchToAutoExposure();
    assertFalse(mExposureSpinner.isShown());
    assertTrue(mExposureValueView.isShown());
    assertTrue(mShutterSpeedTextView.isShown());
    assertTrue(mLockButton.isShown());
  }

  public void testSetExposureValueToManualShouldResultInManualLightSensor() {
    switchToManualExposure();
    assertEquals(LightSensorType.MANUAL, mActivity.getLightMeter().getLightSensor().getType());
    assertTrue(!mActivity.getLightMeter().usingAutoLightSensor());
  }

  public void testManualExposureVisibilities() {
    switchToManualExposure();
    assertTrue(mExposureSpinner.isShown());
    assertFalse(mExposureValueView.isShown());
    assertFalse(mLockButton.isShown());
  }

  public void testSetupSpinnerWithDefaultItem() {
    runOnUiThread(new Runnable() {
      public void run() {
        mActivity.setupSpinner(mCirclesOfConfusionSpinner, CirclesOfConfusion.values(), CirclesOfConfusion.APS_C);
      }
    });
    assertEquals(CirclesOfConfusion.APS_C, mCirclesOfConfusionSpinner.getSelectedItem());
  }

  public void testVisibilitiesBasedOnDoFCalculatorIsValid() {
    assertTrue(mActivity.findViewById(R.id.depthOfFieldTitleTextView).isShown());
    assertFalse(mActivity.findViewById(R.id.depthOfFieldResultTable).isShown());
    setSubjectDistance("343.3");
    assertFalse(mActivity.findViewById(R.id.depthOfFieldTitleTextView).isShown());
    assertTrue(mActivity.findViewById(R.id.depthOfFieldResultTable).isShown());
  }

  public void testSubjectDistanceAndUnitShouldSetInDofCalculator() {
    setSubjectDistance("1.2");
    setSpinnerSelection(mLengthUnitSpinner, indexOf(LengthUnit.selectableUnits(), LengthUnit.m));
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
    assertTrue(indexOf(CirclesOfConfusion.values(), null) < 0);
  }

}
