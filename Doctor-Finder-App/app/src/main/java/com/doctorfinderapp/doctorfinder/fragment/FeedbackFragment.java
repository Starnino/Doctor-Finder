package com.doctorfinderapp.doctorfinder.fragment;

import android.app.ProgressDialog;
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
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class FeedbackFragment extends Fragment {
    private int index;

    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    public static FeedbackAdapter feedbackAdapter, nullAdapter;
    private List<ParseObject> FeedbackArray, nullArray;

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
        // Inflate the layout for this fragment

        ParseObject DOCTORTHIS = DoctorActivity.DOCTORTHIS;
        String EMAIL = DOCTORTHIS.getString("Email");

        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);

        FloatingActionButton fabfeedback = (com.melnykov.fab.FloatingActionButton) getActivity().findViewById(R.id.fabfeedback);
        fabfeedback.attachToRecyclerView(mRecyclerView);

        FeedbackArray= new ArrayList<>();

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);

        if (Util.isOnline(getActivity())) {
            //If there is Internet connection
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Feedback");
            //Log.d("Feedback","showing feedback of"+ EMAIL);
            query.whereEqualTo("email_doctor", EMAIL);

            try {
                FeedbackArray = query.find();

                feedbackAdapter = new FeedbackAdapter(FeedbackArray);

                mRecyclerView.setAdapter(feedbackAdapter);

                //TODO set null text

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            //if there is not Internet connection set null Array
            nullArray = new ArrayList<>();
            nullAdapter = new FeedbackAdapter(nullArray);
            mRecyclerView.setAdapter(nullAdapter);
        }
        return mRecyclerView;
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

}
