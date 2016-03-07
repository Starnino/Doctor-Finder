package com.doctorfinderapp.doctorfinder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doctorfinderapp.doctorfinder.adapter.PersonAdapter;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.doctorfinderapp.doctorfinder.functions.RoundedImageView;
import com.doctorfinderapp.doctorfinder.functions.Util;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import android.util.Log;
import android.widget.Toast;


import java.util.List;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class UserProfileActivity extends AppCompatActivity {

    private AlertDialog.Builder alert;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PersonAdapter mAdapter;
    private Button segnala;
    private String USER_EMAIL;
    private int index;
    private RelativeLayout segnalalo;
    private RelativeLayout rateus;
    private RelativeLayout cambia;
    private List<ParseObject> userList = null;
    private ParseObject currentUser = null;
    private boolean USER_SEX;
    private String Title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //retrieve index from Activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            index = extras.getInt("index");
        }

        //scrolling
        setContentView(R.layout.activity_scrolling_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Titolo AppBar
        Title="Utente";

        //nameProfile.setText(Title);
        if(getSupportActionBar()!=null){
            //getSupportActionBar().setTitle(Title);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        String indexUser = ParseUser.getCurrentUser().getObjectId().toString();
        //setInfo(indexUser);
        TextView utente = (TextView) findViewById(R.id.txt_users);
        TextView email = (TextView) findViewById(R.id.emaillino);

        //utente.setText(ParseUser.getCurrentUser().get("fName").toString() + " " + currentUser.get("lName").toString());
        String fisrtName = ParseUser.getCurrentUser().get("fName").toString();
        String lastName = ParseUser.getCurrentUser().get("lName").toString();
        String email_users = ParseUser.getCurrentUser().get("email").toString();
        utente.setText(fisrtName + " " + lastName);
        email.setText(email_users);
        //email.setText(ParseUser.getCurrentUser().get("email").toString());

        //UserPhoto
        //floating button for report problems
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert = new AlertDialog.Builder(UserProfileActivity.this);
                alert.setTitle("Hai bisogno di aiuto?");
                alert.setMessage("Se stai riscontrando problemi con il tuo profilo, o " +
                        "hai problemi con un dottore, clicca sul pulsante posto in basso, segnala problema, " +
                        " e spiegaci il tuo problema. Saremo a tua disposizione per aiutarti!");
                alert.setPositiveButton("Ho capito", null);
                alert.show();
            }
        });

        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert = new AlertDialog.Builder(UserProfileActivity.this);
                alert.setTitle("Hai bisogno di aiuto?");
                alert.setMessage("Se stai riscontrando problemi con il tuo profilo, o " +
                        "hai problemi con un dottore, clicca sul pulsante posto in basso, segnala problema, " +
                        " e spiegaci il tuo problema. Saremo a tua disposizione per aiutarti!");
                alert.setPositiveButton("Ho capito", null);
                alert.show();
            }
        });

        RelativeLayout segnala = (RelativeLayout) findViewById(R.id.segnalalo);
        segnala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(UserProfileActivity.this)
                        .setTitleText("Hai qualcosa da segnalarci?")
                        .setContentText("Inviaci una mail a info@doctorfinderapp.com")
                        .setConfirmText("Riceverai presto nostre notizie!")
                        .show();

            }
        });

        RelativeLayout rateus = (RelativeLayout) findViewById(R.id.rateus);
        rateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_about = new Intent(UserProfileActivity.this, WebViewActivity.class);

                Bundle about = new Bundle();
                about.putString("URL",
                        "https://play.google.com/store/apps/details?id=com.doctorfinderapp.doctorfinder" );
                intent_about.putExtras(about);
                startActivity(intent_about);
            }
        });

        RelativeLayout cambia = (RelativeLayout) findViewById(R.id.cambiamelo);
        cambia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(UserProfileActivity.this)
                        .setTitleText("Non lo so fare")
                        .show();
            }
        });

    }

    /*private void setInfo(final String idUser){

        ParseQuery<ParseObject> userQuery = ParseQuery.getQuery("_User");

        userQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    userList = objects;

                    for (int i = 0; i < userList.size(); i++) {
                        if(userList.get(i).getObjectId().equals(idUser))
                            currentUser=userList.get(i);
                    }
                }
                else {
                    new SweetAlertDialog(getApplicationContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Qualcosa è andato storto!")
                            .show();
                }
            }
        });

        TextView utente = (TextView) findViewById(R.id.tvNumber1);
        TextView email = (TextView) findViewById(R.id.email);

        //utente.setText(currentUser.get("fName").toString() + " " + currentUser.get("lName").toString());
        //email.setText(currentUser.get("email").toString());

    }*/
}

