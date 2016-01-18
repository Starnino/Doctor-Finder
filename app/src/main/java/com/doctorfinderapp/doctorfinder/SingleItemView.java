package com.doctorfinderapp.doctorfinder;

/**
 * Created by vindel100 on 17/01/16.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SingleItemView extends Activity {
    // Declare Variables
    TextView txtname;
    TextView txtspecial;
    TextView txtfeedback;
    String name;
    String special;
    String feedback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singleitemview);
        // Retrieve data from MainActivity on item click event
        Intent i = getIntent();
        name = i.getStringExtra("name");
        special = i.getStringExtra("special");
        feedback = i.getStringExtra("feedback");

        // Locate the TextViews in singleitemview.xml
        txtname = (TextView) findViewById(R.id.name);
        txtspecial = (TextView) findViewById(R.id.special);
        txtfeedback = (TextView) findViewById(R.id.feedback);

        // Load the results into the TextViews
        txtname.setText(name);
        txtspecial.setText(special);
        txtfeedback.setText(feedback);
    }
}