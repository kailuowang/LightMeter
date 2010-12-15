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

  @Test
  public void testSelectCategoryShouldPopulateScenarioSpinner(){
    showDialog();
    setSpinnerSelection(lightValueSelector.getCategorySpinner(), 1);
    int numOfScenarios = CameraSettingsRepository.lightScenarioCategories[1].getScenarios().size();
    assertEquals(numOfScenarios, lightValueSelector.getScenarioSpinner().getAdapter().getCount());
  }

  @Test
  public void testSelectScenarioSpinnerShouldPopulateExposureRadioGroup(){
    showDialog();
    assertTrue(lightValueSelector.getLightValueRadioGroup().isShown());
    setSpinnerSelection(lightValueSelector.getScenarioSpinner(), 2);
    assertFalse(lightValueSelector.getLightValueRadioGroup().isShown());
    setSpinnerSelection(lightValueSelector.getScenarioSpinner(), 0);
    assertTrue(lightValueSelector.getLightValueRadioGroup().isShown());
  }

  private void showDialog() {
    click(lightValueSelector.getSelectLightValueFromScenarioButton());
  }


}
