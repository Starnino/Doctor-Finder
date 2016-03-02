package com.doctorfinderapp.doctorfinder;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.doctorfinderapp.doctorfinder.Class.Doctor;
import com.doctorfinderapp.doctorfinder.Class.Person;
import com.doctorfinderapp.doctorfinder.adapter.PersonAdapter;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.doctorfinderapp.doctorfinder.functions.Util;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class DoctorProfileActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PersonAdapter mAdapter;
    private List<Person> persons;
    private ImageButton feedButton;
    private List<ParseObject> doctors;
    private Doctor currentDoctor;

    //Doctor information
    private int index;
    private String DOCTOR_FIRST_NAME;
    private String DOCTOR_LAST_NAME;
    private String DOCTOR_EXPERIENCE;
    private ArrayList<String> DOCTOR_WORK;
    private ArrayList<String> DOCTOR_SPECIALIZATION_ARRAY;
    private ArrayList<String> DOCTOR_CITY_ARRAY;
    private String DOCTOR_FEEDBACK;
    private boolean DOCTOR_SEX;
    private String DOCTOR_DESCRIPTION;
    private int DOCTOR_PHOTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //retrieve index from Activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            index = extras.getInt("index");
        }

        //set status bar color because in xml don't work
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        setContentView(R.layout.activity_scrolling);

        doctors = GlobalVariable.DOCTORS;

        //set ParseDoctor this
        ParseObject DOCTORTHIS = doctors.get(index);
        DOCTOR_FIRST_NAME = DOCTORTHIS.getString("FirstName");              Log.d("DOCTORTHIS", DOCTOR_FIRST_NAME);
        DOCTOR_LAST_NAME = DOCTORTHIS.getString("LastName");                Log.d("DOCTORTHIS", DOCTOR_LAST_NAME);
        DOCTOR_EXPERIENCE = DOCTORTHIS.getString("Exp");                    Log.d("DOCTORTHIS", DOCTOR_EXPERIENCE);
        DOCTOR_FEEDBACK = DOCTORTHIS.getString("Feedback");                 Log.d("DOCTORTHIS", DOCTOR_FEEDBACK);
        DOCTOR_SEX = (DOCTORTHIS.getString("Sesso") == "M");                Log.d("DOCTORTHIS", DOCTOR_SEX + "");
        DOCTOR_DESCRIPTION = DOCTORTHIS.getString("Description");           Log.d("DOCTORTHIS", DOCTOR_DESCRIPTION);
        DOCTOR_WORK = (ArrayList<String>) DOCTORTHIS.get("Work");                               Log.d("DOCTORTHIS",DOCTOR_WORK.get(0));
        DOCTOR_CITY_ARRAY = (ArrayList<String>) DOCTORTHIS.get("Province");                     Log.d("DOCTORTHIS", DOCTOR_CITY_ARRAY.get(0));
        DOCTOR_SPECIALIZATION_ARRAY = (ArrayList<String>) DOCTORTHIS.get("Specialization");     Log.d("DOCTORTHIS", DOCTOR_SPECIALIZATION_ARRAY.get(0));

        //DOCTOR_PHOTO == ..

        /**refresh recentDoctors*/                                                //doctor_rounded_avatar
        currentDoctor = new Doctor(DOCTOR_FIRST_NAME,DOCTOR_LAST_NAME, R.drawable.giampa,
                DOCTOR_SPECIALIZATION_ARRAY, DOCTOR_CITY_ARRAY, DOCTOR_SEX);
        if (GlobalVariable.recentDoctors.size() < 10 && !GlobalVariable.recentDoctors.contains(currentDoctor))
            GlobalVariable.recentDoctors.add(currentDoctor);

        else {
            if (!GlobalVariable.recentDoctors.contains(currentDoctor)) {
                GlobalVariable.recentDoctors.add(0, currentDoctor);
                GlobalVariable.recentDoctors.remove(10);
            }
        }
        /**updated recent_doctor*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_friends);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //getting data from xml
        TextView nameProfile = (TextView) findViewById(R.id.tvNumber1);
        TextView special = (TextView) findViewById(R.id.tvNumber2);
        TextView years = (TextView) findViewById(R.id.years);
        TextView workPlace = (TextView) findViewById(R.id.workPlace);
        TextView info = (TextView) findViewById(R.id.doctor_info);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBarDoctorProfile);
        //TextView province = (TextView) findViewById(R.id.province);

        //setting data from objects
        if(DOCTOR_SEX)
            nameProfile.setText("Dott. " + DOCTOR_FIRST_NAME + " " + DOCTOR_LAST_NAME);
        else
            nameProfile.setText("Dott.ssa "+ DOCTOR_FIRST_NAME+ " " + DOCTOR_LAST_NAME);


        years.setText(DOCTOR_EXPERIENCE);

        workPlace.setText(Util.setCity(DOCTOR_WORK));

        info.setText(DOCTOR_DESCRIPTION);

        special.setText(Util.setSpecialization(DOCTOR_SPECIALIZATION_ARRAY));

        ratingBar.setRating(Float.parseFloat(DOCTOR_FEEDBACK));

        //province.setText(Util.setCity(DOCTOR_CITY_ARRAY));

        //initialize more Persons
        persons = new ArrayList<>();
        persons.add(new Person("Francesco", R.drawable.starnino));
        persons.add(new Person("Federico", R.drawable.fedebyes));
        persons.add(new Person("Giovanni", R.drawable.giampa));
        persons.add(new Person("Vincenzo", R.drawable.vindel));
        persons.add(new Person("Chiara", R.drawable.chiara));
        persons.add(new Person("Angelo", R.drawable.angelo));
        persons.add(new Person("Jessica", R.drawable.jessica));
        persons.add(new Person("Giampo", R.drawable.p1));

        // specify an adapter
        mAdapter = new PersonAdapter(persons);

        //set adapter to recycler view
        mRecyclerView.setAdapter(mAdapter);

        feedButton = (ImageButton) findViewById(R.id.feedback_button);
        feedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo show fragment not activity
                Intent intent = new Intent(DoctorProfileActivity.this,
                        FeedbackItemActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Invia una mail a Dottore", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        });
    }

    @Override
    public void onBackPressed() {
        super.finish();
        super.onBackPressed();
    }
}
