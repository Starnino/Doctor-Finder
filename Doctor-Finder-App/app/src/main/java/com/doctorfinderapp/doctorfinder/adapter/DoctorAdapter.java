package com.doctorfinderapp.doctorfinder.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.doctorfinderapp.doctorfinder.objects.Doctor;
import com.doctorfinderapp.doctorfinder.activity.DoctorActivity;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.functions.RoundedImageView;
import com.doctorfinderapp.doctorfinder.functions.Util;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by francesco on 27/02/16.
 */
public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private final String EMAIL = "Email";

    public static class DoctorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView personName;
        TextView profession;
        RoundedImageView personPhoto;
        TextView province;
        String email;

        DoctorViewHolder(View itemView) {
            super(itemView);
            personPhoto = (RoundedImageView) itemView.findViewById(R.id.doctor_photo);
            profession = (TextView) itemView.findViewById(R.id.doctor_profession);
            personName = (TextView) itemView.findViewById(R.id.doctor_name);
            province = (TextView) itemView.findViewById(R.id.province);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Context context = v.getContext();
            Intent intent = new Intent(context, DoctorActivity.class);
            //------
            intent.putExtra("email", email);
            //------
            context.startActivity(intent);
        }
    }

    static List<Doctor> visits;

    public DoctorAdapter(List<Doctor> doctors) {
        this.visits = new ArrayList<>(doctors);
    }

    @Override
    public DoctorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_item, parent, false);
        return new DoctorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final DoctorViewHolder holder, final int position) {
        holder.personName.setText((visits.get(position).havePisello() ? "Dott. " : "Dott.ssa ")
                + Util.reduceString(visits.get(position).getSurname()));

        holder.personPhoto.setImageResource(R.drawable.doctor_avatar);

            final ParseQuery<ParseObject> doctorph = ParseQuery.getQuery("DoctorPhoto");
            doctorph.whereEqualTo(EMAIL, visits.get(position).getEmail());

            doctorph.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject doctorPhoto, ParseException e) {

                    if (doctorPhoto == null)
                        Log.d("doctorphoto", "isNull");

                    else {

                        ParseFile file = (ParseFile) doctorPhoto.get("profilePhoto");
                        if (e == null) {

                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    holder.personPhoto.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
                                }
                            });
                        }
                    }
                }
            });



        holder.profession.setText(Util.reduceString(visits.get(position).getProfession()));
        holder.province.setText(visits.get(position).getCity());
        holder.email = visits.get(position).getEmail();
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

