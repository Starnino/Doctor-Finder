package com.doctorfinderapp.doctorfinder.adapter;

/**
 * Created by francesco on 09/03/16.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.Class.Person;
import com.doctorfinderapp.doctorfinder.functions.RoundedImageView;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by francesco on 18/02/16.
 */
public class FacebookAdapter extends RecyclerView.Adapter<FacebookAdapter.FacebookViewHolder> {

    public static final String NAME = "fName";
    public static final String EMAIl = "email";
    public static final String USERNAME = "username";
    public static final String PHOTO = "UserPhoto";

    public static class FacebookViewHolder extends RecyclerView.ViewHolder {
        TextView personName;
        RoundedImageView personPhoto;

        FacebookViewHolder(View itemView) {
            super(itemView);
            personName = (TextView) itemView.findViewById(R.id.person_name);
            personPhoto = (RoundedImageView) itemView.findViewById(R.id.person_photo);
        }
    }

    List<ParseObject> friends;

    public FacebookAdapter(List<ParseObject> persons) {
        this.friends = persons;
    }

    @Override
    public FacebookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_item, parent, false);
        FacebookViewHolder pvh = new FacebookViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final FacebookViewHolder holder, int position) {
        holder.personName.setText(friends.get(position).getString(NAME));
        holder.personPhoto.setImageResource(R.drawable.mario);

        //set dinamically photo
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PHOTO);
        query.whereEqualTo(USERNAME, friends.get(position).getString(EMAIl));
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject userPhoto, ParseException e) {

                //userphoto exists

                if (userPhoto == null) {
                    Log.d("userphoto", "isnull");

                } else {

                    ParseFile file = (ParseFile) userPhoto.get("profilePhoto");
                    file.getDataInBackground(new GetDataCallback() {
                        public void done(byte[] data, ParseException e) {
                            if (e == null) {
                                // data has the bytes for the resume
                                //data is the image in array byte
                                //must change image on profile
                                holder.personPhoto.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
                                Log.d("FriendPhoto", "downloaded");

                            } else {
                                // something went wrong
                                Log.d("FriendPhoto ", "problem download image");
                            }
                        }
                    });
                }
            }
        });
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