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

import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.SpecialSearchActivity;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


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
                                Intent intent = new Intent(SignupActivity.this, SpecialSearchActivity.class);
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

    }

    public void finish(){
        super.finish();
    }
}


