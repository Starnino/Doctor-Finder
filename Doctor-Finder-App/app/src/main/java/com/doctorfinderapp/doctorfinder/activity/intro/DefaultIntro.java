package com.doctorfinderapp.doctorfinder.activity.intro;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.activity.access.FirstActivity;
import com.github.paolorotolo.appintro.AppIntro;


public class DefaultIntro extends AppIntro {
    private String key= "DoctorFinder";
    private boolean isShowed;

    @Override
    public void init(Bundle savedInstanceState) {


        //add immersive mode
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        //finish immersive mode
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        //editor.putString("Name","Bacci");
        //editor.apply();

        //preferences.getAll();
        //Log.d("intro", preferences.getAll().toString());


       boolean ciao=preferences.getBoolean(key,isShowed);
        //Log.d("intro",ciao+"");

        if(ciao){
            loadMainActivity();
        }

        //pop up
        /*if (ParseUser.getCurrentUser() == null) {
            new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText("Benvenuto su Doctor Finder")
                    .setContentText("Con il nostro aiuto troverai facilmente lo specialista che stai cercando")
                    .setCustomImage(R.drawable.logoverde)
                    .show();
        }*/

        addSlide(SampleSlide.newInstance(R.layout.intro));
        addSlide(SampleSlide.newInstance(R.layout.intro2));
        addSlide(SampleSlide.newInstance(R.layout.intro3));
        addSlide(SampleSlide.newInstance(R.layout.intro4));
    }

    private void loadMainActivity() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        isShowed=true;
        editor.putBoolean(key, isShowed);
        editor.apply();




        Intent intent = new Intent(this,FirstActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onNextPressed() {
    }

    @Override
    public void onSkipPressed() {
        loadMainActivity();
    }

    @Override
    public void onDonePressed() {
        loadMainActivity();
    }

    @Override
    public void onSlideChanged() {
    }


    public void getStarted(View v) {
        loadMainActivity();
    }

}
