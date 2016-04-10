package com.doctorfinderapp.doctorfinder.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.doctorfinderapp.doctorfinder.activity.DoctorActivity;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.adapter.FeedbackAdapter;
import com.doctorfinderapp.doctorfinder.adapter.ParseAdapter;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.doctorfinderapp.doctorfinder.functions.Util;
import com.doctorfinderapp.doctorfinder.objects.Doctor;
import com.melnykov.fab.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;


public class FeedbackFragment extends Fragment implements  SwipeRefreshLayout.OnRefreshListener{
    private int index;

    private OnFragmentInteractionListener mListener;
    static private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    public static FeedbackAdapter feedbackAdapter;
    private List<ParseObject> FeedbackArray;
    String EMAIL;
    ProgressWheel progressWheel;
    private String EMAIL_DOCTOR = "email_doctor";
    private String EMAIL_USER = "email_user";
    private String FEEDBACK = "Feedback";
    private String Email = "Email";
    private SwipeRefreshLayout refresh;

    public FeedbackFragment() {
        // Required empty public constructor
    }


    public static FeedbackFragment newInstance(int index) {
        FeedbackFragment fragment = new FeedbackFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int indexFragment = getArguments().getInt("index", 0);
        this.index=indexFragment;
        DoctorActivity.switchFAB(1);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_feedback,
                container, false);

        progressWheel = (ProgressWheel) rootView.findViewById(R.id.progressBarFeedback);
        progressWheel.setBarColor(getResources().getColor(R.color.colorPrimaryDark));
        progressWheel.spin();


        refresh= (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_feedback);
        refresh.setOnRefreshListener(this);

        ParseObject DOCTORTHIS = DoctorActivity.DOCTORTHIS;
        EMAIL = DOCTORTHIS.getString(Email);


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.feedback_recyclerview);

        FloatingActionButton fabfeedback = (com.melnykov.fab.FloatingActionButton) getActivity().findViewById(R.id.fabfeedback);
        fabfeedback.attachToRecyclerView(mRecyclerView);


        if (Util.isOnline(getActivity()) && ParseUser.getCurrentUser() != null) {
            //If there is Internet connection
            DoctorActivity.fabfeedback.setClickable(false);

            downloadFeedback();
        }

        return rootView;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        DoctorActivity.switchFAB(0);
        mListener = null;
    }

    @Override
    public void onRefresh() {
        downloadFeedback();
        //refresh.setRefreshing(false);
    }


    public void downloadFeedback(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(FEEDBACK);
        //Log.d("Feedback","showing feedback of"+ EMAIL);
        query.whereEqualTo(EMAIL_DOCTOR, EMAIL);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                refresh.setRefreshing(false);
                if(e==null) {
                    Util.SnackbarYumm(DoctorActivity.fabfeedback, DoctorActivity.coordinator_layout,
                            "Trovati "+objects.size()+" feedback" );
                    FeedbackArray = objects;
                    orderBy(FeedbackArray, ParseUser.getCurrentUser().getEmail());
                    setRecyclerView();
                }
            }
        });
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void setRecyclerView(){

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new SlideInDownAnimator());
        feedbackAdapter = new FeedbackAdapter(FeedbackArray, EMAIL, ParseUser.getCurrentUser().getEmail());


        mRecyclerView.setAdapter(new SlideInBottomAnimationAdapter(feedbackAdapter));

        DoctorActivity.fabfeedback.setClickable(true);

        progressWheel.stopSpinning();
    }

    public void orderBy(List<ParseObject> feedbackArray, String email){
        for (int i = 0; i < feedbackArray.size(); i++) {
            if (feedbackArray.get(i).getString(EMAIL_USER).equals(email)) {
                ParseObject first = feedbackArray.get(i);
                feedbackArray.remove(i);
                feedbackArray.add(0,first);
            }
        }
    }

    public static void scroolTo(int position){
        mRecyclerView.scrollToPosition(position);
    }
}
