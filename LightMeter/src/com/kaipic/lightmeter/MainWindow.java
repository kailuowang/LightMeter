package com.kaipic.lightmeter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import com.kaipic.lightmeter.lib.*;

public class MainWindow extends Activity implements LightMeterListener {
  private LightMeter lightMeter;
  private Button pauseButton;
  private TextView shutterSpeedTextView;
  private Spinner apertureSpinner;
  private Spinner shutterSpeedSpinner;
  private Spinner exposureSpinner;
  private Spinner isoSpinner;
  private TextView exposureValueTextView;
  private TextView statusTextView;
  private static final String PREFS_NAME = "LIGHT_METER_PREFS";
  private static final String LIGHT_SENSOR_CALIBRATION = "LightSensorCalibration";
  private static final boolean DEBUG = false;
  private WorkMode workMode = null;
  private RadioButton radioAv;
  private RadioButton radioM;
  private RadioButton radioSv;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    initializeFields();
    updateLightMeterSettings();
    registerEvents();
    display();
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

  public void onLightMeterChange() {
    display();
  }

  public void display() {
    exposureValueTextView.setText(workMode.getExposure().toString());
    shutterSpeedTextView.setText(workMode.getShutterSpeed().toString());
    statusTextView.setText("Status: " + lightMeter.getStatus());
    boolean usingManualExposureSetting = !lightMeter.usingAutoLightSensor() || !workMode.isExposureValueChangeable();
    pauseButton.setVisibility(usingManualExposureSetting ? View.INVISIBLE : View.VISIBLE);
    findViewById(R.id.apertureSpinnerRow).setVisibility(workMode.isApertureChangeable() ? View.VISIBLE : View.GONE);
    findViewById(R.id.shutterSpeedSpinnerRow).setVisibility(workMode.isShutterSpeedChangeable() ? View.VISIBLE : View.GONE);
    findViewById(R.id.shutterSpeedResultRow).setVisibility(workMode.isShutterSpeedChangeable() ? View.GONE : View.VISIBLE);
    int exposureSpinnerVisibility = workMode.isExposureValueChangeable() ? View.VISIBLE : View.GONE;
    findViewById(R.id.exposureSpinnerRow).setVisibility(exposureSpinnerVisibility);
    findViewById(R.id.exposureSpinnerTitleRow).setVisibility(exposureSpinnerVisibility);
    findViewById(R.id.exposureDisplayRow).setVisibility(lightMeter.usingAutoLightSensor() || !workMode.isExposureValueChangeable() ? View.VISIBLE : View.GONE);
  }

  private void initializeFields() {
    pauseButton = (Button) findViewById(R.id.pause_button);
    exposureValueTextView = (TextView) findViewById(R.id.exposureValue);
    shutterSpeedTextView = (TextView) findViewById(R.id.shutterSpeed);
    statusTextView = (TextView) findViewById(R.id.status_text_view);
    initializeLightMeter();
    apertureSpinner = (Spinner) findViewById(R.id.apertureSpinner);
    isoSpinner = (Spinner) findViewById(R.id.isoSpinner);
    shutterSpeedSpinner = (Spinner) findViewById(R.id.shutterSpeedSpinner);
    exposureSpinner = (Spinner) findViewById(R.id.exposureSpinner);
    setupSpinner(isoSpinner, R.array.isos);
    setupSpinner(apertureSpinner, R.array.appertures);
    setupSpinner(exposureSpinner, R.array.exposureValues);
    setupSpinner(shutterSpeedSpinner, R.array.shutterSpeeds);

    radioAv = (RadioButton) findViewById(R.id.radio_Av);
    radioAv.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        changeToAvMode();
      }
    });
    radioSv = (RadioButton) findViewById(R.id.radio_Sv);
    radioSv.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        changeToSvMode();
      }
    });
    radioM = (RadioButton) findViewById(R.id.radio_Manual);
    radioM.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        changeToMMode();
      }
    });

  }

  private void changeToMMode() {
    workMode = new ManualMode(lightMeter);
    display();
  }

  private void changeToAvMode() {
    workMode = new AvMode(lightMeter);
    display();
  }

  private void changeToSvMode() {
    workMode = new SvMode(lightMeter);
    display();
  }

  private void initializeLightMeter() {
    lightMeter = new LightMeter(new LightSensorRepo(getLightSensorFactory()));
    lightMeter.setLightSensor(LightSensorType.AUTO.toString());
    lightMeter.setCalibration(getSettings().getFloat(LIGHT_SENSOR_CALIBRATION, lightMeter.getCalibration()));
    lightMeter.subscribe(this);
    lightMeter.start();
    workMode = new AvMode(lightMeter);
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

  public void updateLightMeterSettings() {
    setAperture((String) apertureSpinner.getSelectedItem());
    lightMeter.setISO(Integer.parseInt((String) isoSpinner.getSelectedItem()));
    lightMeter.setShutterSpeed(new ShutterSpeed((String) shutterSpeedSpinner.getSelectedItem()));
    lightMeter.setLightSensor(((Integer) exposureSpinner.getSelectedItemPosition()).toString());

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

  public void setupSpinner(Spinner spinner, int itemArray) {
    ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(
        this, itemArray, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
    int savedSelection = getSettings().getInt(spinnerPreferenceKey(spinner), 0);
    if (savedSelection < adapter.getCount()) {
      spinner.setSelection(savedSelection, false);
    }
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

    SpinnerItemSelectListenner listener = new SpinnerItemSelectListenner() {
      public void onSpinnerItemSelected(Object selectedValue, int position) {
        updateLightMeterSettings();
        display();
      }
    };
    registerSpinnerListenner(exposureSpinner, listener);
    registerSpinnerListenner(isoSpinner, listener);
    registerSpinnerListenner(apertureSpinner, listener);
    registerSpinnerListenner(shutterSpeedSpinner, listener);
  }

  private void registerSpinnerListenner(Spinner spinner, final SpinnerItemSelectListenner listener) {
    spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      public void onItemSelected(AdapterView<?> arg0, View arg1,
                                 int arg2, long arg3) {
        Object selectedItem = arg0.getItemAtPosition(arg2);
        listener.onSpinnerItemSelected(selectedItem, arg2);
      }

      public void onNothingSelected(AdapterView<?> arg0) {
      }
    });
  }

  public void setAperture(String aperture) {
    lightMeter.setAperture(Aperture.fromString(aperture));
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
    editor.putFloat(LIGHT_SENSOR_CALIBRATION, lightMeter.getCalibration());
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