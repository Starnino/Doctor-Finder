package com.doctorfinderapp.doctorfinder;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

public class FeedbackItemActivity extends AppCompatActivity {

    private  AlertDialog.Builder alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set status bar color because in xml don't work
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        setContentView(R.layout.activity_scrolling_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert = new AlertDialog.Builder(FeedbackItemActivity.this);
                alert.setTitle("Vuoi lasciare un feedback?");
                alert.setMessage("Puoi lasciare un feedback soltanto se precedentemente sei stato paziente di questo dottore!");
                alert.setPositiveButton("Ok", null);
                alert.setNegativeButton("No", null);
                alert.setIcon(R.drawable.ic_star_rate_white_18dp);
                alert.show();
            }
        });
    }
}
