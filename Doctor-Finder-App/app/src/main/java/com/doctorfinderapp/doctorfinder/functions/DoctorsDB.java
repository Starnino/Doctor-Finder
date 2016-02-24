package com.doctorfinderapp.doctorfinder.functions;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

//import java.lang.reflect.Array;
import java.util.Arrays;


public class DoctorsDB {

    private static ParseObject fQuery=null;

    private static void DoctorsDB(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Doctor");

        try {
            fQuery=query.getFirst();
        }

        catch (ParseException e) {
                Log.d("DoctorsDB Class",e.getMessage());
        }

        ParseObject Doctor = new ParseObject("Doctor");
        Doctor.put("_id", GlobalVariable.idDocotrs);
    }
}
