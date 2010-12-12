package com.kaipic.lightmeter.test;

import com.kaipic.lightmeter.LightValueSelector;
import com.kaipic.lightmeter.lib.CameraSettingsRepository;
import com.kaipic.lightmeter.lib.ExposureValue;
import org.junit.Test;

public class LightValueSelectorTest extends AbstractMainWindowTestCase {

  LightValueSelector lightValueSelector;
  protected void setUp() throws Exception {
    super.setUp();
    lightValueSelector = mActivity.getLightValueSelector();
  }
  @Test
  public void testExposureValueSpinnerItems() {
    String[] items = lightValueSelector.exposureValueSpinnerItems();
    assertEquals(CameraSettingsRepository.exposureValues.length, items.length);
  }

  @Test
  public void testSetExposureValueShouldResultInSuchExposureValueInLightMeter() {
    ExposureValue ev = new ExposureValue(3);
    setExposureValueSpinnerTo(ev);
    assertEquals(ev, mActivity.getWorkMode().getExposure());
  }
}
