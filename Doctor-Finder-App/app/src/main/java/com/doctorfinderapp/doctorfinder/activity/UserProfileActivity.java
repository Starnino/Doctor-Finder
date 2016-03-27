package com.doctorfinderapp.doctorfinder.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.LayoutDirection;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.adapter.FacebookAdapter;
import com.doctorfinderapp.doctorfinder.functions.FacebookProfile;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.doctorfinderapp.doctorfinder.functions.RoundedImageView;
import com.doctorfinderapp.doctorfinder.functions.Util;
import com.melnykov.fab.ObservableScrollView;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Collection;


public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String NAME = "fName";
    public static final String SURNAME = "lName";
    private static Context c;
    private static com.melnykov.fab.FloatingActionButton fab_share;
    public final String USER_EMAIL = "email";
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FacebookAdapter mAdapter;
    private String Title = "";
    private RoundedImageView profile;
    private String firstName = "";
    private String lastName = "";
    private TextView utente;
    private TextView email;
    private TextView friend_null;
    private String email_users;

    public static void showToastFeedback() {
        Toast.makeText(c, R.string.feedback_visual,
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //scrolling
        setContentView(R.layout.activity_scrolling_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_user);
        setSupportActionBar(toolbar);

        final ParseUser user = ParseUser.getCurrentUser();

        fab_share= (com.melnykov.fab.FloatingActionButton) findViewById(R.id.fab_share);
        fab_share.setOnClickListener(this);

        friend_null = (TextView) findViewById(R.id.friend_null);

        if (user != null) {

            if (user.getString(NAME) != null)
                firstName = user.get(NAME).toString();

            if (user.getString(SURNAME) != null)
                lastName = user.get(SURNAME).toString();

            if (user.getString(USER_EMAIL) != null)
                email_users = user.get(USER_EMAIL).toString();

            utente = (TextView) findViewById(R.id.txt_users);
            email = (TextView) findViewById(R.id.emaillino);

            utente.setText(firstName + " " + lastName);
            email.setText(email_users);

            Title = firstName + " " + lastName;
            profile = (RoundedImageView) findViewById(R.id.user_photo);

            if (GlobalVariable.UserPropic != null)
                profile.setImageBitmap(GlobalVariable.UserPropic);

            Log.d("UTENTE --> ", firstName + ", " + lastName + ", " + email_users);
        }

        if (Util.isOnline(getApplicationContext())) {
            //recycler view
            mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_friends);

            mRecyclerView.setHasFixedSize(true);

            mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

            mRecyclerView.setLayoutManager(mLayoutManager);

            //set adapter to recycler

            mAdapter = new FacebookAdapter(Util.getUserFacebookFriends(user));

            if (mAdapter.getItemCount() != 0) friend_null.setVisibility(View.INVISIBLE);

            //if friends.size() is not empty set height to 100dp
            if (mAdapter.getItemCount() != 0) {
                mRecyclerView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.doctor_item_height);
                mRecyclerView.getLayoutParams().width  = (int) getResources().getDimension(R.dimen.doctor_item_width);
            }

            mRecyclerView.setAdapter(mAdapter);

        } else {
            friend_null.setText("C'è qualche problema. Assicurati che la tua connessione a Internet funzioni!");
            friend_null.setGravity(View.TEXT_ALIGNMENT_CENTER);
        }

        //nameProfile.setText(Title);
        if (getSupportActionBar() != null) {
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





        RelativeLayout condividi = (RelativeLayout) findViewById(R.id.condividi);
        condividi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, R.string.share_email);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        RelativeLayout rateus = (RelativeLayout) findViewById(R.id.rateus);
        rateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.doctorfinderapp.doctorfinder" + appPackageName)));
                }

            }
        });


        RelativeLayout inviacela = (RelativeLayout) findViewById(R.id.inviaci_mail);
        inviacela.setOnClickListener(new View.OnClickListener()

                                     {
                                         @Override
                                         public void onClick(View view) {
                                             Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                                     "mailto","info@doctorfinderapp.com", null));
                                             emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback app");
                                             emailIntent.putExtra(Intent.EXTRA_TEXT, "" +
                                                     " \n " +
                                                     " \n " +
                                                     " \n " +" \n " +
                                                     " \n " +

                                                     " \n " +
                                                     " \n " +
                                                     "Messaggio inviato tramite Doctor Finder ");
                                             startActivity(Intent.createChooser(emailIntent, "Invia mail"));

                                             // Verify that the intent will resolve to an activity
                                             if (emailIntent.resolveActivity(getPackageManager()) != null) {
                                                 //startActivity(emailIntent);
                                             }
                                         }
                                     }

        );


        RelativeLayout facebukkalo = (RelativeLayout) findViewById(R.id.facebook);
        facebukkalo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                if (!ParseFacebookUtils.isLinked(user)) {
                    ParseFacebookUtils.linkWithReadPermissionsInBackground(user, UserProfileActivity.this,
                            (Collection<String>) GlobalVariable.permissions,
                            new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    Log.d("Facebook link", e+"EXCEPTION");
                                    if (ParseFacebookUtils.isLinked(user)) {
                                        Snackbar.make(v, R.string.facebook_linked, Snackbar.LENGTH_SHORT)
                                                .setAction("Action", null).show();

                                        FacebookProfile.getGraphRequest(user);
                                        Log.d("MyApp", "Woohoo, user logged in with Facebook!");
                                    }else{
                                        Snackbar.make(v, R.string.error_facebook, Snackbar.LENGTH_SHORT)
                                                .setAction("Action", null).show();
                                    }
                                }
                            });

                }



                }});}










    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

            {
                super.onBackPressed();
                finish();
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {

            case R.id.fab_share:

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, R.string.share_email);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;

        }
    }

}

