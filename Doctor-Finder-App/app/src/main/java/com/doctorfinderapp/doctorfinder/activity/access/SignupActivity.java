package com.doctorfinderapp.doctorfinder.activity.access;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.activity.MainActivity;
import com.doctorfinderapp.doctorfinder.functions.FacebookProfile;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;


public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    private ImageButton close;
    private Button signUp;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;
    private EditText repeatPassword;
    private boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //immersion mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        //code to permit facebook request
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //get the view from xml
        setContentView(R.layout.activity_signup);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);
        //locate button in xml
        signUp = (Button) findViewById(R.id.buttonSignUp);


        //locate EditText in xml
        firstName = (EditText) findViewById(R.id.first_name_text);
        lastName = (EditText) findViewById(R.id.last_name_text);
        email = (EditText) findViewById(R.id.email_text);
        password = (EditText) findViewById(R.id.password_text);
        repeatPassword = (EditText) findViewById(R.id.repeat_password_text);

        //SignUp Click Listener
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                //code to close keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                //
                //define String variables
                String fName;
                String lName;

                final String email_string;
                String password_string;
                String repeatPassword_string;

                //extract String from EditText fields
                fName = firstName.getText().toString();
                lName = lastName.getText().toString();
                email_string = email.getText().toString();
                password_string = password.getText().toString();
                repeatPassword_string = repeatPassword.getText().toString();

                // Force user to fill up the form
                if (password_string.equals("") || fName.equals("") || lName.equals("")
                        || email_string.equals("") || repeatPassword_string.equals("")) {
                    /*Toast.makeText(getApplicationContext(),
                            "Please complete the sign up form",
                            Toast.LENGTH_LONG).show();*/
                    Snackbar.make(v, R.string.complete_form, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                //password equals to repeatPassword
                else if (!password_string.equals(repeatPassword_string)) {
                    /*Toast.makeText(getApplicationContext(),
                            "Passwords don't match!",
                            Toast.LENGTH_LONG).show();*/
                    Snackbar.make(v, R.string.password_match_no, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                //abort short Passwords
                else if (password_string.length() < 6) {
                    /*Toast.makeText(getApplicationContext(),
                            "Please digit 6 or more characters",
                            Toast.LENGTH_LONG).show();*/
                    Snackbar.make(v, R.string.password_longer, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                } else {

                    Log.d("Login Activity", "progress bar pre onclick" + progressBar.getVisibility());
                    progressBar.setVisibility(View.VISIBLE);
                    Log.d("Login Activity", "progress bar afetr onclick" + progressBar.getVisibility());

                    final ParseUser user = new ParseUser();
                    user.setUsername(email_string);
                    user.setPassword(password_string);
                    user.setEmail(email_string);
                    user.put("fName", fName);
                    user.put("lName", lName);

                    //don't sign up if user email exist!
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("email", email_string);
                    //make progress bar visible only when signup in background

                    query.findInBackground(new FindCallback<ParseUser>() {
                        public void done(List<ParseUser> objects, ParseException e) {
                            if (e == null) {
                                // The query was successful.
                                if (objects.size() == 0) {
                                    //the user not exists



                                    if (clicked == false) {
                                        clicked=true;
                                        Log.d("Signup", "user not exists");
                                        user.signUpInBackground(new SignUpCallback() {

                                            public void done(com.parse.ParseException e) {
                                                if (e == null) {
                                                    clicked = false;
                                                    // Hooray! Let them use the app now.
                                                    //create a toast
                                                    Snackbar.make(v, R.string.signup_completed, Snackbar.LENGTH_SHORT)
                                                            .setAction("Action", null).show();
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    Log.d("Login Activity", "signup" + progressBar.getVisibility());


                                                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                    finish();

                                                } else {
                                                    //
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    clicked = false;
                                                    Log.v(TAG, "errore");
                                                    Log.v(TAG, e.toString());
                                                    // Sign up didn't succeed. Look at the ParseException
                                                    // to figure out what went wrong
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    //user exists

                                    /*Toast
                                            .makeText(getApplicationContext(),
                                                    "This user alredy exists on database",
                                                    Toast.LENGTH_LONG).show();*/
                                    Snackbar.make(v, R.string.user_exists, Snackbar.LENGTH_SHORT)
                                            .setAction("Action", null).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    //Log.d("Signup", "user exists " + email_string);

                                }


                            } else {
                                // Something went wrong.
                                Log.d("Signup", "Something went wrong");
                            }
                        }
                    });



                }
            }
        });


        close = (ImageButton) findViewById(R.id.close2);
        //close button  x
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //loginWithFacebook.
        Button FLogin = (Button) findViewById(R.id.flogin2);
        FLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(final View v) {


                progressBar.setVisibility(View.VISIBLE);
                ParseFacebookUtils.logInWithReadPermissionsInBackground(SignupActivity.this, GlobalVariable.permissions, new LogInCallback() {


                    @Override
                    public void done(ParseUser user, ParseException err) {
                        if (user == null) {
                            Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                            Log.d("MyApp", "errore parse" + err.toString());
                            progressBar.setVisibility(View.INVISIBLE);

                        } else if (user.isNew()) {
                            Log.d("MyApp", "User signed up and logged in through Facebook!");

                            Snackbar.make(v, R.string.signup_completed, Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                            //facebook request
                            FacebookProfile.getGraphRequest(user);

                            progressBar.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        } else {
                            Log.d("MyApp", "User logged in through Facebook!");
                            progressBar.setVisibility(View.INVISIBLE);
                            //new LongOperation().execute();
                            Snackbar.make(v, R.string.signup_completed, Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                            //facebook request
                            FacebookProfile.getGraphRequest(user);

                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        }
                    }
                });
            }

        });

    }

    public void finish() {
        super.finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
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


