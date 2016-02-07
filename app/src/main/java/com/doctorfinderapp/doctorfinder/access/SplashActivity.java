package com.doctorfinderapp.doctorfinder.access;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;


import com.doctorfinderapp.doctorfinder.R;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.IOException;
import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    public static final int SPLASH_ACTIVITY_TIME = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Parse.enableLocalDatastore(this);

        Context myContext = null;
        Parse.initialize(new Parse.Configuration.Builder(this)
                        .applicationId("YOUR_APP_ID")
                        .clientKey("YOUR_APP_CLIENT_KEY")
                        .server("http://doctor-finder-server.herokuapp.com:1337/parse")


                        .build()
        );


        ParseUser user = new ParseUser();
        user.setUsername("my name");
        user.setPassword("my pass");
        user.setEmail("email@example.com");

// other fields can be set just like with ParseObject
        user.put("phone", "650-253-0000");

        user.signUpInBackground(new SignUpCallback() {
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    Log.v(TAG, "OK");
                } else {
                    Log.v(TAG, "errore");
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });

        /*
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
        */

    }








}
