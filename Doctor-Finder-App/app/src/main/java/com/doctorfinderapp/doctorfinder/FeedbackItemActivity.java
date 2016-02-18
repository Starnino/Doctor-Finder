package com.doctorfinderapp.doctorfinder;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class FeedbackItemActivity extends AppCompatActivity {

    private  AlertDialog.Builder alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //alert
        alert = new AlertDialog.Builder(FeedbackItemActivity.this);
        alert.setTitle("Vuoi lasciare un feedback?");
        alert.setMessage("Clicca la stella sulla a destra e scrivi il tuo feedback!");
        alert.setPositiveButton("Ok", null);
        alert.setNegativeButton("No",null);
        alert.show();


        setContentView(R.layout.activity_scrolling_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Scrivi il tuo feedback", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
