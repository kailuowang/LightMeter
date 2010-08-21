package com.kaipic.lightmeter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import com.kaipic.lightmeter.lib.*;

import static com.kaipic.lightmeter.lib.Util.indexOf;

public class MainWindow extends Activity implements LightMeterListener {
  private LightMeter lightMeter;
  private DoFCalculator doFCalculator;
  private WorkMode workMode = null;

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
  private static final boolean DEBUG = true;
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

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    initialize();
    updateSettings();
    registerEvents();
  }

  private void initialize() {
    initializeFields();
    initializeLightMeter();
    initializeSpinners();
    initializeWorkMode();
    initializeExposureSettings();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.option_menu, menu);
    return true;
  }

  public LightMeter getLightMeter() {
    return lightMeter;
  }

  public DoFCalculator getDoFCalculator() {
    return doFCalculator;
  }

  public void onLightMeterChange() {
    display();
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

  private void setVisible(final int viewId, final boolean visible) {
    setVisible(findViewById(viewId), visible);
  }

  private void setVisible(View view, final boolean visible) {
    view.setVisibility(visible ? View.VISIBLE : View.GONE);
  }


  private void initializeFields() {
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
  }

  private void initializeSpinners() {
    setupSpinner(isoSpinner, CameraSettingsRepository.isos);
    setupSpinner(apertureSpinner, CameraSettingsRepository.apertures);
    setupSpinner(exposureSpinner, exposureSpinnerItems());
    setupSpinner(focalLengthSpinner, CameraSettingsRepository.focalLengths, CameraSettingsRepository.defaultFocalLength);
    setupSpinner(shutterSpeedSpinner, CameraSettingsRepository.shutterSpeeds);
    setupSpinner(circlesOfConfusionSpinner, CirclesOfConfusion.values(), CirclesOfConfusion.defaultCirclesOfConfusion);
    setupSpinner(lengthUnitSpinner, LengthUnit.selectableUnits());
  }


  private void initializeWorkMode() {
    radioAv = (RadioButton) findViewById(R.id.radio_Av);
    radioAv.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        changeWorkMode(new AvMode(lightMeter));
      }
    });
    radioSv = (RadioButton) findViewById(R.id.radio_Sv);
    radioSv.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        changeWorkMode(new SvMode(lightMeter));
      }
    });
    radioM = (RadioButton) findViewById(R.id.radio_Manual);
    radioM.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        changeWorkMode(new ManualMode(lightMeter));
      }
    });
  }

  private void initializeExposureSettings() {
    View.OnClickListener onExposureSettingChange = new View.OnClickListener() {
      public void onClick(View view) {
        updateSettings();
      }
    };
    radioManualExposure = (RadioButton) findViewById(R.id.radioManualExposure);
    radioManualExposure.setOnClickListener(onExposureSettingChange);
    radioAutoExposure = (RadioButton) findViewById(R.id.radioAutoExposure);
    radioAutoExposure.setOnClickListener(onExposureSettingChange);
  }

  public String[] exposureSpinnerItems() {
    String[] items = new String[ExposureValue.DETAIL_STRINGS.length];
    for (int i = 0; i < items.length; i++) {
      items[i] = new ExposureValue(i).toDetailString();
    }
    return items;
  }

  private void changeWorkMode(WorkMode workMode) {
    this.workMode = workMode;
    updateSettings();
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
    return DEBUG ? new TestLightSensorFactory(getApplicationContext()) : new LightSensorFactory(getApplicationContext());
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
    Dialog dialog;
    switch (id) {
      case R.id.about:
        dialog = createAboutDialog();
        break;
      case R.id.help:
        dialog = createHelpDialog();
        break;
      case R.id.calibrate:
        dialog = createCalibrateDialog();
        break;
      default:
        dialog = null;
    }
    return dialog;
  }

  private Dialog createHelpDialog() {
    return createSimpleDialog(R.layout.help_dialog, "Help");
  }

  private Dialog createCalibrateDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("This will use the currently set manual exposure value to calibrate the auto light sensor. Please make sure the light sensor on your phone is getting the light matching that EV value right now and then you can click the calibrate button.")
      .setPositiveButton("Calibrate", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
          lightMeter.calibrate();
        }
      })
      .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
          dialog.cancel();
        }
      })
      .setNeutralButton("Reset Factory", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
          lightMeter.resetCalibration();
          display();
        }
      });
    return builder.create();
  }

  public void updateSettings() {
    workMode.setAperture((Aperture) apertureSpinner.getSelectedItem());
    lightMeter.setISO((Iso) isoSpinner.getSelectedItem());
    lightMeter.setShutterSpeed((ShutterSpeed) shutterSpeedSpinner.getSelectedItem());
    lightMeter.setLightSensor(lightSensorString());
    doFCalculator.setFocalLength((Length) focalLengthSpinner.getSelectedItem());
    doFCalculator.setCircleOfConfusion((CirclesOfConfusion) circlesOfConfusionSpinner.getSelectedItem());
    doFCalculator.setAperture(workMode.getAperture());
    setSubjectDistance();
    display();
  }

  private void setSubjectDistance() {
    String distanceString = subjectDistanceEditText.getText().toString();
    if(distanceString.length() == 0)
      return;
    doFCalculator.setSubjectDistance(Length.from(distanceString, getSelectedLengthUnit()));
  }

  private LengthUnit getSelectedLengthUnit() {
    return (LengthUnit) lengthUnitSpinner.getSelectedItem();
  }

  private String lightSensorString() {
    if (radioAutoExposure.isChecked())
      return "AUTO";
    return ((Integer) (exposureSpinner.getSelectedItemPosition() + 1)).toString();
  }

  private Dialog createAboutDialog() {
    return createSimpleDialog(R.layout.about_dialog, "About Light Meter");
  }

  private Dialog createSimpleDialog(int dialogId, String title) {
    final Dialog dialog = new Dialog(this);
    dialog.setContentView(dialogId);
    dialog.setTitle(title);
    return dialog;
  }

  private void setupSpinner(Spinner spinner, ArrayAdapter<?> adapter, int defaultSelection) {
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
    int savedSelection = getSettings().getInt(spinnerPreferenceKey(spinner), defaultSelection);
    if (savedSelection > adapter.getCount() || savedSelection < 0) {
      savedSelection = 0;
    }
    spinner.setSelection(savedSelection, false);

  }

  public void setupSpinner(final Spinner spinner, final Object[] itemArray) {
    setupSpinner(spinner, itemArray, null);
  }

  public void setupSpinner(final Spinner spinner, final Object[] itemArray, Object defaultItem) {
    setupSpinner(spinner,
      (ArrayAdapter<?>) new ArrayAdapter(this, android.R.layout.simple_spinner_item, itemArray),
      indexOf(itemArray, defaultItem));
  }

  public SharedPreferences getSettings() {
    return getSharedPreferences(PREFS_NAME, 0);
  }

  private void registerEvents() {
    pauseButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        toggleLock();
        display();
      }
    });

    subjectDistanceEditText.addTextChangedListener(new TextWatcher() {
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

      public void afterTextChanged(Editable editable) {
        updateSettings();
      }
    });

    SpinnerItemSelectListenner displayListener = new SpinnerItemSelectListenner() {
      public void onSpinnerItemSelected(Object selectedValue, int position) {
        updateSettings();
      }
    };
    registerSpinnerListenner(exposureSpinner, displayListener);
    registerSpinnerListenner(isoSpinner, displayListener);
    registerSpinnerListenner(apertureSpinner, displayListener);
    registerSpinnerListenner(shutterSpeedSpinner, displayListener);
    registerSpinnerListenner(circlesOfConfusionSpinner, displayListener);
    registerSpinnerListenner(focalLengthSpinner, displayListener);
    registerSpinnerListenner(lengthUnitSpinner, displayListener);
  }

  private void registerSpinnerListenner(Spinner spinner, final SpinnerItemSelectListenner listener) {
    spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Object selectedItem = arg0.getItemAtPosition(arg2);
        listener.onSpinnerItemSelected(selectedItem, arg2);
      }

      public void onNothingSelected(AdapterView<?> arg0) {
      }
    });
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
    saveSpinnerSetting(editor, isoSpinner);
    saveSpinnerSetting(editor, exposureSpinner);
    saveSpinnerSetting(editor, apertureSpinner);
    saveSpinnerSetting(editor, focalLengthSpinner);
    saveSpinnerSetting(editor, shutterSpeedSpinner);
    saveSpinnerSetting(editor, circlesOfConfusionSpinner);
    saveSpinnerSetting(editor, lengthUnitSpinner);
    editor.putFloat(LIGHT_SENSOR_CALIBRATION, lightMeter.getCalibration());
    editor.commit();
  }

  public void clearSettings(){
    SharedPreferences.Editor editor = getSettings().edit();
    editor.clear();
    editor.commit();
  }

  private void saveSpinnerSetting(SharedPreferences.Editor editor, final Spinner spinner) {
    editor.putInt(spinnerPreferenceKey(spinner), spinner.getSelectedItemPosition());
  }

  public String spinnerPreferenceKey(final Spinner spinner) {
    return "Spinner" + spinner.getId() + "Position";
  }

  public WorkMode getWorkMode() {
    return workMode;
  }
}