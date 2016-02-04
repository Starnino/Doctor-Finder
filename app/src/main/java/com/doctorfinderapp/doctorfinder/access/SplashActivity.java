package com.doctorfinderapp.doctorfinder.access;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.doctorfinderapp.doctorfinder.R;

import java.util.Timer;
import java.util.TimerTask;
//import com.kinvey.android.Client;

public class SplashActivity extends AppCompatActivity {

    public static final int SPLASH_ACTIVITY_TIME = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        //roba di kinvey
        //final String your_app_key="kid_-JXvrsSX6l";
        //final String your_app_secret="b817a69644c44f468925349474f92b97";
        /*final Client mKinveyClient = new Client.Builder(your_app_key, your_app_secret
                , this.getApplicationContext()).build();*/
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
