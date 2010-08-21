package com.kaipic.lightmeter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;

public class MainWindowDialogFactory {
  private MainWindow mainWindow;

  public MainWindowDialogFactory(MainWindow mainWindow) {
    this.mainWindow = mainWindow;
  }
  protected Dialog create(int id) {
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
     AlertDialog.Builder builder = new AlertDialog.Builder(mainWindow);
     builder.setMessage("This will use the currently set manual exposure value to calibrate the auto light sensor. Please make sure the light sensor on your phone is getting the light matching that EV value right now and then you can click the calibrate button.")
         .setPositiveButton("Calibrate", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {
             mainWindow.getLightMeter().calibrate();
           }
         })
         .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {
             dialog.cancel();
           }
         })
         .setNeutralButton("Reset Factory", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {
             mainWindow.getLightMeter().resetCalibration();
             mainWindow.display();
           }
         });
     return builder.create();
   }

   private Dialog createAboutDialog() {
     return createSimpleDialog(R.layout.about_dialog, "About Light Meter");
   }


  private Dialog createSimpleDialog(int dialogId, String title) {
    final Dialog dialog = new Dialog(mainWindow);
    dialog.setContentView(dialogId);
    dialog.setTitle(title);
    return dialog;
  }

}
