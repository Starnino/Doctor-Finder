package com.doctorfinderapp.doctorfinder;

/**
 * Created by vindel100 on 17/01/16.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

public class SingleItemView extends Activity {
    // Declare Variables
    TextView txtname;
    TextView txtspecial;
    TextView txtfeedback;
    String name;
    String special;
    String feedback;
    Button buttonCall, buttonSend;

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

        buttonCall = (Button) findViewById(R.id.button_call);
        buttonSend = (Button) findViewById(R.id.button_send);

        buttonCall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "Thanks for use Doctor Finder", Toast.LENGTH_LONG).show();

            }
        });

        buttonSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "Thanks for use Doctor Finder", Toast.LENGTH_LONG).show();
            }
        });
    }
}