package com.doctorfinderapp.doctorfinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.app.AlertDialog;
import android.view.WindowManager;
import android.widget.TextView;

import org.w3c.dom.Text;


public class FirstActivity extends Activity {

    private ImageButton searchButton;
    private Button loginButton;
    private Button signupButton;
    private AlertDialog.Builder alert;
    private TextView searchText;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        //immersion mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set activity layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        //buttons get xml layout
        searchButton = (ImageButton) findViewById(R.id.search_button);
        loginButton = (Button) findViewById(R.id.login_button);
        signupButton = (Button) findViewById(R.id.signin_button);

        //textview get xml
        searchText = (TextView) findViewById(R.id.search_text);

        //set font
        Typeface font = Typeface.createFromAsset(getAssets(), "font/Lato-Regular.ttf");
        loginButton.setTypeface(font);
        signupButton.setTypeface(font);
        searchText.setTypeface(font);


        //alert
        alert = new AlertDialog.Builder(FirstActivity.this);
        alert.setTitle("Notice");
        alert.setMessage("Please do not use this application in case of medical emergency!");
        alert.setPositiveButton("OK",null);
        alert.show();

        //searchButton Click Listener
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //loginButton Click Listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //signupButton Click Listener
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

}
