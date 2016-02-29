package com.doctorfinderapp.doctorfinder.fragment;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.doctorfinderapp.doctorfinder.DoctorProfileActivity;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by francesco on 03/02/16.
 */

public class DoctorListFragment extends Fragment {

    private static RatingBar ratingBar;
    private static List<ParseObject> DOCTORS = GlobalVariable.DOCTORS;
    private static int SIZE=DOCTORS.size();
    private static int index=0;
    private static String TAG="DoctorListFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        index=0;
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        ContentAdapter adapter = new ContentAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {

            super(inflater.inflate(R.layout.doctor_card_item, parent, false));

            final int IndexLocale=index;
            ParseObject DoctorLocal= GlobalVariable.DOCTORS.get(IndexLocale);

            //Get and set Name and LastName
            TextView name = (TextView) itemView.findViewById(R.id.name);
            String nameString = DoctorLocal.getString("FirstName") + " " + DoctorLocal.getString("LastName");
            name.setText(nameString);

            //Setting specialization
            //Log.d(TAG, "specialization as arraylist" + spec);
            /*TextView special = (TextView) itemView.findViewById(R.id.special);
            ArrayList<String> spec= (ArrayList<String>) DoctorLocal.get("Specialization");
            String specializationString="";
            //divido le spec

            specializationString += spec.get(0);

            if (spec.size() > 1) {
                if (spec.get(0).length() > 12)
                specializationString += ", " + spec.get(1).subSequence(0,6) + "...";
                else
                    if (spec.get(1).length() < 12)
                        specializationString += ", " + spec.get(1);
                    else specializationString += ", " + spec.get(1).subSequence(0,6);
            }

            special.setText(specializationString);
            */

            //setting rating aka feedback
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            ratingBar.setRating(Float.parseFloat(DoctorLocal.get("Feedback").toString()));

            ImageView profile = (ImageView) itemView.findViewById(R.id.profile_image);
            profile.setImageResource(R.drawable.p_default);

            //todo query if photo exists on doctorphoto

            //todo download photo

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DoctorProfileActivity.class);
                    //------
                    GlobalVariable.idDoctors = DOCTORS.get(IndexLocale).getObjectId();
                    //------
                    context.startActivity(intent);
                }
            });

            index++;
        }
    }

    /**Adapter to display recycler view */
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of Card in RecyclerView.
        private static final int LENGTH = SIZE;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // no-op
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }



}
