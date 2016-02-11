package com.doctorfinderapp.doctorfinder.adapter;

/**
 * Created by vindel100 on 17/01/16.
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.doctorfinderapp.doctorfinder.Doctor.Doctors;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.SingleItemView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListDoctorAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Doctors> doctorslist = null;
    private ArrayList<Doctors> arraylist;

    public ListDoctorAdapter(Context context, List<Doctors> doctorslist) {
        mContext = context;
        this.doctorslist = doctorslist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Doctors>();
        this.arraylist.addAll(doctorslist);
    }

    public class ViewHolder {
        TextView name;
        TextView special;
        TextView feedback;
    }



    @Override
    public int getCount() {
        return doctorslist.size();
    }

    @Override
    public Doctors getItem(int position) {
        return doctorslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.doctor_item, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.special = (TextView) view.findViewById(R.id.special);
            holder.feedback = (TextView) view.findViewById(R.id.feedback);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(doctorslist.get(position).getName());
        holder.special.setText(doctorslist.get(position).getSpecial());
        holder.feedback.setText(doctorslist.get(position).getFeedback());

        // Listen for ListView Item Click
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Send single item click data to SingleItemView Class
                Intent intent = new Intent(mContext, SingleItemView.class);
                intent.putExtra("name",(doctorslist.get(position).getName()));
                intent.putExtra("special",(doctorslist.get(position).getSpecial()));
                intent.putExtra("feedback",(doctorslist.get(position).getFeedback()));
                // Pass all data flag
                // Start SingleItemView Class
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        doctorslist.clear();
        if (charText.length() == 0) {
            doctorslist.addAll(arraylist);
        }
        else
        {
            for (Doctors wp : arraylist)
            {
                if (wp.getSpecial().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    doctorslist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}