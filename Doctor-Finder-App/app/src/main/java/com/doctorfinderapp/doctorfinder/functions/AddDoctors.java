package com.doctorfinderapp.doctorfinder.functions;

import android.util.Log;

import com.doctorfinderapp.doctorfinder.Doctor.Doctor;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

//import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by fedebyes on 12/02/16.
 */
public class AddDoctors {


    private static boolean exist;

    public static void AddDoctors(String FirstName,String LastName, String email, String data,
                                  String[] Specialization, String[] Work,
                                  String cellphone, String description
    ){

            Exist(email);
            if(exist){
                Log.d("Add doctor","exist");
            }
        else {
                ParseObject Doctor = new ParseObject("Doctor");
                Doctor.put("FirstName", FirstName);
                Doctor.put("LastName", LastName);
                Doctor.put("Email", email);

                Doctor.put("Specialization", Arrays.asList(Specialization));
                //todo photo
                Doctor.put("Work", Arrays.asList(Work));
                Doctor.put("Cellphone", cellphone);
                Doctor.put("Description", description);
                Doctor.saveInBackground();
                Log.d("Add doctor", "Saving doctor that not exist");

            }


    }

    public static void Exist(String email){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Doctor");
        //codice per vedere se la mail del dottore esiste nel database
        Log.d("add doctor","adding "+ email);

        exist= true;
        query.whereEqualTo("Email",email);
        query.getFirstInBackground(new GetCallback<ParseObject>()
        {
            public void done(ParseObject object, ParseException e)
            {
                if(e == null)
                {
                    //object exists
                    exist=true;
                    Log.d("add doctor","doctor exist");
                }
                else
                {
                    if(e.getCode() == ParseException.OBJECT_NOT_FOUND)
                    {
                        //object doesn't exist
                        exist=false;
                        Log.d("Add Doctors","doctor NOT exist");
                    }
                    else
                    {
                        //unknown error, debug
                        Log.d("Add Doctors","errore");
                        Log.d("Add Doctors",e.toString());

                    }
                }
            }
        });



    }

    public static Doctor getDoctor(String id){
        return null;
    }

    public boolean saveDoctorsLocal(){
        return true;
    }


}
