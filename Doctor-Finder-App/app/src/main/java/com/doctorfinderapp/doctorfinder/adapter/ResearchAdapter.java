package com.doctorfinderapp.doctorfinder.adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.activity.MainActivity;
import com.doctorfinderapp.doctorfinder.functions.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    List<String[]> linearLayouts;

    public ResearchAdapter(List<String[]> research){
        this.linearLayouts = new ArrayList<>(research);
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
                Context context = v.getContext();
                if(!Util.isOnline(context)) {
                    Snackbar.make(v, R.string.connection_control, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                } else {
                    MainActivity.research(Arrays.asList(linearLayouts.get(position)[0]),
                            Arrays.asList(linearLayouts.get(position)[1]), v.getContext());
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