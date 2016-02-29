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
    private static List<ParseObject> DOCTORS=GlobalVariable.DOCTORS;
    private static int SIZE=DOCTORS.size();
    private static int index=0;
    private static String TAG="DoctorListFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        index=0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //adding data from Parse
        //MainActivity.showData();
        //showData();
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

            //Dottore cpy local
            ParseObject DoctorLocal= GlobalVariable.DOCTORS.get(IndexLocale);

            //Get and set Name and LastName
            TextView name = (TextView) itemView.findViewById(R.id.name);
            String nameString = DoctorLocal.getString("FirstName") + " " + DoctorLocal.getString("LastName");
            name.setText(nameString);

            //Setting specialization
            TextView special = (TextView) itemView.findViewById(R.id.special);
            ArrayList<String> spec= (ArrayList<String>) DoctorLocal.get("Specialization");
            Log.d(TAG, "specialization as arraylist" + spec);
            String specializationString="";
            //divido le spec
            for(int i=0; i<spec.size(); i++){
                if (i == spec.size()-1) specializationString += spec.get(i);
                else specializationString += spec.get(i)+", ";
            }
            special.setText(specializationString);

            //setting rating aka feedback
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            Log.d(TAG, "rating of " + nameString + " " + DoctorLocal.get("Feedback").toString());
            ratingBar.setRating(Float.parseFloat(DoctorLocal.get("Feedback").toString()));
            //ratingBar.setRating();




            //TextView feedback = (TextView) itemView.findViewById(R.id.feedback);

            ImageView profile = (ImageView) itemView.findViewById(R.id.profile_image);
            int [] fotoId = {R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4, R.drawable.p5, R.drawable.p6, R.drawable.p7, R.drawable.p8, R.drawable.p2, R.drawable.p1};
            profile.setImageResource(fotoId[IndexLocale]);

            //String feedbackString = DOCTORS.get(index).getString("FirstName") + " " + DOCTORS.get(index).getString("LastName");


            //special.setText(specialString.substring(1,specialString.length()-1));


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

                    //give parameters to start activity
                    //String id=(DOCTORS.get(IndexLocale)).getObjectId();
                    //Log.d("Main","Showing "+id);
                    //Bundle_selected_doctor.putString("id", id); //Your id
                    //intent.putExtras(Bundle_selected_doctor); //Put your id to your next Intent
                    /*
                    //this code is for passing data to an activity
                    Bundle b = new Bundle();
                    b.putInt("key", 1); //Your id
                        intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
                     */
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
