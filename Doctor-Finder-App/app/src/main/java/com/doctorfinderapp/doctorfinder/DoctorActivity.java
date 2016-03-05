package com.doctorfinderapp.doctorfinder;

import android.content.ComponentName;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RatingBar;
import android.widget.TextView;

import com.doctorfinderapp.doctorfinder.Class.Doctor;
import com.doctorfinderapp.doctorfinder.fragment.DoctorFragment;
import com.doctorfinderapp.doctorfinder.fragment.FeedbackFragment;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class DoctorActivity extends AppCompatActivity implements View.OnClickListener,FeedbackFragment.OnFragmentInteractionListener,FragmentManager.OnBackStackChangedListener {


    //Doctor information
    private static int index;
    private boolean DOCTOR_SEX;

    private String DOCTOR_FIRST_NAME;
    private String DOCTOR_LAST_NAME;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ParseObject> doctors;
    private Doctor currentDoctor;
    private String Title;
    private boolean isFabOpen = false;
    private FloatingActionButton fabcontact,fabfeedback,fabemail, fabtelephone;
    private Animation fab_open_normal,fab_open,fab_close,rotate_forward,rotate_backward;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        //take index
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            index = extras.getInt("index");
        }
        doctors = GlobalVariable.DOCTORS;


        //set ParseDoctor this
        ParseObject DOCTORTHIS = doctors.get(index);
        DOCTOR_FIRST_NAME = DOCTORTHIS.getString("FirstName");
        DOCTOR_LAST_NAME = DOCTORTHIS.getString("LastName");


        //find fab buttons
        fabcontact = (FloatingActionButton)findViewById(R.id.fabcontact);
        fabfeedback= (FloatingActionButton)findViewById(R.id.fabfeedback);
        fabtelephone = (FloatingActionButton)findViewById(R.id.fabtelephone);
        fabemail = (FloatingActionButton)findViewById(R.id.fabemail);

        //load animation
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        fab_open_normal = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open_normal);

        //onClick button
        fabcontact.setOnClickListener(this);
        fabfeedback.setOnClickListener(this);
        fabtelephone.setOnClickListener(this);
        fabemail.setOnClickListener(this);



        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        //add parameters
        DoctorFragment doctorFragment = DoctorFragment.newInstance(index);
        ft.replace(R.id.frame_doctor, doctorFragment);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        //ft.addToBackStack(null);
        ft.commit();

        fabcontact.startAnimation(fab_open_normal);



        //getting data from xml
        TextView nameProfile = (TextView) findViewById(R.id.tvNumber1);
        TextView special = (TextView) findViewById(R.id.tvNumber2);
        TextView years = (TextView) findViewById(R.id.years);
        TextView workPlace = (TextView) findViewById(R.id.workPlace);
        TextView info = (TextView) findViewById(R.id.doctor_info);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBarDoctorProfile);




        //mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_friends2);




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_doctor);
        setSupportActionBar(toolbar);

        DOCTOR_SEX = DOCTORTHIS.getString("Sesso").equals("M");
        if(DOCTOR_SEX)
            Title="Dott. " + DOCTOR_FIRST_NAME + " " + DOCTOR_LAST_NAME;
        else
           Title="Dott.ssa "+ DOCTOR_FIRST_NAME+ " " + DOCTOR_LAST_NAME;

        //nameProfile.setText(Title);
        if(getSupportActionBar()!=null){
            //getSupportActionBar().setTitle(Title);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
        final CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_doc);
        collapsingToolbarLayout.setTitle(Title);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent));
        // transperent color = #00000000
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.rgb(255, 255, 255));











    }

    public void refreshDoctorList(Doctor currentDoctor){
        //set flag
        if (!GlobalVariable.FLAG_CARD_DOCTOR_VISIBLE) GlobalVariable.FLAG_CARD_DOCTOR_VISIBLE = true;
        boolean flag = true;
        //if doctor not exist in list
        for (int i = 0; i < GlobalVariable.recentDoctors.size(); i++) {
            if ((GlobalVariable.recentDoctors.get(i).getName()+GlobalVariable.recentDoctors.get(i).getSurname())
                    .equals(currentDoctor.getName()+currentDoctor.getSurname()))
                flag = false;
        }

        if (flag){

            //if size of list is minor of 10
            if (GlobalVariable.recentDoctors.size() < 10)
                GlobalVariable.recentDoctors.add(currentDoctor);

                //if size is 10 or plus
            else {
                GlobalVariable.recentDoctors.add(0, currentDoctor);
                GlobalVariable.recentDoctors.remove(10);
            }
        } flag = true;
    }
    @Override
    public boolean onSupportNavigateUp(){
        /*FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
            return true;
        }else {
            super.finish();
            return true;
        }*/
        getSupportFragmentManager().popBackStack();
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //animation fab buttons
    public void animateFAB() {

        if (isFabOpen) {

            fabcontact.startAnimation(rotate_backward);
            fabemail.startAnimation(fab_close);
            fabtelephone.startAnimation(fab_close);
            fabemail.setClickable(false);
            fabtelephone.setClickable(false);
            isFabOpen = false;
            Log.d("button", "close");

        } else {

            fabcontact.startAnimation(rotate_forward);
            fabemail.startAnimation(fab_open);
            fabtelephone.startAnimation(fab_open);
            fabemail.setClickable(true);
            fabtelephone.setClickable(true);
            isFabOpen = true;
            Log.d("button", "open");
        }
    }

    //switch fab
    public void switchFAB(int position){
        switch(position){
            case 0:
                if (isFabOpen) {
                    Log.d("fab", "open");
                    fabfeedback.startAnimation(fab_close);
                    fabfeedback.setClickable(false);
                    isFabOpen = false;
                }
                fabcontact.startAnimation(fab_open_normal);
                fabcontact.setClickable(true);
                break;
            case 1:
                Log.d("fab_location", "open");
                fabcontact.startAnimation(fab_close);
                fabcontact.setClickable(false);
                fabfeedback.startAnimation(fab_open_normal);
                fabfeedback.setClickable(true);
                break;
        }
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch(id){
            case R.id.fabcontact:
                animateFAB();
                break;

            case R.id.fabfeedback:
                openFeedbackDialog();
                break;

            case R.id.fabemail:
                //TODO
                break;

            case R.id.fabtelephone:
                //TODO
                break;
        }
    }

    private void openFeedbackDialog(){

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }
    public void shouldDisplayHomeUp(){
        //Enable Up button only  if there are entries in the back stack
        boolean canback = getSupportFragmentManager().getBackStackEntryCount()>0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canback);
    }
}
