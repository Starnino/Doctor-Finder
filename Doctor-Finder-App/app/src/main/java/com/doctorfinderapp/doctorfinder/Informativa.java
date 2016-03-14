package com.doctorfinderapp.doctorfinder;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;


import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


/**
 * Created by giovanni on 3/8/16.
 */

public class Informativa extends AppCompatActivity{

    private String Title = "Informativa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scrolling_informativa);
/*
    final CollapsingToolbarLayout collapsingToolbarLayout =
            (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_user);
    collapsingToolbarLayout.setTitle(Title);
    collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent));
    // transperent color = #00000009
    collapsingToolbarLayout.setCollapsedTitleTextColor(Color.rgb(255, 255, 255));
*/
    }


    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.home:
                finish();
                break;
        }

    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();
            finish();
            }

}


