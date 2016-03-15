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
import com.doctorfinderapp.doctorfinder.adapter.FacebookAdapter;
import com.doctorfinderapp.doctorfinder.adapter.PersonAdapter;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.doctorfinderapp.doctorfinder.functions.RoundedImageView;
import com.doctorfinderapp.doctorfinder.functions.Util;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;


public class DoctorFragment extends Fragment {

    private String TitoloDot;
    private String TAG = "DoctorFragment";
    private String DOCTOR_FIRST_NAME;
    private String DOCTOR_LAST_NAME;
    private String DOCTOR_EXPERIENCE;
    private ArrayList<String> DOCTOR_WORK_ARRAY;
    private ArrayList<String> DOCTOR_SPECIALIZATION_ARRAY;
    private ArrayList<String> DOCTOR_CITY_ARRAY;
    private String DOCTOR_EMAIL;
    private String DOCTOR_FEEDBACK;
    private boolean DOCTOR_SEX;
    private String DOCTOR_DESCRIPTION;
    private String DOCTOR_PHONE;
    private String DOCTOR_DATE;
    private String DOCTOR_PRICE;
    private ComponentName cn;
    private List<ParseObject> doctors;
    private Doctor currentDoctor;
    private double LAT;
    private double LONG;
    private static int index;
    public  GoogleMap googleMap;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FacebookAdapter mAdapter;
    private TextView suggest_null;

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
         ParseObject DOCTORTHIS = DoctorActivity.DOCTORTHIS;

        DOCTOR_FIRST_NAME = DOCTORTHIS.getString("FirstName");
        DOCTOR_LAST_NAME = DOCTORTHIS.getString("LastName");
        DOCTOR_EXPERIENCE = DOCTORTHIS.getString("Exp");
        DOCTOR_FEEDBACK = DOCTORTHIS.get("Feedback").toString();
        DOCTOR_SEX = DOCTORTHIS.getString("Sesso").equals("M");
        DOCTOR_DESCRIPTION = DOCTORTHIS.getString("Description");
        DOCTOR_WORK_ARRAY = (ArrayList<String>) DOCTORTHIS.get("Work");
        DOCTOR_EMAIL = DOCTORTHIS.getString("Email");
        DOCTOR_CITY_ARRAY = (ArrayList<String>) DOCTORTHIS.get("Province");
        DOCTOR_SPECIALIZATION_ARRAY = (ArrayList<String>) DOCTORTHIS.get("Specialization");
        DOCTOR_PHONE = DOCTORTHIS.getString("Cellphone");
        DOCTOR_PRICE = DOCTORTHIS.getString("Price");
        DOCTOR_DATE = DOCTORTHIS.getString("Visit");

        ArrayList<HashMap> position = (ArrayList<HashMap>) DOCTORTHIS.get("Marker");
        final String[][]latLong = Util.setPosition(position);
        RelativeLayout workMaps = (RelativeLayout) rootView.findViewById(R.id.Work);
        workMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LAT = Double.parseDouble(latLong[0][0]);
                LONG = Double.parseDouble(latLong[0][1]);
                openMaps(LAT, LONG);
            }
        });

        //getting data from xml
        TextView nameProfile = (TextView) rootView.findViewById(R.id.tvNumber1);
        TextView special = (TextView) rootView.findViewById(R.id.tvNumber2);
        TextView years = (TextView) rootView.findViewById(R.id.years);
        TextView workPlace = (TextView) rootView.findViewById(R.id.workPlace);
        TextView cityPlace = (TextView) rootView.findViewById(R.id.cityPlace);
        TextView info = (TextView) rootView.findViewById(R.id.doctor_info);
        RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBarDoctorProfile);
        TextView price = (TextView) rootView.findViewById(R.id.price_text);
        TextView visit = (TextView) rootView.findViewById(R.id.visit_date);
        TextView phone = (TextView) rootView.findViewById(R.id.phone_number);
        suggest_null = (TextView) rootView.findViewById(R.id.suggest_null);


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_friends2);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new FacebookAdapter(Util.getUserFacebookFriendsAndFeedback(ParseUser.getCurrentUser(), DOCTOR_EMAIL));

        int adapter_count = mAdapter.getItemCount();

        if (adapter_count != 0) {
            mRecyclerView.getLayoutParams().height = 300;
            if (adapter_count > 1)
                suggest_null.setText(adapter_count + " amici trovati!");
            else
                suggest_null.setText(adapter_count + " amico trovato!");
        }

        mRecyclerView.setAdapter(mAdapter);


        if (DOCTOR_SEX)
            TitoloDot="Dott. " + DOCTOR_FIRST_NAME + " " + DOCTOR_LAST_NAME;
        else
            TitoloDot="Dott.ssa " + DOCTOR_FIRST_NAME + " " + DOCTOR_LAST_NAME;
        nameProfile.setText(TitoloDot);

        years.setText(DOCTOR_EXPERIENCE);

        cityPlace.setText(Util.setCity(DOCTOR_CITY_ARRAY));
        workPlace.setText(Util.setCity(DOCTOR_WORK_ARRAY));
        price.setText(DOCTOR_PRICE);
        phone.setText(DOCTOR_PHONE);
        visit.setText(DOCTOR_DATE);
        info.setText(DOCTOR_DESCRIPTION);

        String text = "";

        for (int i = 0; i < DOCTOR_SPECIALIZATION_ARRAY.size(); i++) {
            text += DOCTOR_SPECIALIZATION_ARRAY.get(i);
        }

        special.setText(text);

        if(DOCTOR_FEEDBACK!=null){
            //Log.d("DoctorFragment","Feedback is "+DOCTOR_FEEDBACK.toString());
            ratingBar.setRating(Float.parseFloat(DOCTOR_FEEDBACK));
        }else{
            //Log.d("DoctorFragment","Feedback is null");
        }




        RelativeLayout feedback_button = (RelativeLayout) rootView.findViewById(R.id.feedback_relative);
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


    private void openMaps(double lat, double lng) {

        String uristring = "geo:" + lat + "," + lng+"?q="+lat+","+lng+"("+TitoloDot+")";
        //?q=<lat>,<long>(Label+Name)
        Log.d(TAG, uristring);
        Uri gmmIntentUri = Uri.parse(uristring);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    private void openWhatsapp(String number,String doctorname){
        Intent sendIntent = new Intent();

        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setPackage("com.whatsapp");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

}
