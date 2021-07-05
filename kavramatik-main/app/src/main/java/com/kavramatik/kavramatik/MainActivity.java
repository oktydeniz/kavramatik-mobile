package com.kavramatik.kavramatik;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.kavramatik.kavramatik.util.AppAlertDialogs;
import com.kavramatik.kavramatik.util.IntentsTTS;
import com.kavramatik.kavramatik.util.SharedPreferencesManager;

public class MainActivity extends AppCompatActivity implements DialogInterface.OnClickListener {
    ActionBar actionBar;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = getSupportActionBar();
        ColorDrawable drawable = new ColorDrawable(Color.parseColor("#FF018786"));
        actionBar.setBackgroundDrawable(drawable);

        boolean isFirstTime = SharedPreferencesManager.getIsFirstTime(this);
        if (isFirstTime) {
            AppAlertDialogs.showOnlyOnce(MainActivity.this, this);
            SharedPreferencesManager.setIsFirstTime(MainActivity.this, false);
        }
        navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        NavigationUI.setupActionBarWithNavController(this, navController);

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.tts")));
        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
            IntentsTTS.openTTSSettings(getApplicationContext());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }
}