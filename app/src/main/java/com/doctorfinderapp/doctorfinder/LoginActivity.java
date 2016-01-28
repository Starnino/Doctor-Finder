package com.doctorfinderapp.doctorfinder;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {



    // Declare Variables
    private Button loginButton;
    private Button loginWithFacebook;
    private Button loginWithGoogle;
    private String usernametxt;
    private String passwordtxt;
    private EditText password;
    private EditText username;
    private CheckBox remeberMe; //da implementare codice gestione rememberMe


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

        // Get the view from xml
        setContentView(R.layout.activity_login);

        // Locate EditTexts in xml
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        // Locate Buttons in xml
        loginButton = (Button) findViewById(R.id.login_button2);
        loginWithFacebook = (Button) findViewById(R.id.login_fb_button);
        loginWithGoogle= (Button) findViewById(R.id.login_google_button);

        //set font
        Typeface font = Typeface.createFromAsset(getAssets(), "font/Lato-Regular.ttf");
        loginButton.setTypeface(font);
        loginWithFacebook.setTypeface(font);
        loginWithGoogle.setTypeface(font);
        username.setTypeface(font);
        password.setTypeface(font);


        // Login Button Click Listener
        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Retrieve the text entered from the EditText
                usernametxt = username.getText().toString();
                passwordtxt = password.getText().toString();

                // Send data to Parse.com for verification
                ParseUser.logInInBackground(usernametxt, passwordtxt,
                        new LogInCallback() {
                            public void done(ParseUser user, ParseException e) {
                                if (user != null || usernametxt.equals("test")) {
                                    // If user exist and authenticated, send user to Welcome.class
                                    Intent intent = new Intent(
                                            LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(),
                                            "Successfully Logged in",
                                            Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "No such user exist, please signup",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }


        });

        //Login Facebook Button Click Listener --> da implementare
        loginWithFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
