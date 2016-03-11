package com.doctorfinderapp.doctorfinder.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.doctorfinderapp.doctorfinder.DoctorActivity;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.adapter.FeedbackAdapter;
import com.doctorfinderapp.doctorfinder.functions.Util;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by fedebyes on 08/03/16.
 */

public class FeedbackDialogFragment extends DialogFragment {

    public View rootView;

    public static FeedbackDialogFragment newInstance(String email_doctor) {
        FeedbackDialogFragment dialog = new FeedbackDialogFragment();
        Bundle args = new Bundle();
        args.putString("email_doctor", email_doctor);

        dialog.setArguments(args);
        return dialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String email_doctor = getArguments().getString("email_doctor");
        final float rating = 0;
        final String email_user = ParseUser.getCurrentUser().getEmail();

        //builder.setView(inflater.inflate(R.layout.dialog_feedback, null));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        rootView = inflater.inflate(R.layout.dialog_feedback, null);


        controlFeedback(rootView, email_user, email_doctor);


        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(rootView);


        TextView send = (TextView) rootView.findViewById(R.id.invia);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CheckBox checkBox = (CheckBox) rootView.findViewById(R.id.checkBox_respons);
                final CheckBox checkBoxAnonymus = (CheckBox) rootView.findViewById(R.id.checkBox_anonymus);
                Log.d("FeedbackDialog", "send clicked");
                if (checkBox.isChecked()) {
                    pushFeedback(rootView, email_user, email_doctor, checkBoxAnonymus.isChecked());
                    dismiss();
                }

            }
        });
        TextView cancel = (TextView) rootView.findViewById(R.id.annulla);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("FeedbackDialog", "cancel clicked");
                FeedbackDialogFragment.this.dismiss();


            }
        });


        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void controlFeedback(View rootView, String email_user, String email_doctor) {

        final EditText feedback_description_editText = (EditText) rootView.findViewById(R.id.editText);
        final RatingBar ratingbar = (RatingBar) rootView.findViewById(R.id.ratingbar_feedback);
        final CheckBox checkBoxAnonymus = (CheckBox) rootView.findViewById(R.id.checkBox_anonymus);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Feedback");
        query.whereEqualTo("email_user", email_user);
        query.whereEqualTo("email_doctor", email_doctor);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (object != null) {

                    Log.d("controFeedback", "User feedback alredy given for this doctor, editing mode");

                    String feedback_description = object.getString("feedback_description");

                    if (feedback_description != null) {
                        Log.d("Feedback", feedback_description.toString());
                        Log.d("Feedback", (object.get("Anonymus")).toString());
                        checkBoxAnonymus.setChecked(object.getBoolean("Anonymus"));
                        feedback_description_editText.setText(feedback_description);
                        ratingbar.setRating(Float.parseFloat(object.get("Rating").toString()));
                    }
                }
            }
        });


    }

    private void pushFeedback(View rootView, final String email_user, final String email_doctor, final boolean anonymus) {
        //todo checked

        final String name = ParseUser.getCurrentUser().get("fName").toString();
        final EditText feedback_description_editText =
                (EditText) rootView.findViewById(R.id.editText);
        final String feedback_description = feedback_description_editText.getText().toString();
        final RatingBar ratingbar = (RatingBar) rootView.findViewById(R.id.ratingbar_feedback);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Feedback");
        query.whereEqualTo("email_user", email_user);
        query.whereEqualTo("email_doctor", email_doctor);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (object != null) {

                    //case editing mode
                    Log.d("controFeedback", "User feedback alredy given for this doctor, editing mode");
                    //String feedback_description = object.getString("feedback_description");
                    //feedback_description_editText.setText(feedback_description);
                    //ParseObject feedback = new ParseObject("Feedback");
                    object.put("email_user", email_user);
                    object.put("email_doctor", email_doctor);
                    object.put("Rating", new Float(ratingbar.getRating()));
                    object.put("Anonymus", anonymus);
                    object.put("feedback_description", feedback_description);

                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) Log.d("Push feedback", e.toString());
                            Log.d("Push feedback", "feedback saved");

                            DoctorActivity.showToastFeedback();
                            Util.calculateFeedback(email_doctor);
                        }
                    });
                } else {

                    //case never inserted feedback
                    ParseObject feedback = new ParseObject("Feedback");
                    feedback.put("email_user", email_user);
                    feedback.put("email_doctor", email_doctor);
                    feedback.put("Name", name);
                    feedback.put("Anonymus", anonymus);
                    feedback.put("feedback_description", feedback_description);
                    feedback.put("Rating", new Float(ratingbar.getRating()));

                    feedback.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) Log.d("Push feedback", e.toString());
                            DoctorActivity.showToastFeedback();
                            Util.calculateFeedback(email_doctor);
                            Log.d("Push feedback", "feedback saved");
                        }
                    });
                }

            }
        });


    }

}