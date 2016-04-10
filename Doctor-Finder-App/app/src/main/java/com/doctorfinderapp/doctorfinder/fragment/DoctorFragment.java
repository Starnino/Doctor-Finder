package com.doctorfinderapp.doctorfinder.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctorfinderapp.doctorfinder.activity.DoctorActivity;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.activity.MainActivity;
import com.doctorfinderapp.doctorfinder.activity.access.FirstActivity;
import com.doctorfinderapp.doctorfinder.adapter.FacebookAdapter;
import com.doctorfinderapp.doctorfinder.functions.Util;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.pnikosis.materialishprogress.ProgressWheel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class DoctorFragment extends Fragment {

    public final String USER = "_User";
    public final String FRIENDS = "friends";
    public final String FACEBOOK = "Facebook";
    public final String ID = "facebookId";
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
    private List<ParseObject>  friends_tip;
    private double LAT;
    private double LONG;
    private static int index;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FacebookAdapter mAdapter;
    private TextView suggest_null;
    private ImageView facebook_tip;
    private Context c;
    private ProgressWheel progress_tip;
    private RatingBar ratingBar;
    private static float rightRating = 0;

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
        c = getContext();

        progress_tip = (ProgressWheel) rootView.findViewById(R.id.progress_tip);
        progress_tip.setBarColor(getResources().getColor(R.color.colorPrimaryDark));
        progress_tip.spin();

        if (ParseUser.getCurrentUser() == null || !ParseFacebookUtils.isLinked(ParseUser.getCurrentUser())) {
            progress_tip.stopSpinning();
            CardView tip = (CardView) rootView.findViewById(R.id.card_tip);
            tip.setVisibility(View.GONE);
        }

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
        TextView special = (TextView) rootView.findViewById(R.id.specializations);
        TextView years = (TextView) rootView.findViewById(R.id.years);
        TextView workPlace = (TextView) rootView.findViewById(R.id.workPlace);
        TextView cityPlace = (TextView) rootView.findViewById(R.id.cityPlace);
        TextView info = (TextView) rootView.findViewById(R.id.doctor_info);
        ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBarDoctorProfile);
        TextView price = (TextView) rootView.findViewById(R.id.price_text);
        TextView visit = (TextView) rootView.findViewById(R.id.visit_date);
        TextView phone = (TextView) rootView.findViewById(R.id.phone_number);
        suggest_null = (TextView) rootView.findViewById(R.id.suggest_null);
        facebook_tip = (ImageView) rootView.findViewById(R.id.icon_facebook_tip);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_friends2);

        if (ParseUser.getCurrentUser() != null) {

            if (ParseFacebookUtils.isLinked(ParseUser.getCurrentUser())) {

                mRecyclerView.setHasFixedSize(true);

                mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

                mRecyclerView.setLayoutManager(mLayoutManager);

                friends_tip = new ArrayList<>();

                final ParseUser user = ParseUser.getCurrentUser();

                if (user != null && user.getString(FACEBOOK) != null && user.getString(FACEBOOK).equals("true")) {

                    //get user friends
                    List<String> id = Arrays.asList(user.get(FRIENDS).toString().split(","));

                    ParseQuery<ParseObject> friendQuery = ParseQuery.getQuery(USER);
                    friendQuery.whereContainedIn(ID, id);
                    friendQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            friends_tip = Util.getUserFacebookFriendsAndFeedback(DOCTOR_EMAIL, objects);
                            setAdapter();
                        }
                    });

                }

            } else {
                progress_tip.setVisibility(View.GONE);
                suggest_null.setVisibility(View.VISIBLE);
                facebook_tip.setVisibility(View.VISIBLE);
            }
        }


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
            rightRating = Float.parseFloat(DOCTOR_FEEDBACK);
        }else{
            //Log.d("DoctorFragment","Feedback is null");
        }

        RelativeLayout call_button = (RelativeLayout) rootView.findViewById(R.id.telefono);
        call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String no = DOCTOR_PHONE;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + no));
                startActivity(intent);
            }
        });



        RelativeLayout feedback_button = (RelativeLayout) rootView.findViewById(R.id.feedback_relative);
        feedback_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*FragmentTransaction ft2 = getActivity().getSupportFragmentManager().beginTransaction();
                ProgressFragment f2=ProgressFragment.newInstance("","");
                ft2.replace(R.id.frame_doctor, f2);

                ft2.commit();*/
                if(ParseUser.getCurrentUser()!=null){
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                FeedbackFragment fragment = new FeedbackFragment().newInstance(index);

                ft.replace(R.id.frame_doctor,fragment);
                ft.addToBackStack(null);
                ft.commit();
                }else{
                    new MaterialDialog.Builder(c)
                            .title("Login")
                            .content("Per vedere i feedback è richiesto il login")
                            .positiveText("Login")
                            .negativeText("Annulla")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Intent intent = new Intent(c, FirstActivity.class);
                                    startActivity(intent);
                                }

                            })
                            .show();
                }
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

    public void setAdapter(){

        mAdapter = new FacebookAdapter(friends_tip);

        mRecyclerView.setAdapter(mAdapter);

        int adapter_count = mAdapter.getItemCount();

        facebook_tip.setVisibility(View.GONE);
        progress_tip.setVisibility(View.GONE);

        if (adapter_count != 0) {
            mRecyclerView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.doctor_item_height);

            if (adapter_count > 1)
                suggest_null.setText(adapter_count + " amici consigliano questo dottore!");
            else
                suggest_null.setText(adapter_count + " amico consiglia questo dottore");
        }
        else {
            suggest_null.setVisibility(View.VISIBLE);
            suggest_null.setText(R.string.feedback_null);
        }
    }


    public static void changeRating(float avg){
        rightRating = avg;
        DoctorListFragment.refreshList();

    }

    @Override
    public void onStart() {
        super.onStart();
        if (rightRating != 0)
            ratingBar.setRating(rightRating);
    }
}
