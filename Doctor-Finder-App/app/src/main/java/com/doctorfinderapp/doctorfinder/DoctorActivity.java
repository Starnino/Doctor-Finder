package com.doctorfinderapp.doctorfinder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.doctorfinderapp.doctorfinder.Class.Doctor;
import com.doctorfinderapp.doctorfinder.fragment.DoctorFragment;
import com.doctorfinderapp.doctorfinder.fragment.FeedbackDialogFragment;
import com.doctorfinderapp.doctorfinder.fragment.FeedbackFragment;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.doctorfinderapp.doctorfinder.functions.RoundedImageView;
import com.github.clans.fab.FloatingActionMenu;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class DoctorActivity extends AppCompatActivity implements View.OnClickListener, FeedbackFragment.OnFragmentInteractionListener, FragmentManager.OnBackStackChangedListener {


    public static ParseObject DOCTORTHIS;
    //Doctor information
    private static int index;
    private static boolean isFabOpen = false;
    private static com.melnykov.fab.FloatingActionButton fabfeedback;
    private static FloatingActionMenu fabmenu;
    private com.github.clans.fab.FloatingActionButton fab_email, fab_whastapp, fab_telegram;
    private static Context c;
    public final String EMAIL = "Email";
    private String DOCTOR_EMAIL = "";
    private boolean DOCTOR_SEX;
    private String DOCTOR_FIRST_NAME;
    private String DOCTOR_LAST_NAME;
    private ArrayList<String> DOCTOR_CITY_ARRAY;
    private ArrayList<String> DOCTOR_SPECIALIZATION_ARRAY;
    private Bitmap DOCTOR_PHOTO;
    private List<ParseObject> doctors;
    private Doctor currentDoctor;
    private String Title;
    private String email;
    private  static FragmentManager p;

    public static void showToastFeedback() {
        Toast.makeText(c, R.string.feedback_sended,
                Toast.LENGTH_LONG).show();
        FragmentTransaction ft = p.beginTransaction();
        FeedbackFragment fragment = new FeedbackFragment().newInstance(index);

        ft.replace(R.id.frame_doctor, fragment);

        ft.commit();
    }

    //switch fab
    public static void switchFAB(int position) {
        switch (position) {
            case 0:
                if (fabfeedback.isVisible()){
                    fabfeedback.hide();
                }
                fabmenu.showMenu(true);

                break;

            case 1:
                fabmenu.hideMenu(true);
                fabfeedback.setVisibility(View.VISIBLE);
                fabfeedback.show();

                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        c = getApplicationContext();
        setContentView(R.layout.activity_doctor);
        //take index
        Bundle extras = getIntent().getExtras();

        doctors = GlobalVariable.DOCTORS;

        //set ParseDoctor this
        if (extras != null) {

            email = extras.getString("email");
            index = extras.getInt("index");
            //Log.d("EXTRAS ===> ", "email: " + email + " " + "index: " + index);
        }

        if (email == null) {
            DOCTORTHIS = doctors.get(index);
        } else {
            ParseQuery doctorQuery = ParseQuery.getQuery("Doctor");
            doctorQuery.whereEqualTo(EMAIL, email);
            try {
                DOCTORTHIS = doctorQuery.getFirst();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        doctors = GlobalVariable.DOCTORS;

        DOCTOR_FIRST_NAME = DOCTORTHIS.getString("FirstName");
        DOCTOR_LAST_NAME = DOCTORTHIS.getString("LastName");
        DOCTOR_SEX = DOCTORTHIS.getString("Sesso").equals("M");
        DOCTOR_CITY_ARRAY = (ArrayList<String>) DOCTORTHIS.get("Province");
        DOCTOR_SPECIALIZATION_ARRAY = (ArrayList<String>) DOCTORTHIS.get("Specialization");
        DOCTOR_EMAIL = DOCTORTHIS.getString(EMAIL);

        final RoundedImageView photoProfile = (RoundedImageView) findViewById(R.id.doctor_propic);

        final ParseQuery<ParseObject> doctorph = ParseQuery.getQuery("DoctorPhoto");
        doctorph.whereEqualTo(EMAIL, DOCTOR_EMAIL);

        doctorph.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject doctorPhoto, ParseException e) {

                if (doctorPhoto == null)
                    Log.d("doctorphoto", DOCTOR_EMAIL + " isNull");

                else {

                    ParseFile file = (ParseFile) doctorPhoto.get("profilePhoto");
                    if (e == null) {

                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                DOCTOR_PHOTO = BitmapFactory.decodeByteArray(data, 0, data.length);
                                Log.d("DOCTOR PHOTO --> ", DOCTOR_PHOTO == null ? "is null" : "ok");
                                photoProfile.setImageBitmap(DOCTOR_PHOTO);
                            }
                        });
                    }
                }
            }
        });

        //if photo exist
        if (DOCTOR_PHOTO != null)
            currentDoctor = new Doctor(DOCTOR_FIRST_NAME, DOCTOR_LAST_NAME,
                    DOCTOR_SPECIALIZATION_ARRAY, DOCTOR_CITY_ARRAY, DOCTOR_SEX, DOCTOR_EMAIL);
            //if photo not exist
        else
            currentDoctor = new Doctor(DOCTOR_FIRST_NAME, DOCTOR_LAST_NAME,
                    DOCTOR_SPECIALIZATION_ARRAY, DOCTOR_CITY_ARRAY, DOCTOR_SEX, DOCTOR_EMAIL);
        p = getSupportFragmentManager();

        ParseObject doctor = new ParseObject("recentDoctor");
        doctor.put("FN", DOCTOR_FIRST_NAME);
        doctor.put("LN", DOCTOR_LAST_NAME);
        doctor.put("E@", DOCTOR_EMAIL);
        doctor.put("SPEC", DOCTOR_SPECIALIZATION_ARRAY);
        doctor.put("CITY", DOCTOR_CITY_ARRAY);
        doctor.put("SEX", DOCTOR_SEX);
        //doctor.pinInBackground();

        //refresh doctors searched
        /**REMOVE refreshDoctorList(currentDoctor); */

        //find fab buttons
        fabfeedback = (com.melnykov.fab.FloatingActionButton) findViewById(R.id.fabfeedback);
        fabmenu = (FloatingActionMenu) findViewById(R.id.fab_menu);
        fab_email = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_email);
        fab_whastapp = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_whastapp);
        fab_telegram = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_telegram);

        //onClick button
        fabfeedback.setOnClickListener(this);
        fab_email.setOnClickListener(this);
        fab_whastapp.setOnClickListener(this);
        fab_telegram.setOnClickListener(this);

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_doctor);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        if (DOCTOR_SEX)
            Title = "Dott. " + DOCTOR_FIRST_NAME + " " + DOCTOR_LAST_NAME;
        else
            Title = "Dott.ssa " + DOCTOR_FIRST_NAME + " " + DOCTOR_LAST_NAME;
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_doc);
        collapsingToolbarLayout.setTitle(Title);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent));
        //collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.docfinder));
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.rgb(255,255,255));

    }

    public void refreshDoctorList(Doctor currentDoctor) {
        //set flag
        if (!GlobalVariable.FLAG_CARD_DOCTOR_VISIBLE)
            GlobalVariable.FLAG_CARD_DOCTOR_VISIBLE = true;
        boolean flag = true;
        //if doctor not exist in list
        for (int i = 0; i < GlobalVariable.recentDoctors.size(); i++) {
            if ((GlobalVariable.recentDoctors.get(i).getName() + GlobalVariable.recentDoctors.get(i).getSurname())
                    .equals(currentDoctor.getName() + currentDoctor.getSurname()))
                flag = false;
        }

        if (flag) {

            //if size of list is minor of 10
            if (GlobalVariable.recentDoctors.size() < 10)
                GlobalVariable.recentDoctors.add(currentDoctor);

                //if size is 10 or plus
            else {
                GlobalVariable.recentDoctors.add(0, currentDoctor);
                GlobalVariable.recentDoctors.remove(10);
            }
        }
        flag = true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                } else {
                    super.onBackPressed();
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {

            case R.id.fabfeedback:
                openFeedbackDialog();
                break;

        }
    }

    private void openFeedbackDialog() {


        //Log.d("DoctorActivity",DOCTOR_EMAIL.toString());
        DialogFragment newFragment = new FeedbackDialogFragment().newInstance(DOCTOR_EMAIL);
        newFragment.show(getSupportFragmentManager(), "feedback");

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        Log.d("Doctor Activity", " On back pressed");
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Log.d("Doctor Activity", "" + getSupportFragmentManager().getBackStackEntryCount());
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public void onBackStackChanged() {
        Log.d("Doctor Activity", " On back stqck changed");
        shouldDisplayHomeUp();
    }

    public void shouldDisplayHomeUp() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Log.d("Doctor Activity", "" + getSupportFragmentManager().getBackStackEntryCount());
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Log.d("Doctor Activity", "" + getSupportFragmentManager().getBackStackEntryCount());
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
            finish();
        }
        return true;
    }


}