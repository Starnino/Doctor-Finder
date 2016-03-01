package com.doctorfinderapp.doctorfinder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.doctorfinderapp.doctorfinder.DoctorProfileActivity;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.functions.RoundedImageView;
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
    public static final String CITY = "Provence";

    public static class ParseViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView special;
        RatingBar ratingBar;
        RoundedImageView profile;
        TextView city;

        public ParseViewHolder(View itemView) {

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

    public ParseAdapter(List<ParseObject> doctors) { this.DOCTORS = doctors;}

    @Override
    public ParseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_card_item, parent, false);
        return new ParseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ParseViewHolder holder, int position) {

        holder.name.setText(DOCTORS.get(position).getString(NAME) + " " + DOCTORS.get(position).getString(SURNAME));

        ArrayList<String> spec = (ArrayList<String>) DOCTORS.get(position).get(SPECIALIZATION);
        holder.special.setText(setSpecialization(spec));

        holder.ratingBar.setRating(Float.parseFloat(DOCTORS.get(position).get(FEEDBACK).toString()));

        ArrayList<String> city = (ArrayList<String>) DOCTORS.get(position).get(CITY);
        holder.city.setText(setCity(city));

        holder.profile.setImageResource(p_default);


    }

    @Override
    public int getItemCount() {
        return DOCTORS.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public String setSpecialization(ArrayList<String> specialization){

        String specializationString="";
        //divido le spec
        specializationString += specialization.get(0);

        if (specialization.size() > 1) {
            if (specialization.get(0).length() > 12)
                specializationString += ", " + specialization.get(1).subSequence(0, 6) + "...";
            else
            if (specialization.get(1).length() < 12)
                specializationString += ", " + specialization.get(1);
            else specializationString += ", " + specialization.get(1).subSequence(0,6);
        }
        return specializationString;
        /**finish setting specialization*/
    }

    public String setCity(ArrayList<String> city){
        String res = "";
        for (int i = 0; i < city.size(); i++) {
            if (i == city.size()-1) res += city.get(i);
            else res += city.get(i) + ", ";
        } return res;
    }
}
