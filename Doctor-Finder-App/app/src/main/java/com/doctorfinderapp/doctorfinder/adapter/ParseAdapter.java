package com.doctorfinderapp.doctorfinder.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import com.doctorfinderapp.doctorfinder.activity.DoctorActivity;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
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

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.doctorfinderapp.doctorfinder.R.drawable.doctor_avatar;

/**
 * Created by francesco on 01/03/16.
 *
 */

public class ParseAdapter extends RecyclerView.Adapter<ParseAdapter.ParseViewHolder> {

    public final String NAME = "FirstName";
    public final String SURNAME = "LastName";
    public final String SPECIALIZATION = "Specialization";
    public final String FEEDBACK = "Feedback";
    public final String CITY = "Province";
    public final String EMAIL = "Email";
    public static SweetAlertDialog dialog = null;

    public class ParseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

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
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = GlobalVariable.DOCTORS.indexOf(DOCTORS.get(getLayoutPosition()));
            Context context = v.getContext();

            Intent intent = new Intent(context, DoctorActivity.class);
            //------
            intent.putExtra("index", position);
            //------
            context.startActivity(intent);

            //Log.d("POSITION >> ", GlobalVariable.DOCTORS.indexOf(DOCTORS.get(getLayoutPosition())) + "");
        }
    }

    List<ParseObject> DOCTORS;

    public ParseAdapter(List<ParseObject> doctors) {
        this.DOCTORS = new ArrayList<>(doctors);
    }

    @Override
    public ParseViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_card_item, parent, false);
        return new ParseViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ParseViewHolder holder, final int position) {

        holder.name.setText(DOCTORS.get(position).getString(NAME) + " " + DOCTORS.get(position).getString(SURNAME));

        ArrayList<String> spec = (ArrayList<String>) DOCTORS.get(position).get(SPECIALIZATION);
        holder.special.setText(Util.setSpecialization(spec));
        holder.setIsRecyclable(false);
        holder.ratingBar.setRating(Float.parseFloat(DOCTORS.get(position).get(FEEDBACK).toString()));

        ArrayList<String> city = (ArrayList<String>) DOCTORS.get(position).get(CITY);
        holder.city.setText(Util.setSpecialization(city));

        holder.profile.setImageResource(doctor_avatar);

        final ParseQuery<ParseObject> doctorph = ParseQuery.getQuery("DoctorPhoto");
        doctorph.whereEqualTo(EMAIL, DOCTORS.get(position).getString(EMAIL));

        doctorph.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject doctorPhoto, ParseException e) {

                if (doctorPhoto == null) {
                    //Log.d("doctorphoto", "isNull");

                } else {

                    ParseFile file = (ParseFile) doctorPhoto.get("profilePhoto");
                    if (e == null) {

                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (e == null) {
                                    holder.profile.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
                                } else {
                                    e.printStackTrace();
                                }}
                        });
                    }
                }
            }
        });

        /*for (int i = 0; i < DOCTORS.size(); i++) {
            Log.d("DOCTOR SIZE --> ", (i+1)+"");
        }*/
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
