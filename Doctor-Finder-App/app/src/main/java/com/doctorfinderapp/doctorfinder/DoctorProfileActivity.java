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
import android.widget.TextView;

import com.doctorfinderapp.doctorfinder.Class.Person;
import com.doctorfinderapp.doctorfinder.adapter.PersonAdapter;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class DoctorProfileActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PersonAdapter mAdapter;
    private List<Person> persons;

    private ImageButton feedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set status bar color because in xml don't work
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        setContentView(R.layout.activity_scrolling);
        //get parameters from activity
        Bundle b = getIntent().getExtras();


        String id = GlobalVariable.idDocotrs;


        ParseObject doctor = new ParseObject("Doctor");

        for(int i=0;i< MainActivity.DOCTORSMAIN.size();i++){
            if(MainActivity.DOCTORSMAIN.get(i).getObjectId()==id){
                Log.d("Doctor","object id "+ MainActivity.DOCTORSMAIN.get(i).getObjectId());
                Log.d("Doctor","id "+id);
                doctor= MainActivity.DOCTORSMAIN.get(i);
            }
        }




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_friends);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);



        TextView nameProfile = (TextView) findViewById(R.id.tvNumber1);
        TextView special = (TextView) findViewById(R.id.tvNumber2);
        TextView years = (TextView) findViewById(R.id.years);
        TextView workPlace = (TextView) findViewById(R.id.workPlace);
        //TextView feedback = (TextView) findViewById(R.id.tvNumber5);
        TextView info = (TextView) findViewById(R.id.doctor_info);

        nameProfile.setText("Martina Tritto");
        special.setText("Oculista");
        years.setText("7");
        workPlace.setText("Via M. Prestinari, 17");
        //name.setText("Martina Tritto");
        info.setText("https://it.linkedin.com/in/martTritto1");

        String nameString = doctor.getString("FirstName");
        Log.d("Doctor", "showing profile of " + nameString + id);
        //name.setText(nameString);
//        //String specialString = doctor.getList("Specialization").subList(0,1).toString();
        //special.setText(specialString.substring(1, specialString.length() - 1));


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
}
