package com.doctorfinderapp.doctorfinder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.doctorfinderapp.doctorfinder.R;

import java.util.ArrayList;

/**
 * Created by francesco on 01/02/16.
 */

public class SpecializationAdapter extends ArrayAdapter<String>  {

    private ArrayList<String> specializations;
    private ArrayList<String> special;
    private Context mycontext;

    public SpecializationAdapter(Context context, ArrayList<String> specializations){
        super(context, 0, specializations);
        this.specializations = specializations;
        this.special = specializations;
        this.mycontext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String specialization = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.special_item, parent, false);
        }

        final TextView text = (TextView) convertView.findViewById(R.id.specialization_text);
        text.setText(specialization);
        return convertView;

    }

    public void filter(String text){
        text = text.toLowerCase();
        specializations.clear();
        if (text.length() == 0) specializations.addAll(special);
        else {
            for (String sp : special){
                if (sp.toLowerCase().contains(text)){
                    specializations.add(sp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
