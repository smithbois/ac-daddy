package com.smithboys.acdaddy.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.smithboys.acdaddy.R;

public class SettingsDialog {

    public static Dialog onCreateDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialogS = builder.create();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.settings_dialog_layout, null);
        dialogS.setView(dialogView);
        dialogS.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // place dialog activity code here

        return dialogS;
    }

}
