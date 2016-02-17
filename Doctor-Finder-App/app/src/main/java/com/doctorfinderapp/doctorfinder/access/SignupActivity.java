package com.doctorfinderapp.doctorfinder.access;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.doctorfinderapp.doctorfinder.ResultsActivity;

import com.doctorfinderapp.doctorfinder.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.Arrays;
import java.util.List;


public class SignupActivity extends AppCompatActivity {

    private ImageButton close;
    private Button signUp;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;
    private EditText repeatPassword;
    private static final String TAG = "SignupActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //immersion mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        //FacebookSdk.sdkInitialize(getApplicationContext());
        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.



        //get the view from xml
        setContentView(R.layout.activity_signup);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        //locate button in xml
        signUp = (Button) findViewById(R.id.buttonSignUp);
        close = (ImageButton) findViewById(R.id.close);


        //locate EditText in xml
        firstName = (EditText) findViewById(R.id.first_name_text);
        lastName = (EditText) findViewById(R.id.last_name_text);
        email = (EditText) findViewById(R.id.email_text);
        password = (EditText) findViewById(R.id.password_text);
        repeatPassword = (EditText) findViewById(R.id.repeat_password_text);

        //SignUp Click Listener
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //define String variables
                String fName;
                String lName;

                String email_string;
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
                    Toast.makeText(getApplicationContext(),
                            "Please complete the sign up form",
                            Toast.LENGTH_LONG).show();
                }
                //password equals to repeatPassword
                else if (!password_string.equals(repeatPassword_string)) {
                    Toast.makeText(getApplicationContext(),
                            "Passwords don't match!",
                            Toast.LENGTH_LONG).show();
                }
                //abort short Passwords
                else if (password_string.length() < 6) {
                    Toast.makeText(getApplicationContext(),
                            "Please digit 6 or more characters",
                            Toast.LENGTH_LONG).show();
                }


                else {




                    ParseUser user = new ParseUser();
                    user.setUsername(email_string);
                    user.setPassword(password_string);
                    user.setEmail(email_string);
                    user.put(fName, lName);
                    //make progress bar visible only when signup in background
                    progressBar.setVisibility(View.VISIBLE);
                    user.signUpInBackground(new SignUpCallback() {
                        public void done(com.parse.ParseException e) {
                            if (e == null) {
                                // Hooray! Let them use the app now.
                                //create a toast
                                Toast.makeText(getApplicationContext(), "Signup completed", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignupActivity.this, ResultsActivity.class);
                                startActivity(intent);

                            } else {

                                Log.v(TAG, "errore");
                                Log.v(TAG, e.toString());
                                // Sign up didn't succeed. Look at the ParseException
                                // to figure out what went wrong
                            }
                        }
                    });

                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //loginWithFacebook.
        Button FLogin = (Button) findViewById(R.id.flogin2);
        FLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                List<String> permissions = Arrays.asList("email", "public_profile");


                progressBar.setVisibility(View.VISIBLE);
                ParseFacebookUtils.logInWithReadPermissionsInBackground(SignupActivity.this, permissions, new LogInCallback() {


                    @Override
                    public void done(ParseUser user, ParseException err) {
                        if (user == null) {
                            Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                            Log.d("MyApp", "errore parse" + err.toString());
                            progressBar.setVisibility(View.INVISIBLE);

                        } else if (user.isNew()) {
                            Log.d("MyApp", "User signed up and logged in through Facebook!");

                            //new LongOperation().execute(user);
                            Toast.makeText(getApplicationContext(),
                                    "Signed up",
                                    Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);

                        } else {
                            Log.d("MyApp", "User logged in through Facebook!");
                            progressBar.setVisibility(View.INVISIBLE);
                            //new LongOperation().execute();
                            Toast.makeText(getApplicationContext(),
                                    "Logged in",
                                    Toast.LENGTH_LONG).show();


                            //new LongOperation().execute(user);
                            Intent intent = new Intent(SignupActivity.this, ResultsActivity.class);

                            startActivity(intent);

                        }
                    }
                });
            }

        });

    }

    public void finish(){
        super.finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

   /* //thread to put facebook
    private class LongOperation extends AsyncTask<ParseUser, Void, com.parse.ParseException> {

        @Override
        protected ParseException doInBackground(ParseUser... params) {
            ParseException exp=null;
                try {
                    //Thread.sleep(1000);


                    exp=FacebookProfile.getFacebookThings(params[0]);

                } catch (InterruptedException e) {
                    Thread.interrupted();

            }

            return exp;
        }

        @Override
        protected void onPostExecute(ParseException result) {
            Log.d("Facebok Profile", "User updated on parse");
            Toast.makeText(getApplicationContext(),
                    "Facebook update user",
                    Toast.LENGTH_LONG).show();

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
*/
}


