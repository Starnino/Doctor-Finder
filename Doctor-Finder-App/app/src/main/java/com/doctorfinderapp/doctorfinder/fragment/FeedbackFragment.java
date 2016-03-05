package com.doctorfinderapp.doctorfinder.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.adapter.FeedbackAdapter;
import com.doctorfinderapp.doctorfinder.adapter.ParseAdapter;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FeedbackFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FeedbackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedbackFragment extends Fragment {
    private int index;

    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FeedbackAdapter feedbackAdapter;
    private List<ParseObject> FeedbackArray;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_feedback,
                container, false);
        //recycler view

        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.feedback_recycler_view);

        FeedbackArray= new ArrayList<>();
        FeedbackArray.add(new ParseObject("Feedback"));
        Log.d("Feedback",""+ FeedbackArray.size());
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);

        feedbackAdapter = new FeedbackAdapter(FeedbackArray);

        mRecyclerView.setAdapter(feedbackAdapter);








        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
