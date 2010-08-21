package com.kaipic.lightmeter.test;

import android.widget.TextView;
import com.kaipic.lightmeter.R;
import com.kaipic.lightmeter.lib.*;

import static com.kaipic.lightmeter.lib.Util.indexOf;

public class MainWindowTest extends AbstractMainWindowTestCase {

  public void testCreateActivity() {
    assertNotNull(mActivity);
    assertNotNull(mLockButton);
    assertNotNull(mExposureValueView);
  }

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

  public void testDisplayShouldDisplayLightMeter() {
    switchToAv();
    switchToManualExposure();
    LightMeter lightMeter = mActivity.getLightMeter();
    lightMeter.setAperture(3.5f).setCalibration(250).setISO(new Iso(100));
    setExposureValueSpinnerTo(9);
    assertEquals("ExposureString", new ExposureValue(9).toDetailString(), getTextViewText(R.id.exposureValue));
    assertTrue(((TextView) mActivity.findViewById(R.id.shutterSpeed)).getText().length() > 0);
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

  public void testAvMode() {
    switchToAv();
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
    assertEquals(3f, mActivity.getWorkMode().getExposure().getValue(), 0.001f);
  }

  public void testSetManualExposureValueShouldHideExposureDisplayRow() {
    setExposureValueSpinnerTo(3);
    assertFalse(mExposureValueView.isShown());
  }

  public void testSetExposureValueToAutoShouldResultInAutomaticLightSensor() {
    switchToAutoExposure();
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
    switchToAutoExposure();
    assertTrue(mLockButton.isShown());
  }

  public void testSetExposureValueToManualShouldHideLockButton() {
    setExposureValueSpinnerTo(1);
    assertFalse(mLockButton.isShown());
  }


  public void testListenToSensorAndDisplayRead() {

    switchToAv();
    switchToAutoExposure();
    setSpinnerSelection(mIsoSpinner, indexOf(CameraSettingsRepository.isos, new Iso(100)));
    final MockLightSensor lightSensor = (MockLightSensor) mActivity.getLightMeter().getLightSensor();
    lightSensor.setRead(new ExposureValue(10f).toIllumination(new Iso(100), lightSensor.getCalibration()));
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
      }
    });
  }

  public void testSetupSpinnerWithDefaultItemShouldRememberLastPosition() {
    setSpinnerSelection(mIsoSpinner, 2);
    mActivity.saveSettings();
    runOnUiThread(new Runnable() {
      public void run() {
        mActivity.setupSpinner(mIsoSpinner, CameraSettingsRepository.isos, new Iso(1600));
        assertEquals(2, mIsoSpinner.getSelectedItemPosition());
      }
    });
  }

  public void testSetupSpinnerWithDefaultItem() {
    runOnUiThread(new Runnable() {
      public void run() {
        mActivity.setupSpinner(mCirclesOfConfusionSpinner, CirclesOfConfusion.values(), CirclesOfConfusion.APS_C);
      }
    });
    assertEquals(CirclesOfConfusion.APS_C, mCirclesOfConfusionSpinner.getSelectedItem());
  }

  public void testTypeInSubjectDistanceShouldHideTitle() {
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

  private void setSubjectDistance(final String value) {
    runOnUiThread(new Runnable() {
      public void run() {
        mSubjectDistanceEditText.setText(value);
      }
    });
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

  public void testDoFCalculation() {
    setSpinnerSelection(mApertureSpinner, indexOf(CameraSettingsRepository.apertures, new Aperture(4f)));
    setSpinnerSelection(mCirclesOfConfusionSpinner, indexOf(CirclesOfConfusion.values(), CirclesOfConfusion.FULL_FRAME_35MM));
    setSpinnerSelection(mFocalLengthSpinner, indexOf(CameraSettingsRepository.focalLengths, new Length(50)));
    setSpinnerSelection(mLengthUnitSpinner, indexOf(LengthUnit.selectableUnits(), LengthUnit.m));
    setSubjectDistance("5");
    assertEquals("nearfield" + getTextViewText(R.id.nearLimitTextView), "4m", getTextViewText(R.id.nearLimitTextView));
    assertEquals("farfield", "6.6m", getTextViewText(R.id.farLimitTextView));
    assertEquals("hyperfocal", "20.9m", getTextViewText(R.id.hyperfocalTextView));
  }

  public void testIndexOf() {
    assertTrue(indexOf(CameraSettingsRepository.focalLengths, new Length(35)) > 1);
    assertTrue(indexOf(CirclesOfConfusion.values(), null) < 0);
  }

}
