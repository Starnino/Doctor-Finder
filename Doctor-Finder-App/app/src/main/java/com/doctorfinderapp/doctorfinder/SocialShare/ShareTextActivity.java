package com.doctorfinderapp.doctorfinder.SocialShare;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.doctorfinderapp.doctorfinder.R;

public class ShareTextActivity extends ActionBarActivity {

    private EditText textEntry;
    private Button shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_text);

        textEntry = (EditText)findViewById(R.id.share_text_entry);
        shareButton = (Button)findViewById(R.id.share_text_button);

        setupEvents();
        setupActionBar();
    }

    private void setupEvents() {
        shareButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String userEntry = textEntry.getText().toString();

                Intent textShareIntent = new Intent(Intent.ACTION_SEND);
                textShareIntent.putExtra(Intent.EXTRA_TEXT, userEntry);
                textShareIntent.setType("text/plain");

                //startActivity(textShareIntent);
                // ^^ this auto-picks the defined default program for a content type, but since we want users to
                //    have options, we instead use the OS to create a chooser for users to pick from

                startActivity(Intent.createChooser(textShareIntent, "Share text with..."));
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            android.app.ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

}