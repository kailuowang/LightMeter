package com.kaipic.lightmeter;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import com.kaipic.lightmeter.lib.CameraSettingsRepository;
import com.kaipic.lightmeter.lib.ExposureValue;
import com.kaipic.lightmeter.lib.LightScenario;
import com.kaipic.lightmeter.lib.LightScenarioCategory;

import java.util.ArrayList;
import java.util.List;

public class LightValueSelector {

  private final MainWindow parentWindow;
  private final SpinnerHelper spinnerHelper;
  private final Dialog dialog;
  private Spinner categorySpinner;
  private Spinner scenarioSpinner;
  private Spinner lightValueSpinner;
  private RadioGroup lightValueRadioGroup;

  private Button selectLightValueFromScenarioButton;

  private Button selectLightValueCancelButton;
  private Spinner parentExposureSpinner;
  private LightScenarioCategory currentCategory;
  private LightScenario currentScenario;

  public Spinner getParentExposureSpinner() {
    return parentExposureSpinner;
  }

  public RadioGroup getLightValueRadioGroup() {
    return lightValueRadioGroup;
  }

  public Spinner getCategorySpinner(){
    return categorySpinner;
  }

  public Spinner getScenarioSpinner() {
    return scenarioSpinner;
  }

  public Button getSelectLightValueFromScenarioButton() {
    return selectLightValueFromScenarioButton;
  }

  public LightValueSelector(MainWindow parentWindow, SpinnerHelper spinnerHelper) {
    this.parentWindow = parentWindow;
    this.spinnerHelper = spinnerHelper;
    dialog = new Dialog(parentWindow);
    dialog.setContentView(R.layout.light_value_selector_dialog);
    dialog.setTitle("Select Light Value From Scenarios");

    initializeSubViews();
    databindSpinners();

  }

  private void databindSpinners() {
    spinnerHelper.setupSpinner(parentExposureSpinner, exposureValueSpinnerItems());
    spinnerHelper.setupSpinner(categorySpinner, CameraSettingsRepository.lightScenarioCategories);
    currentCategory = CameraSettingsRepository.lightScenarioCategories[0];
    setupScenarioSpinner();
    setupLightValueRadioGroup(0);
    spinnerHelper.registerSpinnerListenner(categorySpinner, new SpinnerItemSelectListener() {
      public void onSpinnerItemSelected(Object selectedValue, int position) {
        currentCategory = CameraSettingsRepository.lightScenarioCategories[position];
        setupScenarioSpinner();
      }
    });

    spinnerHelper.registerSpinnerListenner(scenarioSpinner, new SpinnerItemSelectListener() {
      public void onSpinnerItemSelected(Object selectedValue, int position) {
        setupLightValueRadioGroup(position);
      }
    });
  }

  private void setupLightValueRadioGroup(int position) {
    currentScenario = currentCategory.getScenarios().get(position);
    parentWindow.setVisible(lightValueRadioGroup, currentScenario.getLightValues().size() > 1);
  }

  private void setupScenarioSpinner() {
    spinnerHelper.setupSpinner(scenarioSpinner, currentCategory.getScenarios().toArray());
  }

  private void initializeSubViews() {
    categorySpinner = (Spinner) dialog.findViewById(R.id.categorySpinner);
    scenarioSpinner = (Spinner) dialog.findViewById(R.id.scenarioSpinner);
    parentExposureSpinner = (Spinner) parentWindow.findViewById(R.id.exposureSpinner);
    lightValueRadioGroup = (RadioGroup) dialog.findViewById(R.id.lightValueRadioGroup);
    selectLightValueFromScenarioButton = (Button) parentWindow.findViewById(R.id.selectLightValueFromScenarioButton);
    selectLightValueCancelButton = (Button) dialog.findViewById(R.id.select_light_value_cancel_button);
  }


  protected Dialog getDialog() {
    return dialog;
  }

  public void registerEvents() {
    selectLightValueFromScenarioButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        parentWindow.showDialog(R.layout.light_value_selector_dialog);
      }
    });
   selectLightValueCancelButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        dialog.hide();
      }
    });
  }

  public String[] exposureValueSpinnerItems() {
    List<String> items = new ArrayList<String>();
    for (ExposureValue exposureValue : CameraSettingsRepository.exposureValues) {
      items.add(exposureValue.toString());
    }
    return items.toArray(new String[0]);
  }

  public String getLightValueString() {
    int position = parentExposureSpinner.getSelectedItemPosition();
    return new Float(CameraSettingsRepository.exposureValues[position].getValue()).toString();
  }

}
