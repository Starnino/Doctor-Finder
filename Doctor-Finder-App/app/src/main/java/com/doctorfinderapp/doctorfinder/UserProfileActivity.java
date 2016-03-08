package com.doctorfinderapp.doctorfinder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doctorfinderapp.doctorfinder.SocialShare.MainActivitySocialShare;
import com.doctorfinderapp.doctorfinder.Class.Person;
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
    private RelativeLayout condividi;
    private List<ParseObject> userList = null;
    private ParseObject currentUser = null;
    private boolean USER_SEX;
    private String Title ="";
    private RoundedImageView profile;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_user);
        setSupportActionBar(toolbar);

        //se non sei loggato vai via

        if(ParseUser.getCurrentUser()!=null) {


            String indexUser = ParseUser.getCurrentUser().getObjectId().toString();
            //setInfo(indexUser);
            TextView utente = (TextView) findViewById(R.id.txt_users);
            TextView email = (TextView) findViewById(R.id.emaillino);

            String fisrtName = ParseUser.getCurrentUser().get("fName").toString();
            String lastName = ParseUser.getCurrentUser().get("lName").toString();
            String email_users = ParseUser.getCurrentUser().get("email").toString();
            utente.setText(fisrtName + " " + lastName);
            email.setText(email_users);

            Title=fisrtName + " " + lastName;

            profile = (RoundedImageView) findViewById(R.id.user_photo);
            profile.setImageBitmap(GlobalVariable.UserPropic);

        }

        //recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_friends);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        mRecyclerView.setLayoutManager(mLayoutManager);

        //Util.getUserFacebookFriends(ParseUser.getCurrentUser());
        //set adapter to recycler
        ArrayList<Person> persons = new ArrayList<>();

        persons.add(new Person("Giovanni", R.drawable.giampa));
        persons.add(new Person("Francesco", R.drawable.starnino));
        persons.add(new Person("Vincenzo", R.drawable.vindel));
        persons.add(new Person("Federico", R.drawable.fedebyes));
        persons.add(new Person("Angelo", R.drawable.angelo));

        mAdapter = new PersonAdapter(persons);

        mRecyclerView.setAdapter(mAdapter);

        //nameProfile.setText(Title);
        if(getSupportActionBar()!=null){
            //getSupportActionBar().setTitle(Title);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_user);
        collapsingToolbarLayout.setTitle(Title);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent));
        // transperent color = #00000000
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.rgb(255, 255, 255));



        //floating button for report problems
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(UserProfileActivity.this)
                        .setTitleText("Hai qualcosa da segnalarci?")
                        .setContentText("Clicca sul pulsante posto in basso.")
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

        RelativeLayout condividi = (RelativeLayout) findViewById(R.id.cambiamelo);
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

        RelativeLayout cambia = (RelativeLayout) findViewById(R.id.inviaci_mail);
        cambia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_share = new Intent(UserProfileActivity.this, MainActivitySocialShare.class);
                startActivity(intent_share);
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}

