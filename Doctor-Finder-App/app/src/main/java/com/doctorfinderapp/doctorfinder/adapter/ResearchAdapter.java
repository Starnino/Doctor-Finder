package com.doctorfinderapp.doctorfinder.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doctorfinderapp.doctorfinder.MainActivity;
import com.doctorfinderapp.doctorfinder.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by francesco on 04/03/16.
 */
public class ResearchAdapter extends RecyclerView.Adapter<ResearchAdapter.ResearchViewHolder>{

    public static class ResearchViewHolder extends RecyclerView.ViewHolder {
        TextView special;
        TextView city;
        ImageButton search;

        ResearchViewHolder(View itemView) {
            super(itemView);
            special = (TextView) itemView.findViewById(R.id.categ);
            city = (TextView) itemView.findViewById(R.id.city);
            search = (ImageButton) itemView.findViewById(R.id.research_button);
        }
    }

    ArrayList<String[]> linearLayouts;

    public ResearchAdapter(ArrayList<String[]> research){
        this.linearLayouts = research;
    }

    @Override
    public ResearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.linear_item, parent, false);
        return new ResearchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ResearchViewHolder holder, final int position) {
        holder.special.setText(linearLayouts.get(position)[0]);
        holder.city.setText(linearLayouts.get(position)[1]);
        holder.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.research(MainActivity.research_special_parameters.get(position),
                        MainActivity.research_city_parameters.get(position));
                for (int i = 0; i < MainActivity.research_special_parameters.size() ; i++) {
                    Log.d("LINEAR ADAPTER --> ", MainActivity.research_city_parameters.get(i) + "" + position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return linearLayouts.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



}