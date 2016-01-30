package com.doctorfinderapp.doctorfinder;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SignupActivity extends AppCompatActivity {

    private Button signUp;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;
    private EditText repeatPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //immersion mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        //get the view from xml
        setContentView(R.layout.activity_signup);

        //locate button in xml
        signUp = (Button) findViewById(R.id.buttonSignUp);


        //locate EditText in xml
        firstName = (EditText) findViewById(R.id.first_name_text);
        lastName = (EditText) findViewById(R.id.last_name_text);
        email = (EditText) findViewById(R.id.email_text);
        password = (EditText) findViewById(R.id.password_text);
        repeatPassword = (EditText) findViewById(R.id.repeat_password_text);

        //set font
        Typeface font = Typeface.createFromAsset(getAssets(), "font/Lato-Regular.ttf");
        firstName.setTypeface(font);
        lastName.setTypeface(font);
        email.setTypeface(font);
        password.setTypeface(font);
        repeatPassword.setTypeface(font);
        signUp.setTypeface(font);

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
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });


    }
}


