package com.kaipic.lightmeter;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import com.kaipic.lightmeter.lib.CameraSettingsRepository;
import com.kaipic.lightmeter.lib.ExposureValue;

import java.util.ArrayList;
import java.util.List;

public class LightValueSelector {

  private final Activity parentWindow;
  private final SpinnerHelper spinnerHelper;
  private final Dialog dialog;
  private Spinner categorySpinner;
  private Spinner scenarioSpinner;
  private Spinner lightValueSpinner;
  private Button selectLightValueFromScenarioButton;
  private Button selectLightValueOkButton;
  private Button selectLightValueCancelButton;
  private Spinner parentExposureSpinner;

  public Spinner getParentExposureSpinner() {
    return parentExposureSpinner;
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

  public LightValueSelector(Activity parentWindow, SpinnerHelper spinnerHelper) {
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
    spinnerHelper.setupSpinner(scenarioSpinner, CameraSettingsRepository.lightScenarioCategories[0].getScenarios().toArray());
    spinnerHelper.registerSpinnerListenner(categorySpinner, new SpinnerItemSelectListener() {
      public void onSpinnerItemSelected(Object selectedValue, int position) {
        spinnerHelper.setupSpinner(scenarioSpinner, CameraSettingsRepository.lightScenarioCategories[position].getScenarios().toArray());
      }
    });
  }

  private void initializeSubViews() {
    categorySpinner = (Spinner) dialog.findViewById(R.id.categorySpinner);
    scenarioSpinner = (Spinner) dialog.findViewById(R.id.scenarioSpinner);
    parentExposureSpinner = (Spinner) parentWindow.findViewById(R.id.exposureSpinner);

    selectLightValueFromScenarioButton = (Button) parentWindow.findViewById(R.id.selectLightValueFromScenarioButton);
    selectLightValueOkButton = (Button) dialog.findViewById(R.id.select_light_value_ok_button);
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
   selectLightValueOkButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        dialog.hide();
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
