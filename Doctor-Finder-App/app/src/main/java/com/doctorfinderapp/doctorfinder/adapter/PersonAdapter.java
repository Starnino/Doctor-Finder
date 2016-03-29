package com.doctorfinderapp.doctorfinder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.objects.Person;
import com.doctorfinderapp.doctorfinder.functions.RoundedImageView;

import java.util.List;

/**
 * Created by francesco on 18/02/16.
 */
public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder>{

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView personName;
        RoundedImageView personPhoto;

        PersonViewHolder(View itemView) {
            super(itemView);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            personPhoto = (RoundedImageView)itemView.findViewById(R.id.person_photo);
        }
    }

    List<Person> friends;

    public PersonAdapter(List<Person> persons){
        this.friends = persons;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_item, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        holder.personName.setText(friends.get(position).getName());
        holder.personPhoto.setImageResource(friends.get(position).getPhotoId());
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



}