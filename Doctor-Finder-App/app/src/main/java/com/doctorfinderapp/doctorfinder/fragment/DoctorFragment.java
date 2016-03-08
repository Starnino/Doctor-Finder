package com.doctorfinderapp.doctorfinder.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doctorfinderapp.doctorfinder.Class.Doctor;
import com.doctorfinderapp.doctorfinder.Class.Person;
import com.doctorfinderapp.doctorfinder.DoctorActivity;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.adapter.PersonAdapter;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.doctorfinderapp.doctorfinder.functions.Util;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Fedebyes
 */
public class DoctorFragment extends Fragment {

    private String TitoloDot;
    private String TAG = "DoctorFragment";
    private String DOCTOR_FIRST_NAME;
    private String DOCTOR_LAST_NAME;
    private String DOCTOR_EXPERIENCE;
    private ArrayList<String> DOCTOR_WORK_ARRAY;
    private ArrayList<String> DOCTOR_SPECIALIZATION_ARRAY;
    private ArrayList<String> DOCTOR_CITY_ARRAY;
    private JSONArray DOCTOR_MARKER_ARRAY;
    private String DOCTOR_FEEDBACK;
    private boolean DOCTOR_SEX;
    private String DOCTOR_DESCRIPTION;
    private int DOCTOR_PHOTO;
    private ComponentName cn;
    private List<ParseObject> doctors;
    private Doctor currentDoctor;
    private double LAT;
    private double LONG;
    private static int index;
    public  GoogleMap googleMap;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PersonAdapter mAdapter;

    public DoctorFragment() {
    }

    public static DoctorFragment newInstance(int index) {
        DoctorFragment doc = new DoctorFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);

        doc.setArguments(args);
        return doc;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int indexFragment = getArguments().getInt("index", 0);
        index = indexFragment;

        DoctorActivity.switchFAB(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_doctor,
                container, false);

        //set ParseDoctor this
        doctors = GlobalVariable.DOCTORS;
        ParseObject DOCTORTHIS = doctors.get(index);


        DOCTOR_FIRST_NAME = DOCTORTHIS.getString("FirstName");
        DOCTOR_LAST_NAME = DOCTORTHIS.getString("LastName");
        DOCTOR_EXPERIENCE = DOCTORTHIS.getString("Exp");
        DOCTOR_FEEDBACK = DOCTORTHIS.getString("Feedback");
        DOCTOR_SEX = DOCTORTHIS.getString("Sesso").equals("M");
        DOCTOR_DESCRIPTION = DOCTORTHIS.getString("Description");
        DOCTOR_WORK_ARRAY = (ArrayList<String>) DOCTORTHIS.get("Work");
        DOCTOR_CITY_ARRAY = (ArrayList<String>) DOCTORTHIS.get("Province");
        DOCTOR_SPECIALIZATION_ARRAY = (ArrayList<String>) DOCTORTHIS.get("Specialization");

        /*DOCTOR_MARKER_ARRAY= (JSONArray) DOCTORTHIS.get("Marker");



        try {
            JSONObject marker= (JSONObject) DOCTOR_MARKER_ARRAY.get(0);
            LAT = Double.parseDouble((String) marker.get("Lat"));
            LONG=Double.parseDouble((String) marker.get("Long"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/
        String curPosition = DOCTORTHIS.get("Marker").toString();


        /*DONT USE SUBSTRING IS NOT JSON*/

        //LAT = Double.parseDouble(curPosition.substring(6, 15));
        //LONG = Double.parseDouble(curPosition.substring(22, 31));

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_friends2);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<Person> persons = new ArrayList<>();

        persons.add(new Person("Giovanni", R.drawable.giampa));
        persons.add(new Person("Francesco", R.drawable.starnino));
        persons.add(new Person("Vincenzo", R.drawable.vindel));
        persons.add(new Person("Federico", R.drawable.fedebyes));
        persons.add(new Person("Angelo", R.drawable.angelo));

        mAdapter = new PersonAdapter(persons);

        mRecyclerView.setAdapter(mAdapter);

        //getting data from xml
        TextView nameProfile = (TextView) rootView.findViewById(R.id.tvNumber1);
        TextView special = (TextView) rootView.findViewById(R.id.tvNumber2);
        TextView years = (TextView) rootView.findViewById(R.id.years);
        TextView workPlace = (TextView) rootView.findViewById(R.id.workPlace);
        TextView cityPlace = (TextView) rootView.findViewById(R.id.cityPlace);
        TextView info = (TextView) rootView.findViewById(R.id.doctor_info);
        RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBarDoctorProfile);


        if (DOCTOR_SEX)
            TitoloDot="Dott. " + DOCTOR_FIRST_NAME + " " + DOCTOR_LAST_NAME;
        else
            TitoloDot="Dott.ssa " + DOCTOR_FIRST_NAME + " " + DOCTOR_LAST_NAME;
        nameProfile.setText(TitoloDot);

        years.setText(DOCTOR_EXPERIENCE);

        cityPlace.setText(Util.setCity(DOCTOR_CITY_ARRAY));
        workPlace.setText(Util.setCity(DOCTOR_WORK_ARRAY));


        info.setText(DOCTOR_DESCRIPTION);

        special.setText(Util.setSpecialization(DOCTOR_SPECIALIZATION_ARRAY));

        ratingBar.setRating(Float.parseFloat(DOCTOR_FEEDBACK));


       /* RelativeLayout workMaps = (RelativeLayout) rootView.findViewById(R.id.Work);
        workMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMaps(LAT, LONG);
            }
        });*/


        /**refresh recentDoctors*/                                   //doctor_rounded_avatar
        currentDoctor = new Doctor(DOCTOR_FIRST_NAME, DOCTOR_LAST_NAME, R.drawable.doctor_avatar,
                DOCTOR_SPECIALIZATION_ARRAY, DOCTOR_CITY_ARRAY, DOCTOR_SEX);
        refreshDoctorList(currentDoctor);
        /**updated recent_doctor*/




        LinearLayout feedback_button = (LinearLayout) rootView.findViewById(R.id.feedback_relative);
        feedback_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                FeedbackFragment fragment = new FeedbackFragment().newInstance(index);

                ft.replace(R.id.frame_doctor, fragment);
                ft.addToBackStack(null);
                ft.commit();



            }
        });




        return rootView;
    }


    public void refreshDoctorList(Doctor currentDoctor) {
        //set visible flag
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

    private void openMaps(double lat, double lng) {
        // Creates an Intent that will load a map of San Francisco
        String uristring = "geo:" + lat + "," + lng+"?q="+lat+","+lng+"("+TitoloDot+")";
        //?q=<lat>,<long>(Label+Name)
        Log.d(TAG, uristring);
        Uri gmmIntentUri = Uri.parse(uristring);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

}
