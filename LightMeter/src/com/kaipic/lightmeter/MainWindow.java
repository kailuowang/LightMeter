package com.kaipic.lightmeter;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.kaipic.lightmeter.lib.*;

import java.util.ArrayList;
import java.util.List;

public class MainWindow extends Activity implements LightMeterListener {
  private LightMeter lightMeter;
  private DoFCalculator doFCalculator;
  private WorkMode workMode = null;
  private SpinnerHelper spinnerHelper;

  private Button pauseButton;
  private TextView shutterSpeedTextView;
  private Spinner apertureSpinner;
  private Spinner shutterSpeedSpinner;
  private Spinner exposureSpinner;
  private Spinner isoSpinner;
  private Spinner focalLengthSpinner;
  private EditText subjectDistanceEditText;
  private TextView exposureValueTextView;
  private TextView statusTextView;
  private static final String PREFS_NAME = "LIGHT_METER_PREFS";
  private static final String LIGHT_SENSOR_CALIBRATION = "LightSensorCalibration";
  private static final boolean DEBUG_ENABLED = true;
  private RadioButton radioAv;
  private RadioButton radioM;
  private RadioButton radioSv;
  private TextView apertureTextView;
  private RadioButton radioManualExposure;
  private RadioButton radioAutoExposure;
  private Spinner circlesOfConfusionSpinner;
  private Spinner lengthUnitSpinner;
  private TextView nearLimitTextView;
  private TextView farLimitTextView;
  private TextView hyperfocalTextView;

  public LightMeter getLightMeter() {
    return lightMeter;
  }

  public DoFCalculator getDoFCalculator() {
    return doFCalculator;
  }

  public WorkMode getWorkMode() {
    return workMode;
  }

