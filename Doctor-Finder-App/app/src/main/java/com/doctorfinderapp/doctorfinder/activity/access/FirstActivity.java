package com.doctorfinderapp.doctorfinder.activity.access;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.doctorfinderapp.doctorfinder.activity.MainActivity;
import com.doctorfinderapp.doctorfinder.R;

import android.os.Handler;

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

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
               Toast.makeText(this, "Premi un'altra volta per uscire", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }



}
