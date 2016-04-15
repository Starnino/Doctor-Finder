package com.doctorfinderapp.doctorfinder.activity.access;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.doctorfinderapp.doctorfinder.activity.MainActivity;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.functions.FacebookProfile;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.facebook.CallbackManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.Arrays;
import java.util.List;


public class LoginActivity extends AppCompatActivity {


    private String usernametxt;
    private String passwordtxt;
    private EditText password;
    private EditText username;
    private CallbackManager callbackManager;
    private GoogleApiClient client;
    private ImageButton close;
    private TextView resetpsw;
    private TextView accedendo;



    public void onCreate(Bundle savedInstanceState) {
        //immersion mode

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        super.onCreate(savedInstanceState);

        //code to permit facebook request
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        // Get the view from xml
        setContentView(R.layout.activity_login);
        final ProgressWheel progressBar = (ProgressWheel) findViewById(R.id.progressBar1);
        progressBar.setBarColor(getResources().getColor(R.color.white));

        //initialize callback Manager
        callbackManager = CallbackManager.Factory.create();

        // Locate EditTexts in xml
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        TextView forgot_password=(TextView) findViewById(R.id.forgot_password);

        assert forgot_password != null;
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().length()>0)
                resetPassword(username.getText().toString());
            }
        });

        resetpsw = (TextView)findViewById(R.id.forgot_password);
        resetpsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(v.getContext())
                        .title("Reset password")
                        .content("Inserisci qui la tua mail e ti invieremo tutti i dettagli per recuperare la password!")
                        .inputType(InputType.TYPE_MASK_CLASS | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE)
                        .input("La tua Email", null, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                                Log.d("INPUT", input.toString());

                                final String body = "Ho perso la password per accedere a Doctor Finder questa è la mia mail ==> " + input.toString();

                                // TODO: 4/9/16 bisogna organizzare tutto cià da lato server

                                BackgroundMail.newBuilder(resetpsw.getContext())
                                        .withUsername("doctor.finder.dcf@gmail.com")
                                        .withPassword("quantomacina")
                                        .withMailto("info@doctorfinderapp.com")
                                        .withSubject("RESET PASSWORD")
                                        .withBody(body)
                                        .send();

                                Snackbar.make(resetpsw, "Controlla la posta!", Snackbar.LENGTH_LONG)
                                        .show();
                            }
                        }).positiveText("Recupera")
                        .negativeText("Annulla")

                        .show();
                /*
                ParseUser.requestPasswordResetInBackground("myemail@example.com", new RequestPasswordResetCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // An email was successfully sent with reset instructions.
                        } else {
                            // Something went wrong. Look at the ParseException to see what's up.
                        }
                    }
                });
                */
            }
        });

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
        assert loginButton != null;
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // Retrieve the text entered from the EditText
                progressBar.spin();
                usernametxt = username.getText().toString();
                passwordtxt = password.getText().toString();



                    ParseUser.logInInBackground(usernametxt, passwordtxt, new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {

                                Snackbar.make(v, R.string.access_ok, Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();

                            } else {

                                Snackbar.make(v, R.string.bad_login, Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                                progressBar.stopSpinning();

                            }
                        }
                    });
                }





        });




        //loginWithFacebook.
        Button FLogin = (Button) findViewById(R.id.flogin);
        assert FLogin != null;
        FLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                List<String> permissions = Arrays.asList("email", "public_profile","user_friends");
                progressBar.spin();

                ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginActivity.this, GlobalVariable.permissions, new LogInCallback() {


                    @Override
                    public void done(ParseUser user, ParseException err) {
                        if (user == null) {
                            Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                            progressBar.stopSpinning();

                        } else if (user.isNew()) {
                            Log.d("MyApp", "User signed up and logged in through Facebook!");
                            Log.d("login with facebook", user.toString());
                            FacebookProfile.getGraphRequest(user);


                            Snackbar.make(v, R.string.access_ok, Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                            progressBar.stopSpinning();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        } else {
                            Log.d("MyApp", "User logged in through Facebook!");
                            progressBar.stopSpinning();

                            //facebook things
                            Log.d("login with facebook", user.toString());
                            FacebookProfile.getGraphRequest(user);
                            //FacebookProfile.getGraphRequestFriends(user);
                            Snackbar.make(v, R.string.access_ok, Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
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

        accedendo = (TextView)findViewById(R.id.accedendo_clicca);
        accedendo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(v.getContext())
                        .title("Termini e condizioni")
                        .content(R.string.Informativa1, R.string.informativa2)
                        .inputType(InputType.TYPE_MASK_CLASS | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE)
                        .positiveText("Ho letto")

                        .show();

            }
        });




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    private void resetPassword(String email ){
        ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // An email was successfully sent with reset instructions.
                } else {
                    Log.d("Reset password",e.toString());
                    Toast.makeText(getApplicationContext(),
                            "Error",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }



}
