package com.doctorfinderapp.doctorfinder.functions;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by francesco on 02/03/16.
 */
public class Util {

    public static final String FRIENDS = "friends";
    public static final String FACEBOOK = "Facebook";
    public static final String USER = "_User";
    public static final String ID = "facebookId";
    public static final String NAME = "fName";
    public static final String SURNAME = "lName";
    public static final String EMAIl = "email";

    public static String setSpecialization(ArrayList<String> specialization) {

        String specializationString = "";
        //divido le spec
        specializationString += specialization.get(0);

        if (specialization.size() > 1) {
            if (specialization.get(0).length() >= 12)
                specializationString += ", " + specialization.get(1).subSequence(0, 6) + "...";
            else if (specialization.get(1).length() < 12)
                specializationString += ", " + specialization.get(1);
            else specializationString += ", " + specialization.get(1).subSequence(0, 6) + "...";
        }
        return specializationString;
        /**finish setting specialization*/
    }

    public static String[][] setPosition(ArrayList<HashMap> position) {
        String[][] positionString = new String[position.size()][2];

        HashMap currentMap;

        if (position.size() == 1) {
            currentMap = position.get(0);
            positionString[0][0] = currentMap.get("Lat").toString();
            positionString[0][1] = currentMap.get("Long").toString();
        } else {
            for (int i = 0; i < position.size(); i++) {
                currentMap = position.get(i);
                positionString[i][0] = currentMap.get("Lat").toString();
                positionString[i][1] = currentMap.get("Long").toString();
            }
        }
        return positionString;
    }

    public static String reduceString(String string) {
        if (string.length() > 12) return string.substring(0, 12) + "...";
        else return string;
    }

    public static String setCity(ArrayList<String> city) {
        String res = "";
        for (int i = 0; i < city.size(); i++) {
            if (i == city.size() - 1) res += city.get(i);
            else res += city.get(i) + ", ";
        }
        return res;
    }


