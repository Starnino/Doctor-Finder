package com.doctorfinderapp.doctorfinder.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.doctorfinderapp.doctorfinder.DoctorActivity;
import com.doctorfinderapp.doctorfinder.DoctorProfileActivity;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.doctorfinderapp.doctorfinder.functions.RoundedImageView;
import com.doctorfinderapp.doctorfinder.functions.Util;
import com.parse.ParseObject;
import java.util.ArrayList;
import java.util.List;

import static com.doctorfinderapp.doctorfinder.R.drawable.p_default;

/**
 * Created by francesco on 01/03/16.
 * p
 */
public class ParseAdapter extends RecyclerView.Adapter<ParseAdapter.ParseViewHolder> {

    public static final String NAME = "FirstName";
    public static final String SURNAME = "LastName";
    public static final String SPECIALIZATION = "Specialization";
    public static final String FEEDBACK = "Feedback";
    public static final String CITY = "Province";

    public static class ParseViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView special;
        RatingBar ratingBar;
        RoundedImageView profile;
        TextView city;

        public ParseViewHolder(final View itemView) {

            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            special = (TextView) itemView.findViewById(R.id.special);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            city = (TextView) itemView.findViewById(R.id.city);
            profile = (RoundedImageView) itemView.findViewById(R.id.profile_image);

            //todo query if photo exists on doctorphoto

            //todo download photo

        }
    }

    List<ParseObject> DOCTORS;

    public ParseAdapter(List<ParseObject> doctors) { this.DOCTORS = new ArrayList<>(doctors);}

    @Override
    public ParseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_card_item, parent, false);
        return new ParseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ParseViewHolder holder, final int position) {

        holder.name.setText(DOCTORS.get(position).getString(NAME) + " " + DOCTORS.get(position).getString(SURNAME));

        ArrayList<String> spec = (ArrayList<String>) DOCTORS.get(position).get(SPECIALIZATION);
        holder.special.setText(Util.setSpecialization(spec));

        holder.ratingBar.setRating(Float.parseFloat(DOCTORS.get(position).get(FEEDBACK).toString()));

        ArrayList<String> city = (ArrayList<String>) DOCTORS.get(position).get(CITY);
        holder.city.setText(Util.setCity(city));

        holder.profile.setImageResource(p_default);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, DoctorActivity.class);
                //------
                intent.putExtra("index", position);
                //------
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return DOCTORS.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public ParseObject removeItem(int position) {
        final ParseObject doctor = DOCTORS.remove(position);
        notifyItemRemoved(position);
        return doctor;
    }

    public void addItem(int position, ParseObject doctor) {
        DOCTORS.add(position, doctor);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final ParseObject doctor = DOCTORS.remove(fromPosition);
        DOCTORS.add(toPosition, doctor);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<ParseObject> doctors) {
        applyAndAnimateRemovals(doctors);
        applyAndAnimateAdditions(doctors);
        applyAndAnimateMovedItems(doctors);
    }

    private void applyAndAnimateRemovals(List<ParseObject> newDoctor) {
        for (int i = DOCTORS.size() - 1; i >= 0; i--) {
            final ParseObject doctor = DOCTORS.get(i);
            if (!newDoctor.contains(doctor)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<ParseObject> newDoctor) {
        for (int i = 0, count = newDoctor.size(); i < count; i++) {
            final ParseObject doctor = newDoctor.get(i);
            if (!DOCTORS.contains(doctor)) {
                addItem(i, doctor);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<ParseObject> newDoctor) {
        for (int toPosition = newDoctor.size() - 1; toPosition >= 0; toPosition--) {
            final ParseObject doctor = newDoctor.get(toPosition);
            final int fromPosition = newDoctor.indexOf(doctor);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

}
