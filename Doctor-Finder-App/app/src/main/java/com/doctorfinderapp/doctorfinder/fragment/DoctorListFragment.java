package com.doctorfinderapp.doctorfinder.fragment;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.doctorfinderapp.doctorfinder.DoctorProfileActivity;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.adapter.ParseAdapter;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by francesco on 03/02/16.
 */

public class DoctorListFragment extends Fragment {

    private static List<ParseObject> DOCTORS = GlobalVariable.DOCTORS;
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

        parseAdapter = new ParseAdapter(DOCTORS);

        mRecyclerView.setAdapter(parseAdapter);

        return mRecyclerView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
