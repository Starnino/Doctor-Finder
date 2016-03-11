package com.doctorfinderapp.doctorfinder.fragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.adapter.ParseAdapter;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.melnykov.fab.FloatingActionButton;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by francesco on 03/02/16.
 */

public class DoctorListFragment extends Fragment {

    private static RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private static ParseAdapter parseAdapter;
    private List<ParseObject> DOCTORS;
    private FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);

        DOCTORS = GlobalVariable.DOCTORS;

        parseAdapter = new ParseAdapter(DOCTORS);

        mRecyclerView.setAdapter(parseAdapter);

        //fab
        fab = (com.melnykov.fab.FloatingActionButton) getActivity().findViewById(R.id.fab);
        //attach fab to recycler view on scroll
        fab.attachToRecyclerView(mRecyclerView);

        return mRecyclerView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static void refreshDoctors(List<ParseObject> filters){
        parseAdapter.animateTo(filters);
        mRecyclerView.scrollToPosition(0);
    }

}
