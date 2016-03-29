package com.doctorfinderapp.doctorfinder.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;


import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.doctorfinderapp.doctorfinder.R;


/**
 * Created by giovanni on 3/8/16.
 */

public class Informativa extends AppCompatActivity{

    private String Title = "Informativa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scrolling_informativa);
        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_report_feedback);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

    final CollapsingToolbarLayout collapsingToolbarLayout =
            (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_report_feedback);
    collapsingToolbarLayout.setTitle(Title);
    collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent));
    // transperent color = #00000009
    collapsingToolbarLayout.setCollapsedTitleTextColor(Color.rgb(255, 255, 255));

    }


    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
               finish();
                return true;

        }
        return true;
    }


}


