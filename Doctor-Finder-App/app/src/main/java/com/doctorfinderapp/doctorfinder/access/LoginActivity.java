package com.doctorfinderapp.doctorfinder.access;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.doctorfinderapp.doctorfinder.MainActivity;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.functions.FacebookProfile;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.facebook.CallbackManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;


public class LoginActivity extends AppCompatActivity {


    private String usernametxt;
    private String passwordtxt;
    private EditText password;
    private EditText username;
    private CheckBox remeberMe; //da implementare codice gestione rememberMe
    private CallbackManager callbackManager;
    private GoogleApiClient client;
    private ImageButton close;



    public void onCreate(Bundle savedInstanceState) {
        //immersion mode
/*
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
                */

        super.onCreate(savedInstanceState);

        //code to permit facebook request
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        // Get the view from xml
        setContentView(R.layout.activity_login);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        //initialize callback Manager
        callbackManager = CallbackManager.Factory.create();

        // Locate EditTexts in xml
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        // Locate Buttons in xml

        Button loginButton = (Button) findViewById(R.id.login_button2);
        ImageButton close = (ImageButton) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });


        // Login Button Click Listener
        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Retrieve the text entered from the EditText
                progressBar.setVisibility(View.VISIBLE);
                usernametxt = username.getText().toString();
                passwordtxt = password.getText().toString();



                    ParseUser.logInInBackground(usernametxt, passwordtxt, new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                Toast.makeText(getApplicationContext(),
                                        "Logged in",
                                        Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Incorrect Username or Password",
                                        Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }





        });




        //loginWithFacebook.
        Button FLogin = (Button) findViewById(R.id.flogin);
        FLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                List<String> permissions = Arrays.asList("email", "public_profile","user_friends");
                progressBar.setVisibility(View.INVISIBLE);
                ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginActivity.this, GlobalVariable.permissions, new LogInCallback() {


                    @Override
                    public void done(ParseUser user, ParseException err) {
                        if (user == null) {
                            Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                            Log.d("MyApp", "errore parse" + err.toString());
                            progressBar.setVisibility(View.INVISIBLE);

                        } else if (user.isNew()) {
                            Log.d("MyApp", "User signed up and logged in through Facebook!");
                            Log.d("login with facebook", user.toString());
                            FacebookProfile.getGraphRequest(user);


                            Toast.makeText(getApplicationContext(),
                                    "Logged in",
                                    Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        } else {
                            Log.d("MyApp", "User logged in through Facebook!");
                            progressBar.setVisibility(View.INVISIBLE);

                            //facebook things
                            Log.d("login with facebook", user.toString());
                            FacebookProfile.getGraphRequest(user);
                            //FacebookProfile.getGraphRequestFriends(user);
                            Toast.makeText(getApplicationContext(),
                                    "Logged in",
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        }
                    }
                });
            }

        });

        //close activity
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }



}
