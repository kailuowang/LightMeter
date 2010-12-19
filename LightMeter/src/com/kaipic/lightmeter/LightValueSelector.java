package com.kaipic.lightmeter;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.*;
import com.kaipic.lightmeter.lib.CameraSettingsRepository;
import com.kaipic.lightmeter.lib.ExposureValue;
import com.kaipic.lightmeter.lib.LightScenario;
import com.kaipic.lightmeter.lib.LightScenarioCategory;

import java.util.Arrays;

public class LightValueSelector {

  private final MainWindow parentWindow;
  private final SpinnerHelper spinnerHelper;
  private final Dialog dialog;
  private Spinner categorySpinner;
  private Spinner scenarioSpinner;
  private RadioGroup lightValueRadioGroup;

  private TextView lightValueSelectText;
  private Button selectLightValueFromScenarioButton;

  private Button selectLightValueCancelButton;
  private Spinner parentExposureSpinner;
  private LightScenarioCategory currentCategory;
  private LightScenario currentScenario;
  private LightScenarioCategory[] lightScenarioCategories;

  public Spinner getParentExposureSpinner() {
    return parentExposureSpinner;
  }

  public RadioGroup getLightValueRadioGroup() {
    return lightValueRadioGroup;
  }

  public Spinner getCategorySpinner() {
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
    lightScenarioCategories = CameraSettingsRepository.lightScenarioCategories;
    initializeSubViews();
  }



  public Dialog getDialog() {
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

  public String getLightValueString() {
    return getSelectedLightValue().toString();
  }

  public ExposureValue getSelectedLightValue() {
    return CameraSettingsRepository.exposureValues[parentExposureSpinner.getSelectedItemPosition()];
  }

  public void databind() {
    spinnerHelper.setupSpinner(parentExposureSpinner, lightValueItems());
    spinnerHelper.setupSpinner(categorySpinner, lightScenarioCategories);
    currentCategory = lightScenarioCategories[0];
    setupScenarioSpinner();
    setupLightValueRadioGroup(0);
    spinnerHelper.registerSpinnerListenner(categorySpinner, new SpinnerItemSelectListener() {
      public void onSpinnerItemSelected(Object selectedValue, int position) {
        currentCategory = lightScenarioCategories[position];
        setupScenarioSpinner();
      }
    });

    spinnerHelper.registerSpinnerListenner(scenarioSpinner, new SpinnerItemSelectListener() {
      public void onSpinnerItemSelected(Object selectedValue, int position) {
        setupLightValueRadioGroup(position);
      }
    });
  }

  private String[] lightValueItems() {
    String[] lightValueItems = new String[CameraSettingsRepository.exposureValues.length];
    for (int i = 0; i < CameraSettingsRepository.exposureValues.length; i++) {
      String itemString = CameraSettingsRepository.exposureValues[i].toDetailString();
      if(itemString.length() > 39) {
        itemString = itemString.substring(0, 37) + "..";
      }
      lightValueItems[i] = itemString;
    }
    return lightValueItems;
  }

  private void setupLightValueRadioGroup(int position) {
    currentScenario = currentCategory.getScenarios().get(position);
    boolean multipleExposureValues = currentScenario.getLightValues().size() > 1;
    parentWindow.setVisible(lightValueRadioGroup, multipleExposureValues);
    parentWindow.setVisible(lightValueSelectText, multipleExposureValues);

    if (multipleExposureValues) {
      LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
        RadioGroup.LayoutParams.WRAP_CONTENT,
        RadioGroup.LayoutParams.WRAP_CONTENT);
      lightValueRadioGroup.removeAllViews();
      for (final ExposureValue lightValue : currentScenario.getLightValues()) {
        RadioButton newRadioButton = new RadioButton(parentWindow);
        newRadioButton.setText(lightValue.toString());
        newRadioButton.setOnClickListener(new View.OnClickListener(){
          public void onClick(View view) {
           lightValueSelected(new ExposureValue(lightValue.toString()));
          }
        });

        lightValueRadioGroup.addView(newRadioButton, lightValueRadioGroup.getChildCount(), layoutParams);
      }
    }else {
      lightValueSelected(currentScenario.getLightValues().get(0));
    }

  }

  private void lightValueSelected(ExposureValue lightValue) {
    int position;
    position = Arrays.asList(CameraSettingsRepository.exposureValues).indexOf(lightValue);
    parentExposureSpinner.setSelection(position);
    dialog.hide();
    parentWindow.updateSettings();
  }

  private void setupScenarioSpinner() {
    spinnerHelper.setupSpinner(scenarioSpinner, currentCategory.getScenarios().toArray());
  }

  private void initializeSubViews() {
    categorySpinner = (Spinner) dialog.findViewById(R.id.categorySpinner);
    scenarioSpinner = (Spinner) dialog.findViewById(R.id.scenarioSpinner);
    lightValueSelectText = (TextView) dialog.findViewById(R.id.lightValueSelectText);
    parentExposureSpinner = (Spinner) parentWindow.findViewById(R.id.exposureSpinner);
    lightValueRadioGroup = (RadioGroup) dialog.findViewById(R.id.lightValueRadioGroup);
    selectLightValueFromScenarioButton = (Button) parentWindow.findViewById(R.id.selectLightValueFromScenarioButton);
    selectLightValueCancelButton = (Button) dialog.findViewById(R.id.select_light_value_cancel_button);
  }

  public void saveSettings(SharedPreferences.Editor editor) {
    spinnerHelper.saveSpinnerSetting(editor, categorySpinner);
    spinnerHelper.saveSpinnerSetting(editor, scenarioSpinner);

  }

}
