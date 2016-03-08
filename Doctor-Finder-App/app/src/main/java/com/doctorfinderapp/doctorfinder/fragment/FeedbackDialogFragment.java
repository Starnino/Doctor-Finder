package com.doctorfinderapp.doctorfinder.fragment;

import android.app.Dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.doctorfinderapp.doctorfinder.R;

/**
 * Created by fedebyes on 08/03/16.
 */

public class FeedbackDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                //.setMessage(R.string.feedback_message)
                .setPositiveButton(R.string.feedback_send, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        //cancel();
                    }
                });

        builder.setView(inflater.inflate(R.layout.dialog_feedback, null));
        // Create the AlertDialog object and return it
        return builder.create();
    }
}