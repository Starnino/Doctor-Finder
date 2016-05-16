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
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
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

        //TODO riaggiungere sicoli @giampo

        /**ATTENTION PLEASE! THIS IS THE FUNCTION FOR LOAD COMPLETELY A DOCTOR! DON'T PLAY WITH IT*/
        /**LEGGI SOTTO!
         *
         */
        /*
        final String email = "prova@gmail.com";
        final String firstName = "prova";
        final String lastName = "prova";
        final String visit = "su prenotazione";                              //like --> lun-ven 10-19
        final String description = "prova applicazione...";                        //don't link linkedin profile but text
        final String price = "meno di 50";
        final String[] work = new String[]{"Roma"};
        final String[] province = new String[]{"Roma"};
        final String[] specialization = new String[]{"Odontoiatria"};          //like array of--> Odontoiatria (big letter)
        final String experience = "meno di 5 anni";                         //anni
        final String born = "08/11/1994";                               //giorno/mese/anno
        final String sesso = "M";                              // "M" or "F"
        final String phone = "33390934376";
        final String[] marker = new String[]{"Via di Boccea, 437, 00166, Roma"};                  //like array of--> via esempio, civico, cap, provincia
        final Resources res = getResources();
        final int doctorDrawable = R.drawable.doctor_avatar;    //image png 250 x 250 from drawable

        Util.addDoctorWithoutPhoto(email,firstName, lastName, visit,description,
                price,work, province, experience, born, specialization,
                sesso, phone, Util.getMarkerForDoctor(this, marker), res);*/


        //TODO per aggiungere dottore modifica il codice sottostante

        /*final String email = "marcofiniguerra@yahoo.it";
        final String firstName = "Marco";
        final String lastName = "Finiguerra";
        final String visit = "Previo appuntamento";                              //like --> lun-ven 10-19
        final String description = "Medicina Estetica, Nutrizione Clinica e Dietoterapia";                        //don't link linkedin profile but text
        final String price = "Tra 50 e 100 euro";
        final String[] work = new String[]{"Torino"};
        final String[] province = new String[]{"Torino"};
        final String[] specialization = new String[]{"Medicina Estetica"};          //like array of--> Odontoiatria (big letter)
        final String experience = "Meno di 5 anni";                         //anni
        final String born = "25/06/1979";                               //giorno/mese/anno
        final String sesso = "M";                              // "M" or "F"
        final String phone = "3473046161";
        final String[] marker = new String[]{"Via Giacinto Collegno, 48, 10138, Torino"};                  //like array of--> via esempio, civico, cap, provincia
        final Resources res = getResources();
        final int doctorDrawable = R.drawable.finiguerra_marco;    //image png 250 x 250 from drawable

        Util.addDoctorWithPhoto(email,firstName, lastName, visit,description,
                price,work, province, experience, born, specialization,
                sesso, phone, Util.getMarkerForDoctor(this, marker), res,doctorDrawable);*/

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
