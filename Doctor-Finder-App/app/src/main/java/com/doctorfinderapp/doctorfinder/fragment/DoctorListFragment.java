package com.doctorfinderapp.doctorfinder.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doctorfinderapp.doctorfinder.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by francesco on 03/02/16.
 */
public class DoctorListFragment extends Fragment {


    private static List<ParseObject> DOCTORS=null;
    private static int SIZE=0;
    private static int index=0;

    /*// Generate sample data

    String[] name = new String[]{"Loshoqrenzo Gitto", "Marco dell'Aquila", "Francesco Maiese", "Elena Dezi", "Wassim Arleo", "Marco Frison",
            "Mirim Renati", "Alexandra Zanni", "Stefania Bianchin", "Michele Benevento", "Francesco Silla", "Milena D'Arco", "Enzo Gatta",
            "Sofia Mancini", "Marco La Casa", "Antonio Russo"};

    String[] special = new String[]{"Dermatologist", "Cardiologist", "Dentist", "Dermatologist", "Medical Geneticist", "Dentist", "Dentist",
            "Cardiologist", "Dermatologist", "Dermatologist", "Cardiologist", "Allergist", "Medical Geneticist", "Allergist", "Cardiologist",
            "Medical Geneticist"};

    String[] feedback = new String[]{"**", "****", "*", "***", "****", "**", "*", "***", "***", "****", "****", "**", "*", "*", "***", "****"};
    */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //adding data from Parse
        showData();
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        ContentAdapter adapter = new ContentAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));




        return recyclerView;




    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {


            super(inflater.inflate(R.layout.doctor_item, parent, false));

            TextView textView = (TextView) itemView.findViewById(R.id.name);
            String text= DOCTORS.get(index).getString("FirstName");
            index++;
            textView.setText(text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    //Intent intent = new Intent(context, DetailActivty.class);
                    //context.startActivity(intent);
                }
            });
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




    //downloading doctors from parse
    private static void showData() {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Doctor");
        try {
            DOCTORS = query.find();
            SIZE=DOCTORS.size();

            Log.d("DoctorListFragment", "DOCTORS FOUND:" + DOCTORS.get(0).toString());
            Log.d("DoctorListFragment", DOCTORS.size()+"" );
        } catch (ParseException e) {
            Log.d("DoctorListFragment", "Cannot find doctors on parse"+e);
        }

    }
}
