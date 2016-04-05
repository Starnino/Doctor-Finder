package com.doctorfinderapp.doctorfinder.adapter;

import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.functions.RoundedImageView;
import com.mikepenz.iconics.view.IconicsImageView;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.net.PasswordAuthentication;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by fedebyes on 05/03/16.
 */
public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {

    List<ParseObject> feedbacklist;
    static String EMAIL_DOCTOR_THIS;
    static String EMAIL_USER_THIS;
    static String FEEDBACK = "Feedback";
    static String USER_EMAIL = "email_user";
    static String DOCTOR_EMAIL = "email_doctor";
    String NUM_THUMB = "num_thumb";
    String THUMB_LIST = "thumb_list";
    String DATE = "date";


    public FeedbackAdapter(List<ParseObject> feedbacks, String doctor_email, String user_email) {
        this.feedbacklist = feedbacks;
        EMAIL_DOCTOR_THIS = doctor_email;
        EMAIL_USER_THIS = user_email;
    }

    @Override
    public FeedbackAdapter.FeedbackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_item, parent, false);
        return new FeedbackViewHolder(v);
    }

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView feedback_text;
        RatingBar ratingBar;
        TextView name;
        RoundedImageView propic;
        IconicsImageView spam;
        IconicsImageView clear;
        IconicsImageView thumb;
        TextView num_thumb;
        boolean THUMB_PRESSED;
        TextView date;
        PopupMenu popup;
        ParseObject push;

        FeedbackViewHolder(View itemView) {
            super(itemView);
            feedback_text = (TextView) itemView.findViewById(R.id.feedback_text);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            name = (TextView) itemView.findViewById(R.id.username);
            propic = (RoundedImageView) itemView.findViewById(R.id.user_image_feed);
            spam = (IconicsImageView) itemView.findViewById(R.id.feed_spam);
            clear = (IconicsImageView) itemView.findViewById(R.id.feed_clear);
            thumb = (IconicsImageView) itemView.findViewById(R.id.feed_like);
            num_thumb = (TextView) itemView.findViewById(R.id.num_thumb);
            date = (TextView) itemView.findViewById(R.id.feed_date);
            spam.setOnClickListener(this);
            clear.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();

            switch (id) {
                case R.id.feed_spam:

                    //get instance of menu
                    popup = new PopupMenu(itemView.getContext(), spam);
                    popup.getMenuInflater().inflate(R.menu.menu_card_feedback, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            //TODO report spam
                            if (item.getItemId() == R.id.action_spam) {
                                Log.d("FEEDBACK ADAPTER --> ", "SPAM CLICKED");
                            }
                            return true;
                        }
                    });

                    popup.show();
                    break;

                case R.id.feed_clear:

                    //get instance of menu
                    popup = new PopupMenu(itemView.getContext(), clear);
                    popup.getMenuInflater().inflate(R.menu.menu_card_clear, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getItemId() == R.id.action_clear) {
                                //start progress bar
                                ParseQuery<ParseObject> delete = ParseQuery.getQuery(FEEDBACK);
                                delete.whereEqualTo(DOCTOR_EMAIL, EMAIL_DOCTOR_THIS);
                                delete.whereEqualTo(USER_EMAIL, EMAIL_USER_THIS);
                                delete.getFirstInBackground(new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject object, ParseException e) {
                                        if (e != null) {
                                            //errore
                                        }

                                        else object.deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e != null){
                                                    //errore

                                                } else {
                                                    //finish progress bar
                                                    //notifydatasetchanged
                                                }
                                            }
                                        });
                                    }
                                });

                            }
                            return true;
                        }
                    });

                    popup.show();
                    break;
            }
        }
    }

    @Override
    public void onBindViewHolder(final FeedbackAdapter.FeedbackViewHolder holder, final int position) {

        if (feedbacklist.get(position) != null && !feedbacklist.get(position).getClassName().equals(FEEDBACK)) {

            if (feedbacklist.get(position).getClassName().equals("NULL")) {
                holder.name.setText("Doctor Finder");
                holder.feedback_text.setText("La tua connessione non sembra essere stabile, controlla la " +
                        "connessione a Internet e riprova!");
                holder.ratingBar.setRating(holder.ratingBar.getMax());
                holder.propic.setImageResource(R.drawable.personavatar);

            } else if (feedbacklist.get(position).getClassName().equals("NOLOGIN")) {
                holder.name.setText("Doctor Finder");
                holder.feedback_text.setText("Devi essere loggato per poter consultare i feedback" +
                        " lasciati a questo medico!");
                holder.ratingBar.setRating(holder.ratingBar.getMax());
                holder.propic.setImageResource(R.drawable.personavatar);
            }

        } else {

            holder.THUMB_PRESSED = feedbacklist.get(position).getList(THUMB_LIST).contains(EMAIL_USER_THIS);

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
                //Log.d("email", email.toString());
                final ParseQuery<ParseObject> userPhoto = ParseQuery.getQuery("UserPhoto");
                userPhoto.whereEqualTo("username", email);

                userPhoto.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject userPhoto, ParseException e) {

                        if (userPhoto == null) {
                            //Log.d("user feedback photo", "isNull");

                        } else {

                            ParseFile file = (ParseFile) userPhoto.get("profilePhoto");
                            //Log.d("user feedback photo", "not null");
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

        if (EMAIL_USER_THIS.equals(feedbacklist.get(position).getString(USER_EMAIL))) {
            holder.spam.setVisibility(View.INVISIBLE);
            holder.spam.setClickable(false);
            holder.clear.setVisibility(View.VISIBLE);
            holder.clear.setClickable(true);
        }

        holder.date.setText(feedbacklist.get(position).getString(DATE));

        if (ParseUser.getCurrentUser().getEmail() != null)
            if (feedbacklist.get(position).getList(THUMB_LIST).contains(EMAIL_USER_THIS))
                holder.thumb.setColor(holder.itemView.getResources().getColor(R.color.colorPrimaryDark));
            else holder.thumb.setColor(holder.itemView.getResources().getColor(R.color.grey));

        holder.num_thumb.setText(String.valueOf(feedbacklist.get(position).getInt(NUM_THUMB)));
        holder.thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.THUMB_PRESSED)
                    holder.THUMB_PRESSED = false;
                else holder.THUMB_PRESSED = true;

                switchColorAndThumb(holder, position);
            }
        });


    }

    @Override
    public int getItemCount() {
        //Log.d("Feedback", "" + feedbacklist.size());
        return feedbacklist.size();
    }

    public void switchColorAndThumb(FeedbackViewHolder holder, int position) {

        if (holder.THUMB_PRESSED) {
            ThumbUtil(holder.THUMB_PRESSED, position);
            holder.thumb.setColor(holder.itemView.getResources().getColor(R.color.colorPrimaryDark));
            holder.num_thumb.setText(String.valueOf(Integer.parseInt(holder.num_thumb.getText().toString()) + 1));

        } else {
            ThumbUtil(holder.THUMB_PRESSED, position);
            holder.thumb.setColor(holder.itemView.getResources().getColor(R.color.grey));
            holder.num_thumb.setText(String.valueOf(Integer.parseInt(holder.num_thumb.getText().toString()) - 1));
        }
    }

    public void ThumbUtil(final boolean THUMB_PRESSED, int position) {

        final String user_email = feedbacklist.get(position).getString(USER_EMAIL);
        ParseQuery<ParseObject> feedback = ParseQuery.getQuery(FEEDBACK);
        feedback.whereEqualTo(DOCTOR_EMAIL, EMAIL_DOCTOR_THIS);
        feedback.whereEqualTo(USER_EMAIL, user_email);
        feedback.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e != null) {
                    //error

                } else {
                    Log.d("THUMB LIST --> ", object.getString(USER_EMAIL) + " & " + object.getString(DOCTOR_EMAIL) + "AGGIUNTO");
                    //case of thumb was pressed and we need to put object and increment 1 like
                    if (THUMB_PRESSED) {

                        if (EMAIL_USER_THIS != null) {
                            object.add(THUMB_LIST, EMAIL_USER_THIS);

                            object.increment(NUM_THUMB, 1);

                        }

                        //case of thumb wasn't pressed and we need to remove object from thumb list and decrement 1 like
                    } else if (!THUMB_PRESSED) {

                        List<String> thumbList = object.getList(THUMB_LIST);

                        if (ParseUser.getCurrentUser().getEmail() != null) {
                            if (thumbList.remove(EMAIL_USER_THIS))
                                Log.d("THUMB LIST --> ", "RIMOSSO");
                        }

                        object.put(THUMB_LIST, thumbList);
                        object.increment(NUM_THUMB, -1);

                    }
                }
            }
        });
    }

    public void pushFeedbackChange(){
        for (int i = 0; i < feedbacklist.size(); i++) {

        }
    }
}