package com.doctorfinderapp.doctorfinder.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.media.RatingCompat;
import android.support.v7.app.AlertDialog;
import android.text.style.TtsSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.doctorfinderapp.doctorfinder.activity.DoctorActivity;
import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.functions.Util;
import com.doctorfinderapp.doctorfinder.objects.Doctor;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseSession;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by fedebyes on 08/03/16.
 */

public class FeedbackDialogFragment extends DialogFragment implements View.OnClickListener, RatingBar.OnRatingBarChangeListener {

    public View rootView;
    float disponibilita_float;
    float cordialita_float;
    float soddisfazione_float;
    float general_float;
    RatingBar disponibilita;
    RatingBar cordialita;
    RatingBar soddisfazione;
    RatingBar general_ratingbar;
    EditText text;
    static TextView date;
    CheckBox checkBox;
    CheckBox checkBoxAnonymus;
    String email_user;
    String email_doctor;
    TextView send;
    EditText dove;
    EditText tipo;
    Context c;
    SimpleDateFormat today;
    String today_string;
    static Date date_visit;
    static Date now;


    public static FeedbackDialogFragment newInstance(String email_doctor) {
        FeedbackDialogFragment dialog = new FeedbackDialogFragment();
        Bundle args = new Bundle();
        args.putString("email_doctor", email_doctor);

        dialog.setArguments(args);
        return dialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        email_doctor = getArguments().getString("email_doctor");
        //final float rating = 0;
        email_user = ParseUser.getCurrentUser().getEmail();
        c = getActivity();
        //builder.setView(inflater.inflate(R.layout.dialog_feedback, null));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        rootView = inflater.inflate(R.layout.dialog_feedback, null);

        controlFeedback(rootView, email_user, email_doctor);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(rootView);

        disponibilita = (RatingBar) rootView.findViewById(R.id.ratingbar_feedback_disponibilita);
        cordialita = (RatingBar) rootView.findViewById(R.id.ratingbar_cordialita);
        soddisfazione = (RatingBar) rootView.findViewById(R.id.ratingbar_soddisfazione);
        general_ratingbar = (RatingBar) rootView.findViewById(R.id.ratingbar_feedback);
        text = (EditText) rootView.findViewById(R.id.editText);
        date = (TextView) rootView.findViewById(R.id.dateTextView);
        checkBox = (CheckBox) rootView.findViewById(R.id.checkBox_respons);
        checkBoxAnonymus = (CheckBox) rootView.findViewById(R.id.checkBox_anonymus);
        text = (EditText) rootView.findViewById(R.id.editText);
        dove = (EditText) rootView.findViewById(R.id.editTextDove);
        tipo = (EditText) rootView.findViewById(R.id.editTextTipo);

        general_ratingbar.setIsIndicator(true);
        disponibilita.setOnRatingBarChangeListener(this);
        cordialita.setOnRatingBarChangeListener(this);
        soddisfazione.setOnRatingBarChangeListener(this);
        date.setOnClickListener(this);

        today = new SimpleDateFormat("dd/MM/yyyy");
        today_string=today.format(Calendar.getInstance().getTime());
        date.setText(today_string);

        Toast.makeText(this.getContext() , "Descrivi la visita!", Toast.LENGTH_SHORT).show();

        send = (TextView) rootView.findViewById(R.id.invia);

        TextView cancel = (TextView) rootView.findViewById(R.id.annulla);
        cancel.setOnClickListener(this);

        now = Calendar.getInstance().getTime();

        // Create the AlertDialog object and return it
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (general_float == 0
                        || disponibilita_float == 0
                        || cordialita_float == 0
                        || soddisfazione_float == 0)
                    Util.SnackBarFiga(null, v, "Assegna le stelle al feedback!");


                else if (text.getText().length() == 0)
                    Util.SnackBarFiga(DoctorActivity.fabfeedback,
                            v, "Descrivi la visita!");

                else if (dove.getText().length() == 0)
                    Util.SnackBarFiga(null, v, "Scrivi il luogo della visita!");

                else if (tipo.getText().length() == 0)
                    Util.SnackBarFiga(null, v, "Scrivi il tipo di visita!");

                else if (text.getText().length() == 0)
                    Toast.makeText(c, "Descrivi la visita!", Toast.LENGTH_SHORT).show();
                else if (text.getText().toString().split(" ").length < 10)
                    Util.SnackBarFiga(null, v, "I1 Feedback deve contenere almeno 10 parole!");
                else if(date_visit==null)
                    Util.SnackBarFiga(null, v, "Scegli la data");
                else if (date_visit.after(now))
                    Util.SnackBarFiga(null, v, "Scegli la data corretta!");

                else if (!checkBox.isChecked())
                    Util.SnackBarFiga(null, v, "Dichiara che la tua visita è stata veramente effettuata. " +
                            "Attenzione! Verranno effettuati controlli");
                else if (!Util.isOnline(getActivity()))
                    Util.SnackBarFiga(null, v, "Controlla la tua connessione a Internet!");
                else {
                    if(date_visit!=null)Log.d("Date visit",date_visit.toString());
                    Log.d("Now",now.toString());
                    boolean after=date_visit.after(now);
                    Log.d("Date visit after", after + "");
                    pushFeedback(rootView, email_user, email_doctor, checkBoxAnonymus.isChecked());

                }
            }
        });



        return builder.create();
    }


    public void controlToSend() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.annulla:
                FeedbackDialogFragment.this.dismiss();
                break;
            case R.id.invia:
                controlToSend();
                //FeedbackDialogFragment.this.dismiss();

                break;
            case R.id.dateTextView:
                showDatePickerDialog(v);
                break;
            default:
                break;
        }

    }


    private void controlFeedback(View rootView, String email_user, String email_doctor) {


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Feedback");
        query.whereEqualTo("email_user", email_user);
        query.whereEqualTo("email_doctor", email_doctor);
        try {
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (object != null) {

                        //Log.d("controFeedback", "User feedback alredy given for this doctor, editing mode");

                        String feedback_description = object.getString("feedback_description");

                        if (feedback_description != null) {
                            //Log.d("Feedback", feedback_description.toString());
                            //Log.d("Feedback", (object.get("Anonymus")).toString());
                            checkBoxAnonymus.setChecked(object.getBoolean("Anonymus"));
                            text.setText(feedback_description);
                            general_ratingbar.setRating(Float.parseFloat(object.get("Rating").toString()));

                            Object o;
                            o = object.get("disponibilita_rating");
                            if (o != null) {
                                Float d = Float.parseFloat(o.toString());
                                disponibilita.setRating(d);
                            }
                            o = object.get("cordialita_rating");
                            if (o != null) {
                                Float d = Float.parseFloat(o.toString());
                                cordialita.setRating(d);
                            }
                            o = object.get("soddisfazione_rating");
                            if (o != null) {
                                Float d = Float.parseFloat(o.toString());
                                soddisfazione.setRating(d);
                            }
                            String date_visit = object.get("date_visit").toString();

                            date.setText(date_visit);

                            tipo.setText(object.get("type").toString());
                            dove.setText(object.get("place").toString());


                            //cordialita.setRating(Float.parseFloat(object.get("cordialita_rating").toString()));
                            //soddisfazione.setRating(Float.parseFloat(object.get("soddisfazione_rating").toString()));


                        }
                    }
                }
            });
        }catch (NullPointerException e){
            e.printStackTrace();
        }catch( IllegalStateException i){
            i.printStackTrace();
        }


    }

    private void pushFeedback(View rootView, final String email_user, final String email_doctor, final boolean anonymus) {

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
            public void done(final ParseObject object, ParseException e) {
                if (object != null) {

                    //case editing mode
                    //Log.d("controFeedback", "User feedback alredy given for this doctor, editing mode");
                    object.put("email_user", email_user);
                    object.put("email_doctor", email_doctor);
                    object.put("Rating", general_float);
                    object.put("Anonymus", anonymus);
                    object.put("feedback_description", feedback_description);

                    object.put("type",tipo.getText().toString());
                    object.put("place", dove.getText().toString());
                    object.put("cordialita_rating",cordialita_float);
                    object.put("disponibilita_rating",disponibilita_float);
                    object.put("soddisfazione_rating",soddisfazione_float);
                    String date_visit_string="";
                    date_visit_string+=date_visit.getDay()+"/";
                    date_visit_string+= date_visit.getMonth()+"/";
                    date_visit_string+=date_visit.getYear();
                    object.put("date_visit",date_visit_string);


                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    object.put("date", simpleDateFormat.format(Calendar.getInstance().getTime()));

                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) Log.d("Push feedback", e.toString());
                            //Log.d("Push feedback", "feedback saved");
                            //DoctorActivity.showToastFeedback();
                            Util.SnackBarFiga(DoctorActivity.fabfeedback, DoctorActivity.coordinator_layout,
                                    "Feedback Inviato, Grazie!");


                            FeedbackFragment.feedbackAdapter.changeMyFeedback();
                            Util.calculateFeedback(email_doctor);
                        }
                    });
                } else {


                    //case never inserted feedback
                    final ParseObject feedback = new ParseObject("Feedback");
                    feedback.put("email_user", email_user);
                    feedback.put("email_doctor", email_doctor);
                    feedback.put("Name", name);
                    feedback.put("Anonymus", anonymus);
                    feedback.put("feedback_description", feedback_description);
                    feedback.put("Rating", new Float(ratingbar.getRating()));
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    feedback.put("date", simpleDateFormat.format(Calendar.getInstance().getTime()));
                    feedback.put("thumb_list", new ArrayList<String>());
                    feedback.put("num_thumb", 0);

                    feedback.put("type",tipo.getText().toString());
                    feedback.put("place", dove.getText().toString());
                    feedback.put("cordialita_rating",cordialita_float);
                    feedback.put("disponibilita_rating",disponibilita_float);
                    feedback.put("soddisfazione_rating",soddisfazione_float);
                    String date_visit_string="";
                    date_visit_string+=date_visit.getDay()+"/";
                    date_visit_string+= date_visit.getMonth()+"/";
                    date_visit_string+=date_visit.getYear();
                    assert object != null;
                    feedback.put("date_visit",date_visit_string);


                    feedback.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) Log.d("Push feedback", e.toString());
                            Util.SnackBarFiga(DoctorActivity.fabfeedback, DoctorActivity.coordinator_layout,
                                    "Feedback Inviato, Grazie!");

                            FeedbackFragment.feedbackAdapter.insertItem(feedback);
                            Util.calculateFeedback(email_doctor);
                            DoctorFragment.plus1();
                            //Log.d("Push feedback", "feedback saved");
                        }
                    });
                }

            }
        });


    }


    private float calculatefeedback(float f1, float f2, float f3) {
        return (f1 + f2 + f3) / 3;
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(DoctorActivity.fragmentActivity, "datePicker");
    }


    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

        disponibilita_float = disponibilita.getRating();
        cordialita_float = cordialita.getRating();
        soddisfazione_float = soddisfazione.getRating();
        general_float = calculatefeedback(disponibilita_float, cordialita_float, soddisfazione_float);
        general_ratingbar.setRating(general_float);

    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog dp=new DatePickerDialog(getActivity(), this, year, month, day);
            return dp;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            DatePicker dp=(DatePicker) view;
            Calendar c= Calendar.getInstance();
            c.set(year, month, day);
            date_visit=  c.getTime() ;
            //date_visit=new Date(year,month,day);
            Log.d("Date visit", date_visit.toString());
            Log.d("Today", now.toString());


            date.setText("" + day + "/" + month + "/" + year);

           // today = new SimpleDateFormat("dd/MM/yyyy");
            //today_string=today.format(Calendar.getInstance().getTime());
        }
    }


}