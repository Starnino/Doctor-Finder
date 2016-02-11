package com.doctorfinderapp.doctorfinder.access;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.doctorfinderapp.doctorfinder.R;

import java.util.Timer;
import java.util.TimerTask;

import static com.doctorfinderapp.doctorfinder.functions.StartParse.startParse;

public class SplashActivity extends AppCompatActivity {
    public static final int SPLASH_ACTIVITY_TIME = 4000;
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //inizialize parse


        startParse(this);

        //TODO:make this method return something

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
