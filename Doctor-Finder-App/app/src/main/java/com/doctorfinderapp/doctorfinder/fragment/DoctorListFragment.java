package com.doctorfinderapp.doctorfinder.fragment;


import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.activity.ResultsActivity;
import com.doctorfinderapp.doctorfinder.adapter.ParseAdapter;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.melnykov.fab.FloatingActionButton;
import com.parse.ParseObject;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;


/**
 * Created by francesco on 03/02/16.
 */

public class DoctorListFragment extends Fragment
        implements  SwipeRefreshLayout.OnRefreshListener
{

    private static RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private static ParseAdapter parseAdapter;
    private List<ParseObject> DOCTORS;
    private FloatingActionButton fab;
    private static ProgressWheel progressBar;
    private static SwipeRefreshLayout refresh;
    private static View rootView;


    public DoctorListFragment() {
    }

    public static DoctorListFragment newInstance(int index) {
        DoctorListFragment doc = new DoctorListFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);

        doc.setArguments(args);
        return doc;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_doctorlist,
                container, false);
        this.rootView=rootView;
        progressBar = (ProgressWheel) rootView.findViewById(R.id.progressBarList);
        progressBar.setBarColor(getResources().getColor(R.color.colorPrimaryDark));
        progressBar.spin();

        refresh= (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(this);



        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.result_recyclerview);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(mLayoutManager);

        DOCTORS = GlobalVariable.DOCTORS;

        parseAdapter = new ParseAdapter(DOCTORS);

        //mRecyclerView.setAdapter(parseAdapter);

        mRecyclerView.setAdapter(new SlideInBottomAnimationAdapter(parseAdapter));

        //todo remove animation
        //mRecyclerView.setItemAnimator();

        //fab
        fab = (com.melnykov.fab.FloatingActionButton) getActivity().findViewById(R.id.fab);
        //attach fab to recycler view on scroll
        fab.attachToRecyclerView(mRecyclerView);

        return rootView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static void refreshDoctors(List<ParseObject> filters){
        parseAdapter.animateTo(filters);
        mRecyclerView.scrollToPosition(0);
    }

    public static void setProgressBar(int visibility){
        progressBar.setVisibility(visibility);
        progressBar.stopSpinning();
    }

    public static void refreshList(){
        if (parseAdapter != null)
            parseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        Log.d("DoctorListFragment","OnRefresh called");
        refresh.setRefreshing(true);
        ResultsActivity.showDataM();

    }


    public static void stopRefresh(){
        refresh.setRefreshing(false);
    }

}