  public SpinnerHelper getSpinnerHelper() {
    return spinnerHelper;
  }


  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    initialize();
    updateSettings();
    registerEvents();
  }

  private void initialize() {
    spinnerHelper = new SpinnerHelper(this, getSettings());
    initializeSubViewFields();
    initializeLightMeter();
    initializeSpinners();
    initializeWorkModeRadioGroup();
    initializeExposureSettingRadioGroup();
  }

  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.option_menu, menu);
    return true;
  }

  public void onLightMeterChange() {
    updateSettings();
  }

  private void registerEvents() {
    registerLockButtonListener();

    registerSubjectDistanceChangeEvent();
    registerUpdateSettingListenerForSpinners();
  }

  private void registerLockButtonListener() {
    pauseButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        toggleLock();
        display();
      }
    });
  }

  private void registerUpdateSettingListenerForSpinners() {
    SpinnerItemSelectListener displayListener = new SpinnerItemSelectListener() {
      public void onSpinnerItemSelected(Object selectedValue, int position) {
        updateSettings();
      }
    };
    spinnerHelper.registerSpinnerListenner(exposureSpinner, displayListener);
    spinnerHelper.registerSpinnerListenner(isoSpinner, displayListener);
    spinnerHelper.registerSpinnerListenner(apertureSpinner, displayListener);
    spinnerHelper.registerSpinnerListenner(shutterSpeedSpinner, displayListener);
    spinnerHelper.registerSpinnerListenner(circlesOfConfusionSpinner, displayListener);
    spinnerHelper.registerSpinnerListenner(focalLengthSpinner, displayListener);
    spinnerHelper.registerSpinnerListenner(lengthUnitSpinner, displayListener);
  }

  private void registerSubjectDistanceChangeEvent() {
    subjectDistanceEditText.addTextChangedListener(new TextWatcher() {
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      public void afterTextChanged(Editable editable) {
        updateSettings();
      }
    });
  }

  public void display() {
    exposureValueTextView.setText(workMode.getExposure().toDetailString());
    shutterSpeedTextView.setText(workMode.getShutterSpeed().toString());
    apertureTextView.setText(workMode.getAperture().toString());
    statusTextView.setText("Status: " + lightMeter.getStatus());

    boolean usingAutoExposureSetting = lightMeter.usingAutoLightSensor() && workMode.isExposureValueChangeable();
    setVisible(pauseButton, usingAutoExposureSetting);
    setVisible(apertureSpinner, workMode.isApertureChangeable());
    setVisible(apertureTextView, !workMode.isApertureChangeable());
    setVisible(shutterSpeedSpinner, workMode.isShutterSpeedChangeable());
    setVisible(shutterSpeedTextView, !workMode.isShutterSpeedChangeable());
    setVisible(exposureSpinner, !lightMeter.usingAutoLightSensor() && workMode.isExposureValueChangeable());
    setVisible(exposureValueTextView, lightMeter.usingAutoLightSensor() || !workMode.isExposureValueChangeable());
    setVisible(R.id.exposureSettingRadioGroup, workMode.isExposureValueChangeable());
    setVisible(R.id.depthOfFieldTitleTextView, !doFCalculator.isValid());
    setVisible(R.id.depthOfFieldResultTable, doFCalculator.isValid());
    LengthUnit unit = getSelectedLengthUnit();
    hyperfocalTextView.setText(doFCalculator.hyperFocalDistance().toString(unit));
    if (doFCalculator.isValid()) {
      nearLimitTextView.setText(doFCalculator.nearLimit().toString(unit));
      farLimitTextView.setText(doFCalculator.farLimit().toString(unit));
    }
  }

  private void initializeSubViewFields() {
    pauseButton = (Button) findViewById(R.id.pause_button);
    exposureValueTextView = (TextView) findViewById(R.id.exposureValue);
    shutterSpeedTextView = (TextView) findViewById(R.id.shutterSpeed);
    apertureTextView = (TextView) findViewById(R.id.aperture);
    nearLimitTextView = (TextView) findViewById(R.id.nearLimitTextView);
    farLimitTextView = (TextView) findViewById(R.id.farLimitTextView);
    hyperfocalTextView = (TextView) findViewById(R.id.hyperfocalTextView);
    subjectDistanceEditText = (EditText) findViewById(R.id.subjectDistanceEditText);
    statusTextView = (TextView) findViewById(R.id.status_text_view);
    apertureSpinner = (Spinner) findViewById(R.id.apertureSpinner);
    isoSpinner = (Spinner) findViewById(R.id.isoSpinner);
    shutterSpeedSpinner = (Spinner) findViewById(R.id.shutterSpeedSpinner);
    focalLengthSpinner = (Spinner) findViewById(R.id.focalLengthSpinner);
    circlesOfConfusionSpinner = (Spinner) findViewById(R.id.circlesOfConfusionSpinner);
    exposureSpinner = (Spinner) findViewById(R.id.exposureSpinner);
    lengthUnitSpinner = (Spinner) findViewById(R.id.lengthUnitSpinner);
    radioAv = (RadioButton) findViewById(R.id.radio_Av);
    radioSv = (RadioButton) findViewById(R.id.radio_Sv);
    radioM = (RadioButton) findViewById(R.id.radio_Manual);
    radioManualExposure = (RadioButton) findViewById(R.id.radioManualExposure);
    radioAutoExposure = (RadioButton) findViewById(R.id.radioAutoExposure);
  }

  private void initializeSpinners() {
    spinnerHelper.setupSpinner(isoSpinner, CameraSettingsRepository.isos);
    spinnerHelper.setupSpinner(apertureSpinner, CameraSettingsRepository.apertures);
    spinnerHelper.setupSpinner(exposureSpinner, exposureValueSpinnerItems());
    spinnerHelper.setupSpinner(focalLengthSpinner, CameraSettingsRepository.focalLengths, CameraSettingsRepository.defaultFocalLength);
    spinnerHelper.setupSpinner(shutterSpeedSpinner, CameraSettingsRepository.shutterSpeeds);
    spinnerHelper.setupSpinner(circlesOfConfusionSpinner, CirclesOfConfusion.values(), CirclesOfConfusion.defaultCirclesOfConfusion);
    spinnerHelper.setupSpinner(lengthUnitSpinner, LengthUnit.selectableUnits());
  }

  public String[] exposureValueSpinnerItems() {
    List<String> items = new ArrayList<String>();
    for (ExposureValue exposureValue : CameraSettingsRepository.exposureValues) {
      items.add(exposureValue.toDetailString());
    }
    return items.toArray(new String[0]);
  }

  private void initializeWorkModeRadioGroup() {
    radioAv.setOnClickListener(createWorkModeListener(new AvMode(lightMeter)));
    radioSv.setOnClickListener(createWorkModeListener(new SvMode(lightMeter)));
    radioM.setOnClickListener(createWorkModeListener(new ManualMode(lightMeter)));
  }

  private View.OnClickListener createWorkModeListener(final WorkMode workMode) {
    return new View.OnClickListener() {
      public void onClick(View view) {
        MainWindow.this.workMode = workMode;
        updateSettings();
      }
    };
  }

  private void initializeExposureSettingRadioGroup() {
    View.OnClickListener onExposureSettingChange = new View.OnClickListener() {
      public void onClick(View view) {
        updateSettings();
      }
    };
    radioManualExposure.setOnClickListener(onExposureSettingChange);
    radioAutoExposure.setOnClickListener(onExposureSettingChange);
  }


  private void initializeLightMeter() {
    lightMeter = new LightMeter(new LightSensorRepo(getLightSensorFactory()));
    lightMeter.setLightSensor(LightSensorType.AUTO.toString());
    lightMeter.setCalibration(getSettings().getFloat(LIGHT_SENSOR_CALIBRATION, lightMeter.getCalibration()));
    lightMeter.subscribe(this);
    lightMeter.start();
    workMode = new AvMode(lightMeter);
    doFCalculator = new DoFCalculator();
  }

  private LightSensorFactory getLightSensorFactory() {
    return DEBUG_ENABLED ? new TestLightSensorFactory(getApplicationContext()) : new LightSensorFactory(getApplicationContext());
  }

  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.about:
        showDialog(R.id.about);
        return true;
      case R.id.help:
        showDialog(R.id.help);
        return true;
      case R.id.calibrate:
        showDialog(R.id.calibrate);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  protected Dialog onCreateDialog(int id) {
    return new MainWindowDialogFactory(this).create(id);
  }

  public void updateSettings() {
    workMode.setAperture((Aperture) apertureSpinner.getSelectedItem());
    lightMeter.setISO((Iso) isoSpinner.getSelectedItem());
    workMode.setShutterSpeed((ShutterSpeed) shutterSpeedSpinner.getSelectedItem());
    lightMeter.setLightSensor(lightSensorString());
    doFCalculator.setFocalLength((Length) focalLengthSpinner.getSelectedItem());
    doFCalculator.setCircleOfConfusion((CirclesOfConfusion) circlesOfConfusionSpinner.getSelectedItem());
    doFCalculator.setAperture(workMode.getAperture());
    setSubjectDistance();
    display();
  }

  private void setSubjectDistance() {
    String distanceString = subjectDistanceEditText.getText().toString();
    if (distanceString.length() == 0)
      return;
    doFCalculator.setSubjectDistance(Length.from(distanceString, getSelectedLengthUnit()));
  }

  private LengthUnit getSelectedLengthUnit() {
    return (LengthUnit) lengthUnitSpinner.getSelectedItem();
  }

  private String lightSensorString() {
    if (radioAutoExposure.isChecked())
      return "AUTO";
    return ((Integer) (exposureSpinner.getSelectedItemPosition())).toString();
  }

  
  public SharedPreferences getSettings() {
    return getSharedPreferences(PREFS_NAME, 0);
  }

  private void toggleLock() {
    lightMeter.togglePause();
    boolean locked = lightMeter.isPaused();
    int resId = locked ? R.string.continue_btn : R.string.pause;
    pauseButton.setText(getString(resId));
  }

  protected void onResume() {
    super.onResume();
    lightMeter.start();
  }

  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    lightMeter.start();
  }

  protected void onPause() {
    lightMeter.stop();
    super.onPause();
  }

  protected void onStop() {
    super.onStop();
    saveSettings();
  }

  protected void onDestroy() {
    lightMeter.stop();
    super.onDestroy();
  }

  public void saveSettings() {
    SharedPreferences.Editor editor = getSettings().edit();
    spinnerHelper.saveSpinnerSetting(editor, isoSpinner);
    spinnerHelper.saveSpinnerSetting(editor, exposureSpinner);
    spinnerHelper.saveSpinnerSetting(editor, apertureSpinner);
    spinnerHelper.saveSpinnerSetting(editor, focalLengthSpinner);
    spinnerHelper.saveSpinnerSetting(editor, shutterSpeedSpinner);
    spinnerHelper.saveSpinnerSetting(editor, circlesOfConfusionSpinner);
    spinnerHelper.saveSpinnerSetting(editor, lengthUnitSpinner);
    editor.putFloat(LIGHT_SENSOR_CALIBRATION, lightMeter.getCalibration());
    editor.commit();
  }

  public void clearSettings() {
    SharedPreferences.Editor editor = getSettings().edit();
    editor.clear();
    editor.commit();
  }

  private void setVisible(final int viewId, final boolean visible) {
    setVisible(findViewById(viewId), visible);
  }

  private void setVisible(View view, final boolean visible) {
    view.setVisibility(visible ? View.VISIBLE : View.GONE);
  }

}