package com.doctorfinderapp.doctorfinder;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.graphics.drawable.Icon;
import android.media.Image;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by francesco on 01/02/16.
 */
public class SpecialFragment extends ListFragment implements AdapterView.OnItemClickListener {


    SpecializationAdapter adapter;

        @Override
        public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.special_fragment, viewGroup, false);
            return view;
        }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //use custom adapter to listview
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
        icon.setColorFilter(R.color.colorPrimaryDark);
        Toast.makeText(getActivity(), adapter.getItem(position) +" selected", Toast.LENGTH_SHORT)
                .show();

        Fragment fragment = new CityFragment();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.list_fragment, fragment).commit();
    }
}


