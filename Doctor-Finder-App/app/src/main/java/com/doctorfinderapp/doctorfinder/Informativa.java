package com.doctorfinderapp.doctorfinder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import android.view.MenuItem;



/**
 * Created by giovanni on 3/8/16.
 */
public class Informativa extends AppCompatActivity{

    private String Title = "Informativa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scrolling_report_feedback);
/*
    final CollapsingToolbarLayout collapsingToolbarLayout =
            (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_user);
    collapsingToolbarLayout.setTitle(Title);
    collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent));
    // transperent color = #00000000
    collapsingToolbarLayout.setCollapsedTitleTextColor(Color.rgb(255, 255, 255));

*/
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

}
