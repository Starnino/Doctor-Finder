package com.doctorfinderapp.doctorfinder.Intro;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.doctorfinderapp.doctorfinder.MainActivity;
import com.doctorfinderapp.doctorfinder.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.doctorfinderapp.doctorfinder.Intro.CustomIntro;


public class DefaultIntro extends AppIntro {

    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(SampleSlide.newInstance(R.layout.intro));
        addSlide(SampleSlide.newInstance(R.layout.intro2));
        addSlide(SampleSlide.newInstance(R.layout.intro3));
        addSlide(SampleSlide.newInstance(R.layout.intro4));
    }

    private void loadMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onNextPressed() {
    }

    @Override
    public void onSkipPressed() {
        loadMainActivity();
        Toast.makeText(getApplicationContext(),
                getString(R.string.skip), Toast.LENGTH_SHORT).show();
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
