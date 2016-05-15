package com.doctorfinderapp.doctorfinder.activity.access;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.doctorfinderapp.doctorfinder.AnalyticsTrackers;
import com.doctorfinderapp.doctorfinder.activity.intro.DefaultIntro;
import com.doctorfinderapp.doctorfinder.activity.MainActivity;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.functions.FacebookProfile;
import com.doctorfinderapp.doctorfinder.functions.Util;
import com.google.android.gms.analytics.Tracker;
import com.parse.ParseUser;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class SplashActivity extends AppCompatActivity {
    public static final int SPLASH_ACTIVITY_TIME = 4000;
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final ProgressWheel progressWheel = (ProgressWheel) findViewById(R.id.progressSplash);
        progressWheel.setBarColor(getResources().getColor(R.color.lightdf));

        progressWheel.spin();

        /**ATTENTION PLEASE! THIS IS THE FUNCTION FOR LOAD COMPLETELY A DOCTOR! DON'T PLAY WITH IT*/
        /*
        final String email = null;
        final String firstName = null;
        final String lastName = null;
        final String visit = null;                              //like --> lun-ven 10-19
        final String description = null;                        //don't link linkedin profile but text
        final String price = null;
        final String[] work = new String[0];
        final String[] province = new String[0];
        final String[] specialization = new String[0];          //like array of--> Odontoiatria (big letter)
        final String experience = null;                         //anni
        final String born = null;                               //giorno/mese/anno
        final String sesso = null;                              // "M" or "F"
        final String phone = null;
        final String[] marker = new String[0];                  //like array of--> via esempio 10, cap, provincia
        final Resources res = getResources();
        final int doctorDrawable = R.drawable.doctor_avatar;    //image png 250 x 250 from drawable

        Util.addDoctor(email,firstName, lastName, visit,description,
                price,work, province, experience, born, specialization,
                sesso, phone, Util.getMarkerForDoctor(this, marker),
                res, doctorDrawable);*/

        //add immersive mode
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        //finish immersive mode

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // Start the next activity


                ParseUser currentUser = ParseUser.getCurrentUser();
                if (currentUser != null) {

                    FacebookProfile.getGraphRequest(ParseUser.getCurrentUser());
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();

                } else {
                    // show the signup or login screen

                    Intent mainIntent = new Intent().setClass(
                            SplashActivity.this, DefaultIntro.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    // Close the activity so the user won't able to go back this
                    // activity pressing Back button
                    finish();
                    progressWheel.stopSpinning();
                    //progressWheel.setVisibility(View.INVISIBLE);
                }

            }
        };
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_ACTIVITY_TIME);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onStart() {
        super.onStart();


    }
}
