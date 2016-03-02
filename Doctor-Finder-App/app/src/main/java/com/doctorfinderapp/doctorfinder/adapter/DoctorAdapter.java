package com.doctorfinderapp.doctorfinder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.doctorfinderapp.doctorfinder.Class.Doctor;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.functions.RoundedImageView;

import java.util.List;

/**
 * Created by francesco on 27/02/16.
 */
public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    public static class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView personName;
        TextView profession;
        RoundedImageView personPhoto;
        TextView province;

        DoctorViewHolder(View itemView) {
            super(itemView);
            personPhoto = (RoundedImageView) itemView.findViewById(R.id.person_photo);
            profession = (TextView) itemView.findViewById(R.id.profession);
            personName = (TextView) itemView.findViewById(R.id.person_name);
            province = (TextView) itemView.findViewById(R.id.province);
        }
    }

    List<Doctor> visits;

    public DoctorAdapter(List<Doctor> doctors) {
        this.visits = doctors;
    }

    @Override
    public DoctorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_item, parent, false);
        return new DoctorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DoctorViewHolder holder, int position) {
        holder.personName.setText((visits.get(position).havePisello() ? "Dott. ":"Dott.ssa ")
                + visits.get(position).getName());

        holder.personPhoto.setImageResource(visits.get(position).getPhotoId());
        holder.profession.setText(visits.get(position).getProfession());
        holder.province.setText(visits.get(position).getCity());
    }

    @Override
    public int getItemCount() {
        return visits.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

