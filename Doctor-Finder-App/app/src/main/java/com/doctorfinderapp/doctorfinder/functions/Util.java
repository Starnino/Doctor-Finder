package com.doctorfinderapp.doctorfinder.functions;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;
import android.widget.ImageView;

import com.doctorfinderapp.doctorfinder.R;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by francesco on 02/03/16.
 */
public class Util {
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

    public static String setCity(ArrayList<String> city){
        String res = "";
        for (int i = 0; i < city.size(); i++) {
            if (i == city.size()-1) res += city.get(i);
            else res += city.get(i) + ", ";
        } return res;
    }

    /*public static void getUserImage(ParseUser user){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserPhoto");
        query.whereEqualTo("username", user.getEmail());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject userPhoto, ParseException e) {
                //userphoto exists

                if (userPhoto==null){
                    Log.d("userphoto","isnull");
                }
                //todo
               ParseFile file = (ParseFile) userPhoto.get("profilePhoto");
                file.getDataInBackground(new GetDataCallback() {
                    public void done(byte[] data, ParseException e) {
                        if (e == null) {
                            // data has the bytes for the resume
                            //data is the image in array byte
                            //must change image on profile
                            GlobalVariable.UserPropic = BitmapFactory.decodeByteArray(data, 0, data.length);
                            Log.d("Userphoto","downloaded");

                            ImageView mImg =  findViewById(R.id.user_propic);
                            mImg.setImageBitmap(img);
                            //iv.setImageBitmap(bitmap );

                        } else {
                            // something went wrong
                            Log.d("UserPhoto ", "problem download image");
                        }
                    }
                });

            }
        });

    }*/

}
