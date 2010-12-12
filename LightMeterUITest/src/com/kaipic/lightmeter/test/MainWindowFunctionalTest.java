package com.kaipic.lightmeter.test;

import android.widget.TextView;
import com.kaipic.lightmeter.R;
import com.kaipic.lightmeter.lib.*;

import static com.kaipic.lightmeter.lib.Util.indexOf;

public class MainWindowFunctionalTest extends AbstractMainWindowTestCase {

  public void testCreateActivity() {
    assertNotNull(mActivity);
    assertNotNull(mLockButton);
    assertNotNull(mExposureValueView);
  }

  public void testDisplayInAvModeShouldDisplayLightMeterResults() {
    switchToAv();
    switchToManualExposure();
    LightMeter lightMeter = mActivity.getLightMeter();
    lightMeter.setAperture(3.5f).setCalibration(250).setISO(new Iso(100));
    ExposureValue exposureValue = new ExposureValue(9);
    setExposureValueSpinnerTo(exposureValue);
    assertEquals("ExposureString",  exposureValue.toDetailString(), getTextViewText(com.kaipic.lightmeter.R.id.exposureValue));
    assertTrue(((TextView) mActivity.findViewById(R.id.shutterSpeed)).getText().length() > 0);
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
        mActivity.getSpinnerHelper().setupSpinner(mIsoSpinner, CameraSettingsRepository.isos);
        assertEquals(2, mIsoSpinner.getSelectedItemPosition());
      }
    });
  }

  public void testSetupSpinnerWithDefaultItemShouldRememberLastPosition() {
    setSpinnerSelection(mIsoSpinner, 2);
    mActivity.saveSettings();
    runOnUiThread(new Runnable() {
      public void run() {
        mActivity.getSpinnerHelper().setupSpinner(mIsoSpinner, CameraSettingsRepository.isos, new Iso(1600));
        assertEquals(2, mIsoSpinner.getSelectedItemPosition());
      }
    });
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
}
