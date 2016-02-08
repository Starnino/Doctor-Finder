package com.doctorfinderapp.doctorfinder.access;

import com.doctorfinderapp.doctorfinder.MainActivity;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.SpecialSearchActivity;
import com.facebook.CallbackManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

//login facebook
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    /**
     * Called when the activity is first created. Per prova d'accesso provare
     *
     * username = "test"
     * password = "qualsiasi cosa" (inteso come qualsiasi stringa anche vuota)
     */
    public void onCreate(Bundle savedInstanceState) {
        //immersion mode

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        //initialize Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());

        // Get the view from xml
        setContentView(R.layout.activity_login);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        //initialize callback Manager
        callbackManager = CallbackManager.Factory.create();

        // Locate EditTexts in xml
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        // Locate Buttons in xml
        //initialize LoginButton (Facebook sdk)
        LoginButton loginWithFacebook = (LoginButton) findViewById(R.id.login_fb_button);
        Button loginButton = (Button) findViewById(R.id.login_button2);
        Button loginWithGoogle = (Button) findViewById(R.id.login_google_button);
        ImageButton close = (ImageButton) findViewById(R.id.close);

        //set font
        Typeface font = Typeface.createFromAsset(getAssets(), "font/Lato-Regular.ttf");
        loginButton.setTypeface(font);
        //loginWithFacebook.setTypeface(font);
        loginWithGoogle.setTypeface(font);
        username.setTypeface(font);
        password.setTypeface(font);


        // Login Button Click Listener
        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Retrieve the text entered from the EditText
                progressBar.setVisibility(View.VISIBLE);
                usernametxt = username.getText().toString();
                passwordtxt = password.getText().toString();
                if (usernametxt.equals("test")) {
                    Intent intent = new Intent(LoginActivity.this,
                            SpecialSearchActivity.class);
                    startActivity(intent);


                } else {

                    ParseUser.logInInBackground(usernametxt, passwordtxt, new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                Toast.makeText(getApplicationContext(),
                                        "Logged in",
                                        Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this, SpecialSearchActivity.class);
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

        //Facebook Login Callback
        loginWithFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(LoginActivity.this, "Login Succesful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this,
                        MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
