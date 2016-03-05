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


/**
 * Created by francesco on 03/02/16.
 */

public class DoctorListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ParseAdapter parseAdapter;

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

        parseAdapter = new ParseAdapter(GlobalVariable.DOCTORS);

        mRecyclerView.setAdapter(parseAdapter);

        return mRecyclerView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
