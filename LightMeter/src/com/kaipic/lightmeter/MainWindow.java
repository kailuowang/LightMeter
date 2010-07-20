package com.kaipic.lightmeter;

import android.app.Activity;
import android.app.KeyguardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import com.kaipic.lightmeter.lib.*;

public class MainWindow extends Activity implements LightMeterListener {
  private LightMeter lightMeter;
  private Button pauseButton;
  private TextView shutterSpeedTextView;
  private Spinner apertureSpinner;
  private Spinner exposureSpinner;
  private Spinner isoSpinner;
  public static boolean isTesting = false;
  private TextView exposureValueTextView;
  private TextView statusTextView;
  private LightSensorRepo lightSensorRepo;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (isTesting) disableKeyGuardForTesting();
    setContentView(R.layout.main);
    initializeFields();
    registerEvents();
    display();
  }

  public LightMeter getLightMeter() {
    return lightMeter;
  }

  public void onLightMeterChange() {
    display();
  }

  public void display() {
    exposureValueTextView.setText(lightMeter.getISO100EV().toString());
    shutterSpeedTextView.setText(lightMeter.calculateShutterSpeed().toString());
    statusTextView.setText("Status: " + lightMeter.getStatus());
    boolean usingAutoLightSensor = lightMeter.getLightSensor().getType().equals(LightSensorType.AUTO);
    pauseButton.setVisibility(usingAutoLightSensor ? View.VISIBLE : View.INVISIBLE);
  }

  private void initializeFields() {
    pauseButton = (Button) findViewById(R.id.pause_button);
    exposureValueTextView = (TextView) findViewById(R.id.exposureValue);
    shutterSpeedTextView = (TextView) findViewById(R.id.shutterSpeed);
    statusTextView = (TextView) findViewById(R.id.status_text_view);

    lightSensorRepo = new LightSensorRepo(new LightSensorFactory(this.getApplicationContext()));
    lightMeter = new LightMeter(lightSensorRepo);
    lightMeter.setLightSensor(LightSensorType.AUTO.toString());
    lightMeter.subscribe(this);
    lightMeter.start();
    apertureSpinner = (Spinner) findViewById(R.id.apertureSpinner);
    isoSpinner = (Spinner) findViewById(R.id.isoSpinner);
    exposureSpinner = (Spinner) findViewById(R.id.exposureSpinner);
    setupSpinner(isoSpinner, R.array.isos);
    setupSpinner(apertureSpinner, R.array.appertures);
    setupSpinner(exposureSpinner, R.array.exposureValues);
  }

  private void setupSpinner(Spinner spinner, int itemArray) {
    ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(
      this, itemArray, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
  }


  private void registerEvents() {
    pauseButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        toggleLock();
        display();
      }
    });

    registerSpinnerListenner(apertureSpinner, new SpinnerItemSelectListenner() {
      public void onSpinnerItemSelected(Object selectedValue, int position) {
        setAperture((String) selectedValue);
        display();
      }
    });
    registerSpinnerListenner(isoSpinner, new SpinnerItemSelectListenner() {
      public void onSpinnerItemSelected(Object selectedValue, int position) {
        lightMeter.setISO(Integer.parseInt((String) selectedValue));
        display();
      }
    });
    registerSpinnerListenner(exposureSpinner, new SpinnerItemSelectListenner() {
      public void onSpinnerItemSelected(Object selectedValue, int position) {
        lightMeter.setLightSensor(lightSensorRepo.getSensor(((Integer) position).toString()));
        display();
      }
    });
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


  private void disableKeyGuardForTesting() {
    KeyguardManager keyGuardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
    keyGuardManager.newKeyguardLock("com.kaipic.lightmeter.MainWindow").disableKeyguard();
  }

  protected void onResume() {
    super.onResume();
    lightMeter.start();
  }

  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    lightMeter.start();
  }

//    protected void onStop() {
//        lightMeter.stop();
//        super.onStop();
//    }

  protected void onPause() {
    lightMeter.stop();
    super.onPause();
  }

  protected void onDestroy() {
    lightMeter.stop();
    super.onDestroy();
  }

}