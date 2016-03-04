package com.doctorfinderapp.doctorfinder.functions;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
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
}
