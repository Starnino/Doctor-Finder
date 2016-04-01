package com.doctorfinderapp.doctorfinder.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.adapter.FacebookAdapter;
import com.doctorfinderapp.doctorfinder.functions.FacebookProfile;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.doctorfinderapp.doctorfinder.functions.RoundedImageView;
import com.doctorfinderapp.doctorfinder.functions.Util;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    public final String NAME = "fName";
    public final String SURNAME = "lName";
    public final String FRIENDS = "friends";
    public final String FACEBOOK = "Facebook";
    public final String USER = "_User";
    public final String ID = "facebookId";
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
    private CardView card_friend;
    private List<ParseObject> friends;
    private ProgressWheel progress;
    private ParseUser user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!ParseFacebookUtils.isLinked(ParseUser.getCurrentUser()) && !GlobalVariable.FLAG_DIALOG) {
            SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText("Doctor Finder & Facebook")
                    .setContentText(getString(R.string.tip_dcf_user))
                    .setCustomImage(R.drawable.facebook_doctor_finder_icon);

            dialog.getProgressHelper().setRimColor(R.color.facebook_color);
            dialog.show();
            GlobalVariable.FLAG_DIALOG = true;
        }


        //scrolling
        setContentView(R.layout.activity_scrolling_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_user);
        setSupportActionBar(toolbar);

        user = ParseUser.getCurrentUser();

        fab_share = (com.melnykov.fab.FloatingActionButton) findViewById(R.id.fab_share);
        fab_share.setOnClickListener(this);

        friend_null = (TextView) findViewById(R.id.friend_null);
        card_friend = (CardView) findViewById(R.id.card_friends);

        progress = (ProgressWheel) findViewById(R.id.progress_friends);
        progress.setBarColor(getResources().getColor(R.color.colorPrimaryDark));
        progress.spin();

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

            if (ParseFacebookUtils.isLinked(ParseUser.getCurrentUser())) {

                //recycler view
                mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_friends);

                mRecyclerView.setHasFixedSize(true);

                mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

                mRecyclerView.setLayoutManager(mLayoutManager);

                //set adapter to recycler
                friends = new ArrayList<>();

                mAdapter = new FacebookAdapter(friends);

                /**update dinamically recycler view @fedebyes this is TOP*/
                new AsyncGetUserFriends().execute();

            } else  card_friend.setVisibility(View.GONE);



        if (getSupportActionBar() != null) {

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
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Ciao, prova questa nuova applicazione che ti permette di trovare il dottore perfetto per te");
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
                                             Util.sendFeedbackMail(UserProfileActivity.this);
                                         }
                                     }


        );


        RelativeLayout facebukkalo = (RelativeLayout) findViewById(R.id.facebook);
        if (ParseFacebookUtils.isLinked(user)) {
            facebukkalo.setVisibility(View.GONE);
        }
        facebukkalo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                if (!ParseFacebookUtils.isLinked(user)) {
                    ParseFacebookUtils.linkWithReadPermissionsInBackground(user, UserProfileActivity.this,
                            (Collection<String>) GlobalVariable.permissions,
                            new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    Log.d("Facebook link", e + "EXCEPTION");
                                    if (ParseFacebookUtils.isLinked(user)) {
                                        Snackbar.make(v, R.string.facebook_linked, Snackbar.LENGTH_SHORT)
                                                .setAction("Action", null).show();

                                        FacebookProfile.getGraphRequest(user);

                                        Log.d("MyApp", "Woohoo, user logged in with Facebook!");
                                    } else {
                                        Snackbar.make(v, R.string.error_facebook, Snackbar.LENGTH_SHORT)
                                                .setAction("Action", null).show();
                                    }
                                }
                            });

                } else {
                    Snackbar.make(v, R.string.facebook_linked, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }


            }
        });
    }


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
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Ciao, prova questa nuova applicazione che ti permette di trovare il dottore perfetto per te");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }



    public void getUserFacebookFriends(ParseUser user, final List<ParseObject> ArrayAdapterFriends) {
        List<String> id = new ArrayList<>();

        if (user == null) return;
        if (user.getString(FACEBOOK) != null)
            if (!user.getString(FACEBOOK).equals("true")) return;

        if (user.getString(FRIENDS) == null) return;
        if (user.getString(FACEBOOK).equals("true"))
            id = Arrays.asList(user.get(FRIENDS).toString().split(","));

        /*for (int i = 0; i < id.size(); i++) {
            Log.d("AMICO --> ", id.get(i));
        }*/

        ParseQuery<ParseObject> friendQuery = ParseQuery.getQuery(USER);

        friendQuery.whereContainedIn(ID, id);
        friendQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (int i = 0; i < objects.size(); i++)
                    ArrayAdapterFriends.add(objects.get(i));

                progress.setVisibility(View.GONE);

                //if friends.size() is not empty set height to dimen dp
                if (mAdapter.getItemCount() != 0) {

                    friend_null.setVisibility(View.GONE);
                    mRecyclerView.getLayoutParams().height =
                            (int) getResources().getDimension(R.dimen.doctor_item_height);
                }

                mRecyclerView.setAdapter(mAdapter);
            }
        });
    }

    class AsyncGetUserFriends extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            getUserFacebookFriends(user, friends);
            return null;
        }
    }
}

