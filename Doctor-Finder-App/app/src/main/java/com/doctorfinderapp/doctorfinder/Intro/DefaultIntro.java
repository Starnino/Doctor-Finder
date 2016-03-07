package com.doctorfinderapp.doctorfinder.Intro;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.doctorfinderapp.doctorfinder.MainActivity;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.access.FirstActivity;
import com.doctorfinderapp.doctorfinder.access.SplashActivity;
import com.github.paolorotolo.appintro.AppIntro;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class DefaultIntro extends AppIntro {

    @Override
    public void init(Bundle savedInstanceState) {

        //add immersive mode
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        //finish immersive mode

        addSlide(SampleSlide.newInstance(R.layout.intro));
        addSlide(SampleSlide.newInstance(R.layout.intro2));
        addSlide(SampleSlide.newInstance(R.layout.intro3));
        addSlide(SampleSlide.newInstance(R.layout.intro4));
    }

    private void loadMainActivity() {
        Log.d("NON PARTE", "DIOCANE");
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
