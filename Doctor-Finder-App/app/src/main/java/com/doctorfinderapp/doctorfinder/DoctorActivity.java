package com.doctorfinderapp.doctorfinder;

import android.content.ComponentName;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.doctorfinderapp.doctorfinder.Class.Doctor;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class DoctorActivity extends AppCompatActivity {


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
    private ComponentName cn;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ParseObject> doctors;
    private Doctor currentDoctor;
    private String Title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

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
        DOCTOR_EXPERIENCE = DOCTORTHIS.getString("Exp");
        DOCTOR_FEEDBACK = DOCTORTHIS.getString("Feedback");
        DOCTOR_SEX = DOCTORTHIS.getString("Sesso").equals("M");
        DOCTOR_DESCRIPTION = DOCTORTHIS.getString("Description");
        DOCTOR_WORK = (ArrayList<String>) DOCTORTHIS.get("Work");
        DOCTOR_CITY_ARRAY = (ArrayList<String>) DOCTORTHIS.get("Province");
        DOCTOR_SPECIALIZATION_ARRAY = (ArrayList<String>) DOCTORTHIS.get("Specialization");


        //getting data from xml
        TextView nameProfile = (TextView) findViewById(R.id.tvNumber1);
        TextView special = (TextView) findViewById(R.id.tvNumber2);
        TextView years = (TextView) findViewById(R.id.years);
        TextView workPlace = (TextView) findViewById(R.id.workPlace);
        TextView info = (TextView) findViewById(R.id.doctor_info);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBarDoctorProfile);

        /**refresh recentDoctors*/                                   //doctor_rounded_avatar
        currentDoctor = new Doctor(DOCTOR_FIRST_NAME,DOCTOR_LAST_NAME, R.drawable.doctor_rounded_avatar,
                DOCTOR_SPECIALIZATION_ARRAY, DOCTOR_CITY_ARRAY, DOCTOR_SEX);
        refreshDoctorList(currentDoctor);
        /**updated recent_doctor*/


        //mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_friends2);





        setContentView(R.layout.activity_doctor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_doctor);
        setSupportActionBar(toolbar);

        if(DOCTOR_SEX)
            Title="Dott. " + DOCTOR_FIRST_NAME + " " + DOCTOR_LAST_NAME;
        else
           Title="Dott.ssa "+ DOCTOR_FIRST_NAME+ " " + DOCTOR_LAST_NAME;

        nameProfile.setText(Title);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle(Title);
        }






        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_doctor);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });




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

}
