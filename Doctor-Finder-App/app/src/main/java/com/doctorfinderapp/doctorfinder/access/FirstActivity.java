package com.doctorfinderapp.doctorfinder.access;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.doctorfinderapp.doctorfinder.Intro.DefaultIntro;
import com.doctorfinderapp.doctorfinder.MainActivity;
import com.doctorfinderapp.doctorfinder.R;
import com.parse.ParseUser;

import cn.pedant.SweetAlert.SweetAlertDialog;
import com.doctorfinderapp.doctorfinder.functions.AddDoctors;

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

        if (ParseUser.getCurrentUser() == null) {
            new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText("Benvenuto su Doctor Finder")
                    .setContentText("Con il nostro aiuto troverai facilmente lo specialista che stai cercando")
                    .setCustomImage(R.drawable.logoverde)
                    .show();
        }

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

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
