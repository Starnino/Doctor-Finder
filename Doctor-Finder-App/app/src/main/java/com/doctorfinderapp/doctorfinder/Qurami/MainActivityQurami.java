package com.doctorfinderapp.doctorfinder.Qurami;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.doctorfinderapp.doctorfinder.R;

public class MainActivityQurami extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_qurami);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }
}