package com.doctorfinderapp.doctorfinder.adapter;

import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.activity.DoctorActivity;
import com.doctorfinderapp.doctorfinder.functions.RoundedImageView;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.List;

/**
 * Created by fedebyes on 05/03/16.
 */
public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {


    List<ParseObject> feedbacklist;

    public FeedbackAdapter(List<ParseObject> feedbacks) {
        this.feedbacklist = feedbacks;
    }

    @Override
    public FeedbackAdapter.FeedbackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_item, parent, false);
        return new FeedbackViewHolder(v);

    }

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {

        TextView feedback_text;
        RatingBar ratingBar;
        TextView name;
        RoundedImageView propic;
        Button login;

        FeedbackViewHolder(View itemView) {
            super(itemView);
            feedback_text = (TextView) itemView.findViewById(R.id.feedback_text);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            name = (TextView) itemView.findViewById(R.id.username);
            propic= (RoundedImageView) itemView.findViewById(R.id.user_image_feed);
            login = (Button) itemView.findViewById(R.id.login_facebook_feedback);

        }
    }


    @Override
    public void onBindViewHolder(final FeedbackAdapter.FeedbackViewHolder holder, int position) {

        if (feedbacklist.get(position) != null && !feedbacklist.get(position).getClassName().equals("Feedback")) {

            if (feedbacklist.get(position).getClassName().equals("NULL")) {
                holder.name.setText("Doctor Finder");
                holder.feedback_text.setText("La tua connessione non sembra essere stabile, controlla la " +
                        "connessione a Internet e riprova!");
                holder.ratingBar.setRating(holder.ratingBar.getMax());
                holder.propic.setImageResource(R.drawable.personavatar);

            } else if (feedbacklist.get(position).getClassName().equals("NOLOGIN")) {
                holder.name.setText("Doctor Finder");
                holder.feedback_text.setText("Connettiti a Facebook per poter consultare i feedback" +
                        "lasciati a questo medico!");
                holder.ratingBar.setRating(holder.ratingBar.getMax());
                holder.propic.setImageResource(R.drawable.personavatar);

                //Button setup
                holder.login.setVisibility(View.VISIBLE);
                holder.login.setText("Login con Facebook");
                holder.login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        DoctorActivity.loginwithfb();
                    }
                });

            } else {
                String text = feedbacklist.get(position).get("feedback_description").toString();
                String rating = feedbacklist.get(position).get("Rating").toString();
                boolean anonymus = (boolean) feedbacklist.get(position).get("Anonymus");
                holder.propic.setImageResource(R.drawable.mario);
                holder.feedback_text.setText(text);
                holder.ratingBar.setRating(Float.parseFloat(rating));
                if (!anonymus) {
                    String name = feedbacklist.get(position).get("Name").toString();
                    holder.name.setText(name);

                    String email = feedbacklist.get(position).get("email_user").toString();
                    Log.d("email", email.toString());
                    final ParseQuery<ParseObject> userPhoto = ParseQuery.getQuery("UserPhoto");
                    userPhoto.whereEqualTo("username", email);

                    userPhoto.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject userPhoto, ParseException e) {

                            if (userPhoto == null) {
                                Log.d("user feedback photo", "isNull");

                            } else {

                                ParseFile file = (ParseFile) userPhoto.get("profilePhoto");
                                Log.d("user feedback photo", "not null");
                                if (e == null) {

                                    file.getDataInBackground(new GetDataCallback() {
                                        @Override
                                        public void done(byte[] data, ParseException e) {
                                            holder.propic.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
                                        }
                                    });
                                } else {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });


                } else {
                    holder.name.setText(R.string.anonymus_name);
                }


            }

        }
    }

        @Override
        public int getItemCount () {
            //Log.d("Feedback", "" + feedbacklist.size());
            return feedbacklist.size();
        }


}
