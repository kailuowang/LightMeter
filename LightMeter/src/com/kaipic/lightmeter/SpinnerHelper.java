package com.kaipic.lightmeter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import static com.kaipic.lightmeter.lib.Util.indexOf;

public class SpinnerHelper {
  private Activity activity;
  SharedPreferences settings;

  SpinnerHelper(Activity activity, SharedPreferences settings) {
    this.activity = activity;
    this.settings = settings;

  }

  void setupSpinner(Spinner spinner, ArrayAdapter<?> adapter, int defaultSelection) {
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
    int savedSelection = settings.getInt(spinnerPreferenceKey(spinner), defaultSelection);
    if (savedSelection > adapter.getCount() || savedSelection < 0) {
      savedSelection = 0;
    }
    spinner.setSelection(savedSelection, false);
  }


  String spinnerPreferenceKey(final Spinner spinner) {
    return "Spinner" + spinner.getId() + "Position";
  }


  void saveSpinnerSetting(SharedPreferences.Editor editor, final Spinner spinner) {
    editor.putInt(spinnerPreferenceKey(spinner), spinner.getSelectedItemPosition());
  }

  void registerSpinnerListenner(Spinner spinner, final SpinnerItemSelectListener listener) {
    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Object selectedItem = arg0.getItemAtPosition(arg2);
        listener.onSpinnerItemSelected(selectedItem, arg2);
      }

      public void onNothingSelected(AdapterView<?> arg0) {
      }
    });
  }

  public void setupSpinner(final Spinner spinner, final Object[] itemArray) {
    setupSpinner(spinner, itemArray, null);
  }

  public void setupSpinner(final Spinner spinner, final Object[] itemArray, Object defaultItem) {
    setupSpinner(spinner,
        (ArrayAdapter<?>) new ArrayAdapter(activity, android.R.layout.simple_spinner_item, itemArray),
        indexOf(itemArray, defaultItem));
  }
}
