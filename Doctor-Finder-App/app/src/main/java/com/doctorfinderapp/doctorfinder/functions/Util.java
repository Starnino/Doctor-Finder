package com.doctorfinderapp.doctorfinder.functions;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
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

    public static String setSpecialization(ArrayList<String> specialization){

        String specializationString="";
        //divido le spec
        specializationString += specialization.get(0);

        if (specialization.size() > 1) {
            if (specialization.get(0).length() >= 12)
                specializationString += ", " + specialization.get(1).subSequence(0, 6) + "...";
            else
                if (specialization.get(1).length() < 12)
                    specializationString += ", " + specialization.get(1);
                else specializationString += ", " + specialization.get(1).subSequence(0,6) + "...";
        }
        return specializationString;
        /**finish setting specialization*/
    }

    public static String[][] setPosition(ArrayList<HashMap> position){
        String[][] positionString = new String[position.size()][2];

        HashMap currentMap;

        if (position.size() == 1) {
            currentMap = position.get(0);
            positionString[0][0]= currentMap.get("Lat").toString();
            positionString[0][1]= currentMap.get("Long").toString();
        }else{
            for (int i = 0; i <position.size() ; i++) {
                currentMap = position.get(i);
                positionString[i][0]= currentMap.get("Lat").toString();
                positionString[i][1]= currentMap.get("Long").toString();
            }
        }
        return positionString;
    }

    public static String reduceString(String string){
        if (string.length() > 12) return string.substring(0, 12) + "...";
        else return string;
    }

    public static String setCity(ArrayList<String> city){
        String res = "";
        for (int i = 0; i < city.size(); i++) {
            if (i == city.size()-1) res += city.get(i);
            else res += city.get(i) + ", ";
        } return res;
    }



    public static Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/1523582397968672"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/dcfind"));
        }
    }


    public ParseObject getDoctorFromList(ArrayList<ParseObject> list, int index){
        return list.get(index);
    }

    public static Bitmap downloadPhotoDoctor(ParseObject Doctor){
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

    public static List<ParseObject> getUserFacebookFriends(ParseUser user){
        List<String> id = new ArrayList<>();
        List<ParseObject> friends = new ArrayList<>();
        if (user == null) return friends;
        if (!user.getString(FACEBOOK).equals("true")) return friends;
        if (user.getString(FRIENDS).equals(null)) return friends;
        if (user.getString(FACEBOOK).equals("true")){
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


    public static void calculateFeedback(final String doctor_email){


        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Doctor");
        query.whereEqualTo("Email", doctor_email);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(final ParseObject doctor, ParseException e) {
                if (doctor == null) {
                    Log.d("calculate feedback", "Error doctor not exists ");
                } else {
                    ParseQuery query2 = ParseQuery.getQuery("Feedback");
                    query2.whereEqualTo("email_doctor", doctor_email);
                    query2.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            Float somma=0f;
                            for(int i=0;i< objects.size();i++){
                                String f=objects.get(i).get("Rating").toString();
                                Log.d("Feedback", f);
                                somma= somma + Float.parseFloat(f);
                            }
                            Float media=somma/objects.size();
                            Log.d("Feedback", "object.size() "+objects.size()+"");
                            Log.d("Feedback", "somma "+somma+"");
                            Log.d("Feedback", "media "+media+"");
                            doctor.put("Feedback", media);
                            doctor.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    Log.d("Feedback", doctor_email + " salvato");
                                }
                            });


                        }


                    });


                }


                }
        });
    }
}
