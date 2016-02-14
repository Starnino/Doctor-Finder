package com.doctorfinderapp.doctorfinder.functions;

import android.util.Log;

import com.doctorfinderapp.doctorfinder.Doctor.Doctor;
import com.google.android.gms.maps.model.LatLng;
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

    private static void AddDoctors(String FirstName,String LastName, String email, String data,
                                  String[] Specialization, String[] Work,
                                  String cellphone, String description,String latlng
    ){

            //Exist(email);

        //codice per vedere se la mail del dottore esiste nel database
        Log.d("add doctor","adding "+ email);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Doctor");
        query.whereEqualTo("Email",email);
        exist= false;
        ParseObject fromQuery=null;

        try {
            fromQuery=query.getFirst();

        } catch (ParseException e) {
            if(e.equals(ParseException.OBJECT_NOT_FOUND)){
                Log.d("Add doctor","Parse exception doctorn not exist");
                exist=false;
            }


        }
        Log.d("add doctor","query result "+ fromQuery);


        Log.d("Add doctor","exist== "+exist);
        if(!fromQuery.equals(null)){
                Log.d("Add doctor","exist");
            }
        else {
                ParseObject Doctor = new ParseObject("Doctor");
                Doctor.put("FirstName", FirstName);
                Doctor.put("LastName", LastName);
                Doctor.put("Email", email);
                Doctor.put("Marker",latlng);

                Doctor.put("Specialization", Arrays.asList(Specialization));
                //todo photo
                Doctor.put("Work", Arrays.asList(Work));
                Doctor.put("Cellphone", cellphone);
                Doctor.put("Description", description);
                Doctor.saveInBackground();
                Log.d("Add doctor", "Saving doctor that not exist");

            }


    }
    public  static void addData() {
        /*
        public static void AddDoctors(String FirstName,String LastName, String email, Date data,
                                  String[] Specialization, String[] Work,
                                  String cellphone, String description
            ){
         */


        Log.d("main", "adding doctor");
        LatLng ROMA =new LatLng(41.9000, 12.5000);
        AddDoctors("Federico", "Solignani", "federico.solignani@gmail.com", "26/12/1981",
                new String[]{"Oculistica"}, new String[]{"Via"}, "0187738056", "https://www.linkedin.com/in/federico-solignani-596a1661", ROMA.toString());
        LatLng ROMA2 =new LatLng(41.9000, 12.5000);
        AddDoctors("Federico2", "Solignani", "federico.solignani@gmail.com22", "26/12/1981",
                new String[]{"Oculistica"}, new String[]{"Via"}, "0187738056", "https://www.linkedin.com/in/federico-solignani-596a1661", ROMA.toString());

        Log.d("main", "adding doctor post");
    }




}