    public static Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/1523582397968672"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/dcfind"));
        }
    }

    public static Bitmap downloadPhotoDoctor(ParseObject Doctor) {
        final String tablePhoto = "DoctorPh";
        final String columnId = "idDoctor";
        final String ID = Doctor.getObjectId();
        Bitmap photo = null;
        Log.d(columnId, ID);

        ParseQuery<ParseObject> queryPhoto = ParseQuery.getQuery(tablePhoto);
        queryPhoto.whereEqualTo(columnId, ID);

        queryPhoto.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

            }
        });
        return photo;
    }

    public static List<ParseObject> getUserFacebookFriends(ParseUser user) {
        List<String> id = new ArrayList<>();
        List<ParseObject> friends = new ArrayList<>();
        if (user == null) return friends;
        if (!user.getString(FACEBOOK).equals("true")) return friends;
        if (user.getString(FRIENDS).equals(null)) return friends;
        if (user.getString(FACEBOOK).equals("true")) {
            id = Arrays.asList(user.get(FRIENDS).toString().split(","));
        }
        for (int i = 0; i < id.size(); i++) {
            Log.d("AMICO --> ", id.get(i));
        }

        ParseQuery<ParseObject> friend = ParseQuery.getQuery(USER);
        friend.whereContainedIn(ID, id);

        //get query non in background
        try {
            friends = friend.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return friends;
    }

    public static void calculateFeedback(final String doctor_email) {

        /*final ParseACL postACL = new ParseACL(ParseUser.getCurrentUser());
        postACL.setPublicReadAccess(true);
        postACL.setPublicWriteAccess(true);*/
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Doctor");
        query.whereEqualTo("Email", doctor_email);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject doctor, ParseException e) {

                if (doctor == null) {
                    Log.d("calculate feedback", "Error doctor not exists ");
                } else {
                    //ACL
                    //doctor.setACL(postACL);
                    ParseQuery query2 = ParseQuery.getQuery("Feedback");
                    query2.whereEqualTo("email_doctor", doctor_email);
                    Log.d("Feedback", doctor.toString());
                    try {
                        List<ParseObject> objects = (List<ParseObject>) query2.find();
                        Float somma = 0f;
                        for (int i = 0; i < objects.size(); i++) {
                            String f = objects.get(i).get("Rating").toString();
                            Log.d("Feedback", f);
                            somma = somma + Float.parseFloat(f);
                        }
                        Log.d("Feedback2", doctor.toString());
                        float media = somma / objects.size();
                        Log.d("Feedback", "object.size() " + objects.size() + "");
                        Log.d("Feedback", "somma " + somma + "");
                        Log.d("Feedback", "media " + media + "");
                        //doctor.remove("Feedback");

                        //Log.d("Feedback", String.valueOf(doctor.getACL().getPublicReadAccess()));
                        //Log.d("Feedback", String.valueOf(doctor.getACL().getPublicWriteAccess()));

                        //Log.d("Feedback3", doctor.toString());
                        doctor.save();

                        doctor.put("Feedback", media);
                        doctor.save();


                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });



        /*query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(final ParseObject doctor, ParseException e) {

                if (doctor == null) {
                    Log.d("calculate feedback", "Error doctor not exists ");
                } else {
                    ParseQuery query2 = ParseQuery.getQuery("Feedback");

                    query2.whereEqualTo("email_doctor", doctor_email);
                    Log.d("Feedback", doctor.toString());
                    query2.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            ParseObject d=doctor;
                            Float somma = 0f;
                            for (int i = 0; i < objects.size(); i++) {
                                String f = objects.get(i).get("Rating").toString();
                                Log.d("Feedback", f);
                                somma = somma + Float.parseFloat(f);
                            }
                            float media = somma / objects.size();
                            Log.d("Feedback", "object.size() " + objects.size() + "");
                            Log.d("Feedback", "somma " + somma + "");
                            Log.d("Feedback", "media " + media + "");


                            Log.d("Feedback", d.toString());
                            d.put("Feedback", media);

                            d.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e != null) Log.d("calculate feedback", e.toString());
                                    Log.d("Feedback", doctor_email + " salvato");
                                }
                            });


                        }


                    });


                }


            }
        });*/
    }

    public ParseObject getDoctorFromList(ArrayList<ParseObject> list, int index) {
        return list.get(index);
    }

    public static void copyAll(){
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Doctor");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (int i =0;i<objects.size();i++){
                    ParseObject d=new ParseObject("Doctor2");
                    ParseObject DOCTORTHIS=objects.get(i);
                    String DOCTOR_FIRST_NAME = DOCTORTHIS.getString("FirstName");
                    String DOCTOR_LAST_NAME = DOCTORTHIS.getString("LastName");
                    String DOCTOR_EXPERIENCE = DOCTORTHIS.getString("Exp");
                    String DOCTOR_FEEDBACK = DOCTORTHIS.getString("Feedback");
                    String DOCTOR_EMAIL= DOCTORTHIS.getString("Email");
                    String DOCTOR_SEX = DOCTORTHIS.getString("Sesso");
                    String DOCTOR_DESCRIPTION = DOCTORTHIS.getString("Description");
                    ArrayList<String> DOCTOR_WORK_ARRAY = (ArrayList<String>) DOCTORTHIS.get("Work");
                    ArrayList<String> DOCTOR_CITY_ARRAY = (ArrayList<String>) DOCTORTHIS.get("Province");
                    ArrayList<String> DOCTOR_SPECIALIZATION_ARRAY = (ArrayList<String>) DOCTORTHIS.get("Specialization");
                    String DOCTOR_CELLPHONE=DOCTORTHIS.getString("Cellphone");
                    String DOCTOR_VISIT=DOCTORTHIS.getString("Visit");
                    String DOCTOR_BORN=DOCTORTHIS.getString("Born");
                    String DOCTOR_PRICE=DOCTORTHIS.getString("Price");
                    ArrayList<String> DOCTOR_Marker=(ArrayList<String>)DOCTORTHIS.get("Marker");

                    d.put("FirstName",DOCTOR_FIRST_NAME);
                    d.put("LastName",DOCTOR_LAST_NAME);
                    d.put("Exp",DOCTOR_EXPERIENCE);
                    d.put("Feedback",DOCTOR_FEEDBACK);
                    d.put("Email",DOCTOR_EMAIL );
                    d.put("Sesso",DOCTOR_SEX );
                    d.put("Description",DOCTOR_DESCRIPTION );
                    d.put("Work",DOCTOR_WORK_ARRAY );
                    d.put("Province",DOCTOR_CITY_ARRAY );
                    d.put("Specialization",DOCTOR_SPECIALIZATION_ARRAY );
                    d.put("Cellphone",DOCTOR_CELLPHONE );
                    d.put("Visit",DOCTOR_VISIT );
                    d.put("Born",DOCTOR_BORN );
                    d.put("Price",DOCTOR_PRICE );
                    d.put("Marker",DOCTOR_Marker );
                    try {
                        d.save();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }


                }



            }
        });


    }}
