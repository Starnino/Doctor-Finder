package com.doctorfinderapp.doctorfinder.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.doctorfinderapp.doctorfinder.R;
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
        final float rating=0;
        final String email_user = ParseUser.getCurrentUser().getEmail();

        //builder.setView(inflater.inflate(R.layout.dialog_feedback, null));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        rootView = inflater.inflate(R.layout.dialog_feedback, null);




        controlFeedback(rootView, email_user, email_doctor);


        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(rootView);
        builder
                //.setMessage(R.string.feedback_message)
                .setPositiveButton(R.string.feedback_send, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                final CheckBox checkBox = (CheckBox) rootView.findViewById(R.id.checkBox_respons);
                                if (checkBox.isChecked()) {
                                    pushFeedback(rootView, email_user, email_doctor);
                                } else {
                                    //checkbox not checked
                                    //with dialog not toast
                                    //Toast toast = Toast.makeText(getContext(), R.string.notchecked, Toast.LENGTH_LONG);
                                    //toast.show();
                                }

                            }
                        }

                )
                .

                        setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                        //cancel();
                                    }
                                }

                        );


        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void controlFeedback(View rootView, String email_user, String email_doctor) {

        final EditText feedback_description_editText = (EditText) rootView.findViewById(R.id.editText);
        final RatingBar ratingbar= (RatingBar) rootView.findViewById(R.id.ratingbar_feedback);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Feedback");
        query.whereEqualTo("email_user", email_user);
        query.whereEqualTo("email_doctor", email_doctor);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (object != null) {
                    Log.d("controFeedback", "User feedback alredy given for this doctor, editing mode");
                    String feedback_description = object.getString("feedback_description");
                    feedback_description_editText.setText(feedback_description);
                    ratingbar.setRating(Float.parseFloat(object.getString("Rating")));


                }
            }
        });


    }

    private void pushFeedback(View rootView, final String email_user, final String email_doctor) {
        //todo checked


            final EditText feedback_description_editText =
                    (EditText) rootView.findViewById(R.id.editText);
            final String feedback_description = feedback_description_editText.getText().toString();
        final RatingBar ratingbar= (RatingBar) rootView.findViewById(R.id.ratingbar_feedback);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Feedback");
            query.whereEqualTo("email_user", email_user);
            query.whereEqualTo("email_doctor", email_doctor);

            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (object != null) {

                        //case editing mode
                        Log.d("controFeedback", "User feedback alredy given for this doctor, editing mode");
                        String feedback_description = object.getString("feedback_description");
                        //feedback_description_editText.setText(feedback_description);
                        //ParseObject feedback = new ParseObject("Feedback");
                        object.put("email_user", email_user);
                        object.put("email_doctor", email_doctor);
                        object.put("Rating", ratingbar.getRating());
                        object.put("feedback_description", feedback_description);

                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Log.d("Push feedback", "feedback saved");
                            }
                        });
                        //todo set ratingbar

                    } else {

                        //case never inserted feedback
                        ParseObject feedback = new ParseObject("Feedback");
                        feedback.put("email_user", email_user);
                        feedback.put("email_doctor", email_doctor);
                        feedback.put("feedback_description", feedback_description);
                        feedback.put("Rating",ratingbar.getRating());

                        feedback.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Log.d("Push feedback", "feedback saved");
                            }
                        });
                    }

                }
            });


        /*ParseObject feedback = new ParseObject("Feedback");
        feedback.put("email_user", email_user);
        feedback.put("email_doctor", email_doctor);
        feedback.put("feedback_description", feedback_description);

        feedback.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.d("Push feedback","feedback saved");
            }
        });*/
    }

}