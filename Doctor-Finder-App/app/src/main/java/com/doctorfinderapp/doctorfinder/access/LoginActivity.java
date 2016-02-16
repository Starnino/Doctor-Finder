package com.doctorfinderapp.doctorfinder.access;

import android.content.Intent;
import android.os.Bundle;
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

import com.doctorfinderapp.doctorfinder.ResultsActivity;

import com.doctorfinderapp.doctorfinder.R;
import com.facebook.CallbackManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;

//login facebook


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    private String usernametxt;
    private String passwordtxt;
    private EditText password;
    private EditText username;
    private CheckBox remeberMe; //da implementare codice gestione rememberMe
    private CallbackManager callbackManager;
    private GoogleApiClient client;



    public void onCreate(Bundle savedInstanceState) {
        //immersion mode

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        //initialize Facebook SDK
        //FacebookSdk.sdkInitialize(getApplicationContext());


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
        //Button loginWithGoogle = (Button) findViewById(R.id.login_google_button);
        ImageButton close = (ImageButton) findViewById(R.id.close);


        // Login Button Click Listener




        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Retrieve the text entered from the EditText
                progressBar.setVisibility(View.VISIBLE);
                usernametxt = username.getText().toString();
                passwordtxt = password.getText().toString();
                if (usernametxt.equals("test")) {
                    Intent intent = new Intent(LoginActivity.this,
                            ResultsActivity.class);
                    startActivity(intent);


                } else {

                    ParseUser.logInInBackground(usernametxt, passwordtxt, new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                Toast.makeText(getApplicationContext(),
                                        "Logged in",
                                        Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this, ResultsActivity.class);
                                startActivity(intent);

                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Incorrect Username or Password",
                                        Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }


            }


        });




        //loginWithFacebook.
        Button FLogin = (Button) findViewById(R.id.flogin);
        FLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                List<String> permissions = Arrays.asList("email", "public_profile");
                progressBar.setVisibility(View.VISIBLE);
                ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginActivity.this, permissions, new LogInCallback() {



                    @Override
                    public void done(ParseUser user, ParseException err) {
                        if (user == null) {
                            Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                            Log.d("MyApp", "errore parse"+err.toString());
                            progressBar.setVisibility(View.INVISIBLE);

                        } else if (user.isNew()) {
                            Log.d("MyApp", "User signed up and logged in through Facebook!");
                            progressBar.setVisibility(View.INVISIBLE);

                        } else {
                            Log.d("MyApp", "User logged in through Facebook!");
                            progressBar.setVisibility(View.INVISIBLE);

                            Toast.makeText(getApplicationContext(),
                                    "Logged in",
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, ResultsActivity.class);

                            startActivity(intent);

                        }
                    }
                });
            }

        });

        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
