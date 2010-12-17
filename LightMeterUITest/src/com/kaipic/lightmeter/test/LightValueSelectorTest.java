package com.kaipic.lightmeter.test;

import android.widget.RadioButton;
import com.kaipic.lightmeter.LightValueSelector;
import com.kaipic.lightmeter.lib.CameraSettingsRepository;
import com.kaipic.lightmeter.lib.ExposureValue;
import com.kaipic.lightmeter.lib.LightScenarioCategory;
import org.junit.Test;

public class LightValueSelectorTest extends AbstractMainWindowTestCase {

  LightValueSelector lightValueSelector;

  LightScenarioCategory category1;
  LightScenarioCategory category2;
  protected void setUp() throws Exception {

    category1 = new LightScenarioCategory("TestCategory1");
    category2 = new LightScenarioCategory("TestCategory2");
    category1.addScenario("testScenarioWithSingleExposure", 15);
    category1.addScenario("testScenarioWithMultipleExposure", 16, 17);
    category2.addScenario("testScenarioInCategory2_1", 1);
    category2.addScenario("testScenarioInCategory2_2", 2);
    category2.addScenario("testScenarioInCategory2_3", 3);
    CameraSettingsRepository.lightScenarioCategories = new LightScenarioCategory[]{category1, category2};
    super.setUp();
    lightValueSelector = mActivity.getLightValueSelector();

  }


  @Test
  public void testSetExposureValueShouldResultInSuchExposureValueInLightMeter() {
    ExposureValue ev = new ExposureValue(3);
    setExposureValueSpinnerTo(ev);
    assertEquals(ev, mActivity.getWorkMode().getExposure());
  }

  @Test
  public void testSelectCategoryShouldPopulateScenarioSpinner() {
    showDialog();
    setSpinnerSelection(lightValueSelector.getCategorySpinner(), 1);
    assertEquals(3, lightValueSelector.getScenarioSpinner().getAdapter().getCount());
  }

  @Test
  public void testSelectScenarioSpinnerShouldPopulateExposureRadioGroup() {
    showDialog();
    setSpinnerSelection(lightValueSelector.getScenarioSpinner(), 1);
    assertTrue(lightValueSelector.getLightValueRadioGroup().isShown());
    setSpinnerSelection(lightValueSelector.getScenarioSpinner(), 0);
    assertFalse(lightValueSelector.getLightValueRadioGroup().isShown());
  }

  @Test
  public void testSelectScenarioWithMultipleLightValuesShouldPopulateExposureRadioGroup() {
    showDialog();
    setSpinnerSelection(lightValueSelector.getScenarioSpinner(), 1);
    assertEquals(2, lightValueSelector.getLightValueRadioGroup().getChildCount());
    assertEquals("EV 16", ((RadioButton) lightValueSelector.getLightValueRadioGroup().getChildAt(0)).getText());
  }

  @Test
  public void testSelectLightValueExposureRadioGroup() {
    showDialog();
    setSpinnerSelection(lightValueSelector.getScenarioSpinner(), 1);
    click(lightValueSelector.getLightValueRadioGroup().getChildAt(0));
    assertEquals(new ExposureValue(16), lightValueSelector.getParentExposureSpinner().getSelectedItem());
  }


  @Test
  public void testSelectScenarioWithSingleLightValueShouldSetLightValueSpinner(){
    showDialog();
    setSpinnerSelection(lightValueSelector.getCategorySpinner(), 1);
    setSpinnerSelection(lightValueSelector.getScenarioSpinner(), 1);
    assertEquals(new ExposureValue(2), lightValueSelector.getParentExposureSpinner().getSelectedItem());
  }

  private void showDialog() {
    click(lightValueSelector.getSelectLightValueFromScenarioButton());
  }


}
