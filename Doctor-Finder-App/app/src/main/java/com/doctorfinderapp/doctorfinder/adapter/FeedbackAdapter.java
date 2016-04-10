package com.doctorfinderapp.doctorfinder.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.activity.DoctorActivity;
import com.doctorfinderapp.doctorfinder.fragment.DoctorFragment;
import com.doctorfinderapp.doctorfinder.fragment.FeedbackFragment;
import com.doctorfinderapp.doctorfinder.functions.RoundedImageView;
import com.mikepenz.iconics.view.IconicsImageView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fedebyes on 05/03/16.
 */
public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {

    List<ParseObject> feedbacklist;
    String EMAIL_DOCTOR_THIS;
    String EMAIL_USER_THIS;
    String FEEDBACK = "Feedback";
    String USER_EMAIL = "email_user";
    String DOCTOR_EMAIL = "email_doctor";
    String NUM_THUMB = "num_thumb";
    String THUMB_LIST = "thumb_list";
    String DATE = "date";
    String NAME = "Name";
    String ANONYMUS = "Anonymus";
    String FEEDBACK_DESCRIPTION = "feedback_description";
    String RATING = "Rating";
    String DOCTOR = "Doctor";
    String EMAIL = "Email";
    ParseObject annulla;

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

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {

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
        }
    }

    @Override
    public void onBindViewHolder(final FeedbackAdapter.FeedbackViewHolder holder, final int position) {

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

            if (EMAIL_USER_THIS.equals(feedbacklist.get(position).getString(USER_EMAIL))) {
                holder.spam.setVisibility(View.INVISIBLE);
                holder.spam.setClickable(false);
                holder.clear.setVisibility(View.VISIBLE);
                holder.clear.setClickable(true);
                holder.propic.getLayoutParams().height = (int) holder.itemView.getResources().getDimension(R.dimen.feed_image);
                holder.propic.getLayoutParams().width = (int) holder.itemView.getResources().getDimension(R.dimen.feed_image);
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
                    holder.thumb.setClickable(false);
                    if (holder.THUMB_PRESSED)
                        holder.THUMB_PRESSED = false;
                    else holder.THUMB_PRESSED = true;

                    onClickButton(v, holder, position);
                }
            });

            holder.spam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickButton(v, holder, position);
                }
            });

            holder.clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickButton(v, holder, position);
                }
            });

    }

    @Override
    public int getItemCount() {
        //Log.d("Feedback", "" + feedbacklist.size());
        return feedbacklist.size();
    }

    public void onClickButton(final View v, final FeedbackViewHolder holder, final int position) {
        final int id = v.getId();

        switch (id) {
            case R.id.feed_spam:

                //get instance of menu
                holder.popup = new PopupMenu(holder.itemView.getContext(), holder.spam);
                holder.popup.getMenuInflater().inflate(R.menu.menu_card_feedback, holder.popup.getMenu());
                holder.popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getItemId() == R.id.action_spam) {

                            new MaterialDialog.Builder(v.getContext())
                                    .title("Report Spam")
                                    .content("Descrivi perchè secondo te questo feedback deve essere eliminato")
                                    .inputType(InputType.TYPE_MASK_CLASS | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE)
                                    .input("Testo", null, new MaterialDialog.InputCallback() {
                                        @Override
                                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                                            Log.d("INPUT", input.toString());

                                            final String body = "Doctor: " + EMAIL_DOCTOR_THIS + "\n" + "\n" +
                                                    "User Feedback: " + EMAIL_USER_THIS + "\n" + "\n" +
                                                    "User Spammer: " + feedbacklist.get(holder.getAdapterPosition()).getString(USER_EMAIL) + "\n" + "\n" +
                                                    "Feedback text: " + holder.feedback_text.getText() + "\n" +
                                                    "User Text: " + input.toString();

                                            BackgroundMail.newBuilder(v.getContext())
                                                    .withUsername("doctor.finder.dcf@gmail.com")
                                                    .withPassword("quantomacina")
                                                    .withMailto("doctor.finder.dcf@gmail.com")
                                                    .withSubject("REPORT SPAM")
                                                    .withBody(body)
                                                    .send();

                                            Snackbar.make(v, "Grazie per la segnalazione!", Snackbar.LENGTH_LONG)
                                                    .show();
                                        }
                                    }).positiveText("Invia")
                                    .negativeText("Annulla")

                                    .show();
                        }
                        return true;
                    }
                });

                holder.popup.show();
                break;

            case R.id.feed_clear:

                //get instance of menu
                holder.popup = new PopupMenu(holder.itemView.getContext(), holder.clear);
                holder.popup.getMenuInflater().inflate(R.menu.menu_card_clear, holder.popup.getMenu());
                holder.popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getItemId() == R.id.action_clear) {

                            deleteFeedback(position);
                            
                            /*Snackbar.make(v , "Eliminato!", Snackbar.LENGTH_LONG)
                                    .setAction("Annulla", new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            holder.clear.setClickable(false);
                                            safeSave(annulla);
                                            annulla = null;
                                            holder.clear.setClickable(true);

                                        }
                                    })
                                    .setActionTextColor(v.getResources().getColor(R.color.docfinder))
                                    .show();
                        */

                            Snackbar a=Snackbar.make(v, "Eliminato!", Snackbar.LENGTH_LONG)
                                    .setAction("Annulla", new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            holder.clear.setClickable(false);
                                            safeSave(annulla);
                                            annulla = null;
                                            holder.clear.setClickable(true);

                                        }});

                            if(DoctorActivity.fabfeedback.isVisible()){
                                            DoctorActivity.fabfeedback.setTranslationY(-96);
                                        }
                            a.setCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar snackbar, int event) {
                                    super.onDismissed(snackbar, event);
                                    Log.d("Snackbar","dismissed");
                                    if(DoctorActivity.fabfeedback.isVisible())DoctorActivity.fabfeedback.setTranslationY(0);

                                }
                            });
                            a.show();
                        }
                        return true;
                    }
                });

                holder.popup.show();
                break;

            case R.id.feed_like:
                holder.thumb.setClickable(false);
                switchColorAndThumb(holder, position);
        }
    }

    public void switchColorAndThumb(FeedbackViewHolder holder, int position) {
        Animation resize_big = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.resize_big);
        holder.thumb.startAnimation(resize_big);

        if (holder.THUMB_PRESSED) {
            holder.thumb.setColor(holder.itemView.getResources().getColor(R.color.colorPrimaryDark));
            holder.num_thumb.setText(String.valueOf(Integer.parseInt(holder.num_thumb.getText().toString()) + 1));
            ThumbUtil(holder.THUMB_PRESSED, position, holder);

        } else {
            holder.thumb.setColor(holder.itemView.getResources().getColor(R.color.grey));
            holder.num_thumb.setText(String.valueOf(Integer.parseInt(holder.num_thumb.getText().toString()) - 1));
            ThumbUtil(holder.THUMB_PRESSED, position, holder);
        }
    }

    public void ThumbUtil(final boolean THUMB_PRESSED, int position, final FeedbackViewHolder holder) {

        final Animation resize_small = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.resize_small);
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
                    //Log.d("THUMB LIST --> ", object.getString(USER_EMAIL) + " & " + object.getString(DOCTOR_EMAIL) + " AGGIUNTO");
                    //case of thumb was pressed and we need to put object and increment 1 like
                    if (THUMB_PRESSED) {

                        if (EMAIL_USER_THIS != null) {
                            object.add(THUMB_LIST, EMAIL_USER_THIS);

                            object.increment(NUM_THUMB, 1);
                            try {
                                object.save();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                        }

                        //case of thumb wasn't pressed and we need to remove object from thumb list and decrement 1 like
                    } else if (!THUMB_PRESSED) {

                        List<String> thumbList = object.getList(THUMB_LIST);

                        if (ParseUser.getCurrentUser().getEmail() != null)
                            thumbList.remove(EMAIL_USER_THIS);

                        object.put(THUMB_LIST, thumbList);
                        object.increment(NUM_THUMB, -1);
                        try {
                            object.save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    }
                    holder.thumb.setClickable(true);
                    holder.thumb.startAnimation(resize_small);
                }
            }
        });
    }

    public void deleteFeedback(final int position){
        //TODO start progress bar
        annulla = feedbacklist.get(position);
        ParseQuery<ParseObject> delete = ParseQuery.getQuery(FEEDBACK);
        delete.whereEqualTo(DOCTOR_EMAIL, EMAIL_DOCTOR_THIS);
        delete.whereEqualTo(USER_EMAIL, EMAIL_USER_THIS);
        delete.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        object.deleteEventually();
                        feedbacklist.remove(position);
                        notifyItemRemoved(position);
                        rebuildFeedbackAverage();
                        //TODO finish progress bar
                    }
            }
        });
    }

    public void insertItem(ParseObject item){
        feedbacklist.add(0,item);
        notifyItemInserted(0);
        FeedbackFragment.scroolTo(0);
    }

    public void changeMyFeedback(){
        notifyItemChanged(0);
    }


    public void safeSave(final ParseObject object){
        ParseObject feedback = new ParseObject(FEEDBACK);
        feedback.put(USER_EMAIL, object.getString(USER_EMAIL));
        feedback.put(DOCTOR_EMAIL, object.getString(DOCTOR_EMAIL));
        feedback.put(NAME, object.getString(NAME));
        feedback.put(ANONYMUS, object.getBoolean(ANONYMUS));
        feedback.put(FEEDBACK_DESCRIPTION, object.getString(FEEDBACK_DESCRIPTION));
        feedback.put(RATING, Float.parseFloat(object.get(RATING).toString()));
        feedback.put(DATE, object.getString(DATE));
        feedback.put(THUMB_LIST, object.getList(THUMB_LIST));
        feedback.put(NUM_THUMB, object.getInt(NUM_THUMB));
        feedback.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                insertItem(object);
            }
        });
    }

    public void rebuildFeedbackAverage(){
        ParseQuery<ParseObject> doctor = ParseQuery.getQuery(DOCTOR);
        doctor.whereEqualTo(EMAIL, EMAIL_DOCTOR_THIS);
        doctor.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    if (feedbacklist.size() == 0) {
                        object.put(FEEDBACK, 0);

                    } else {
                        List<ParseObject> feedbacks = new ArrayList<>();
                        ParseQuery<ParseObject> numFeed = ParseQuery.getQuery(FEEDBACK);
                        numFeed.whereEqualTo(DOCTOR_EMAIL, EMAIL_DOCTOR_THIS);
                        try {
                            feedbacks = numFeed.find();

                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        float somma = 0;
                        int numfeed = feedbacks.size();
                        for (int i = 0; i < numfeed; i++) {
                            somma += Float.parseFloat(feedbacks.get(i).get(RATING).toString());
                        }

                        float avg = somma / numfeed;
                        DoctorFragment.changeRating(avg);
                        object.put(FEEDBACK, avg);
                    }

                    try {
                        object.save();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }
}