package com.doctorfinderapp.doctorfinder.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.doctorfinderapp.doctorfinder.activity.DoctorActivity;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.adapter.FeedbackAdapter;
import com.doctorfinderapp.doctorfinder.functions.Util;
import com.melnykov.fab.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class FeedbackFragment extends Fragment {
    private int index;

    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    public static FeedbackAdapter feedbackAdapter, nullAdapter;
    private List<ParseObject> FeedbackArray, nullArray = new ArrayList<>();
    int color_red, color_red_pressed;
    String EMAIL;

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
        color_red = getResources().getColor(R.color.red_btn_bg_color);
        color_red_pressed = getResources().getColor(R.color.red_btn_bg_pressed_color);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        ParseObject DOCTORTHIS = DoctorActivity.DOCTORTHIS;
        EMAIL = DOCTORTHIS.getString("Email");


        View rootView = inflater.inflate(R.layout.fragment_feedback,
                container, false);


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.feedback_recyclerview);

        FloatingActionButton fabfeedback = (com.melnykov.fab.FloatingActionButton) getActivity().findViewById(R.id.fabfeedback);
        fabfeedback.attachToRecyclerView(mRecyclerView);


        if (Util.isOnline(getActivity()) && ParseUser.getCurrentUser() != null) {
            //If there is Internet connection
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Feedback");
            //Log.d("Feedback","showing feedback of"+ EMAIL);
            query.whereEqualTo("email_doctor", EMAIL);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    FeedbackArray = objects;
                    setRecyclerView();
                }
            });

        } else if(Util.isOnline(getActivity()) && ParseUser.getCurrentUser() == null){
            ParseObject feedback_null = new ParseObject("NOLOGIN");
            nullArray.add(feedback_null);
            DoctorActivity.fabfeedback.setColorNormal(color_red);
            DoctorActivity.fabfeedback.setColorPressed(color_red);
            nullAdapter = new FeedbackAdapter(nullArray, EMAIL, ParseUser.getCurrentUser().getEmail());
            mRecyclerView.setAdapter(nullAdapter);

        } else {
            //if there is not Internet connection set null Array
            ParseObject feedback_null = new ParseObject("NULL");
            nullArray.add(feedback_null);
            DoctorActivity.fabfeedback.setColorNormal(color_red);
            DoctorActivity.fabfeedback.setColorPressed(color_red);
            nullAdapter = new FeedbackAdapter(nullArray, EMAIL, ParseUser.getCurrentUser().getEmail());
            mRecyclerView.setAdapter(nullAdapter);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void setRecyclerView(){

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);

        feedbackAdapter = new FeedbackAdapter(FeedbackArray, EMAIL, ParseUser.getCurrentUser().getEmail());

        mRecyclerView.setAdapter(feedbackAdapter);
    }
}
