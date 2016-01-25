package com.doctorfinderapp.doctorfinder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ui.ParseLoginBuilder;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    public static final int SPLASH_ACTIVITY_TIME = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "RIZRj6QdFVtP5r5JVnyWsmhVjW6PeRMc2X17UNFA", "97G4cRoADJF0bu3sTn7zAkGTBFoxGP5LJ55wONKX");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                // Start the next activity

                Intent mainIntent = new Intent().setClass(
                        SplashActivity.this,FirstActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // Close the activity so the user won't able to go back this
                // activity pressing Back button
                finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, SPLASH_ACTIVITY_TIME);

    }


}
