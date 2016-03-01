package com.doctorfinderapp.doctorfinder.functions;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

//import java.lang.reflect.Array;
import java.util.Arrays;


public class DoctorsDB {

    public static ParseObject docQuery;

    public static void DoctorsDB(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Doctor");

        try {
            docQuery=query.getFirst();
        }

        catch (ParseException e) {
                Log.d("DoctorsDB Class",e.getMessage());
        }

        ParseObject Doctor = new ParseObject("Doctor");
        Doctor.put("_id", GlobalVariable.idDoctors);
    }

    public static ParseObject getQuery(){
        return docQuery;
    }

}
