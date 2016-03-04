package com.doctorfinderapp.doctorfinder.Intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.doctorfinderapp.doctorfinder.R;

public class MainActivityIntro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_intro);
    }

    public void startDefaultIntro(View v){
        //Intent intent = new Intent(this, DefaultIntro.class);
        //startActivity(intent);
    }

    public void startCustomIntro(View v){
        Intent intent = new Intent(this, CustomIntro.class);
        startActivity(intent);
    }

}