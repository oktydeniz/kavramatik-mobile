package com.kavramatik.kavramatik.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.speech.tts.TextToSpeech;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kavramatik.kavramatik.R;


public class AppAlertDialogs {
    private final Activity classActivity;
    private AlertDialog alertDialog;
    public static TextToSpeech textToSpeech;

    public static void showOnlyOnce(Context context, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.opening_dialog_text);
        builder.setCancelable(false);
        builder.setTitle(R.string.information);
        builder.setPositiveButton(R.string.continue_text, listener);
        builder.setNegativeButton(R.string.open_tts_engine, listener);
        builder.create().show();
    }

    public AppAlertDialogs(Activity activity) {
        this.classActivity = activity;
    }

    public static void noTTSPacketDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.install_tts_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setWindowAnimations(R.style.AnimationForDialogs);
        FloatingActionButton button = dialog.findViewById(R.id.openPlayStore);
        button.setOnClickListener(v -> IntentsTTS.installTTS(context));
        dialog.show();
    }


    public static void showAlertDialogForSettings(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.transparent_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setWindowAnimations(R.style.AnimationForDialogs);
        FloatingActionButton button = dialog.findViewById(R.id.openSettingMenu);
        button.setOnClickListener(v -> IntentsTTS.openTTSSettings(context));
        dialog.show();
    }

    public static void permissionDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.assistant);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setWindowAnimations(R.style.AnimationForDialogs);
        dialog.show();
    }
    public static void matchDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.assistant);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setWindowAnimations(R.style.AnimationForDialogs);
        TextView textView = dialog.findViewById(R.id.textView7);
        textView.setText(context.getResources().getString(R.string.match_assistant));
        dialog.show();
    }

    public void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(classActivity);
        builder.setView(R.layout.loading_dialog);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }

    public static void educationAssistant(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.education_assistant);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setWindowAnimations(R.style.AnimationForDialogs);
        dialog.show();
    }

    public static void sstDialogForInstall(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.transparent_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setWindowAnimations(R.style.AnimationForDialogs);
        FloatingActionButton button = dialog.findViewById(R.id.openSettingMenu);
        button.setOnClickListener(v -> IntentsTTS.sttApp(context));
        dialog.show();

    }

    public void dismissLoading() {
        alertDialog.dismiss();
    }
}
