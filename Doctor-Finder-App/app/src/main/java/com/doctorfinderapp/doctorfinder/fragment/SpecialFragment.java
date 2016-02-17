package com.doctorfinderapp.doctorfinder.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.ResultsActivity;
import com.doctorfinderapp.doctorfinder.adapter.SpecializationAdapter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by francesco on 01/02/16.
 */
public class SpecialFragment extends ListFragment implements AdapterView.OnItemClickListener {

    private SpecializationAdapter adapter;
    private CheckBox checkBox;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.special_fragment, viewGroup, false);

        //set checkbox
        checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // /*cast array in xml to arrayList*/
        ArrayList<String> myArray = new ArrayList<>();
        Collections.addAll(myArray, getResources().getStringArray(R.array.Specializations));
        /////////////////////////////////////
        adapter = new SpecializationAdapter(getActivity(), myArray);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ImageView icon  = (ImageView) view.findViewById(R.id.specialization_icon);
        icon.setImageResource(R.drawable.ic_check_circle_white_24dp);

        /**TODOview.startAnimation(animation);*/
        Toast.makeText(getActivity(), adapter.getItem(position) +" selected", Toast.LENGTH_SHORT)
                .show();

       // Fragment fragment = new CityFragment();
        //FragmentManager fm = getFragmentManager();
        //fm.beginTransaction().replace(R.id.list_fragment, fragment).commit();
        Intent intent = new Intent(getActivity(),ResultsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

}