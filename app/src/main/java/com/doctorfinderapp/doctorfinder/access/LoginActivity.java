package com.doctorfinderapp.doctorfinder.access;

import com.doctorfinderapp.doctorfinder.MainActivity;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.SpecialSearchActivity;
import com.facebook.CallbackManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

//login facebook
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {



    // Declare Variables
    private Button loginButton;
    private LoginButton loginWithFacebook;
    private Button loginWithGoogle;
    private String usernametxt;
    private String passwordtxt;
    private EditText password;
    private EditText username;
    private CheckBox remeberMe; //da implementare codice gestione rememberMe
    private CallbackManager callbackManager;

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

        //initialize callback Manager
        callbackManager = CallbackManager.Factory.create();

        // Locate EditTexts in xml
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        // Locate Buttons in xml
        //initialize LoginButton (Facebook sdk)
        loginWithFacebook = (LoginButton) findViewById(R.id.login_fb_button);
        loginButton = (Button) findViewById(R.id.login_button2);
        loginWithGoogle= (Button) findViewById(R.id.login_google_button);

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
                usernametxt = username.getText().toString();
                passwordtxt = password.getText().toString();
                if (usernametxt.equals("test")){
                    Intent intent = new Intent(LoginActivity.this,
                            SpecialSearchActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "Incorrect Username or Password",
                            Toast.LENGTH_LONG).show();
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

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
