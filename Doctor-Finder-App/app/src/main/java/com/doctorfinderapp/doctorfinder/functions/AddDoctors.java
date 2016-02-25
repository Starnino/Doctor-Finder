package com.doctorfinderapp.doctorfinder.functions;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.doctorfinderapp.doctorfinder.R;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

//import java.lang.reflect.Array;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Created by fedebyes on 12/02/16.
 */
public class AddDoctors {


    private static boolean exist;
    private static Context context;


    private static void AddDoctors(final String FirstName, final String LastName, final String email, String data,
                                  final String[] Specialization, final String[] Work, final String anni,
                                  final String cellphone, final String description, final String latlng)  {

        //codice per vedere se la mail del dottore esiste nel database
        Log.d("add doctor", "adding " + email);




        ParseQuery<ParseObject> query = ParseQuery.getQuery("Doctor");
        query.whereEqualTo("email", email);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
               /* ParseObject Doctor;
               if(objects.size()>0){  Doctor= objects.get(0);
                   Log.d("adddoctor","retri")
               } else {
                  Doctor=new ParseObject("Doctor");
               }

                Doctor.put("FirstName", FirstName);
                Doctor.put("LastName", LastName);
                Doctor.put("Email", email);
                Doctor.put("Marker", latlng);
                Doctor.put("Specialization", Arrays.asList(Specialization));
                Doctor.put("Work", Arrays.asList(Work));
                Doctor.put("Cellphone", cellphone);
                Doctor.put("Description", description);

                Bitmap avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.avatar);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                avatar.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();


                ParseFile file = new ParseFile("Doctorpropic.jpg", byteArray);
                file.saveInBackground();
                // Creazione di un ParseObject da inviare
                ParseObject doctorPhoto = new ParseObject("DoctorPhoto");
                doctorPhoto.put("username", email);
                doctorPhoto.put("profilePhoto", file);
                doctorPhoto.saveInBackground();


                Doctor.put("Years", anni);
                Doctor.saveInBackground();
                Log.d("Add doctor", "Saving doctor that not exist");
                Log.d("Doctor", "adding" + email);

*/

            }
        });












    }
    public  static void addData(Context c) {

        //CREATE DOCTORS
        context=c;
        LatLng Doc1 =new LatLng(38.121932, 13.357361);
        AddDoctors("Calogero Roaul", "Aiello", "roaulaiello@gmail.com", "25/05/1983",
                new String[]{"Oculistica"}, new String[]{"Via"}, "Tra 5 e 10", "+39.3408312029", "https://www.linkedin.com/in/federico-solignani-596a1661", Doc1.toString());
        LatLng Doc2 =new LatLng(41.9000, 12.5000);
        AddDoctors("Antonio", "De Cillis", "federico.solignani@gmail.com22", "26/12/1981",
                new String[]{"Ortopedia"}, new String[]{"Via"}, "Tra 5 e 10","0187738056", "https://www.linkedin.com/in/federico-solignani-596a1661", Doc2.toString());
        LatLng ROMA3 =new LatLng(41.9000, 12.5000);
        AddDoctors("Francesco", "Maiese", "aniellomaiese@msn.com", "26/12/1981",
                new String[]{"Chirurgia Generale"}, new String[]{"Via"}, "Tra 5 e 10", "0187738056", "https://www.linkedin.com/in/federico-solignani-596a1661", ROMA3.toString());
        LatLng ROMA4 =new LatLng(41.9000, 12.5000);
        AddDoctors("Lorenzo", "Gitto", "canossaelisa@gmail.com", "26/12/1981",
                new String[]{"Oculistica"}, new String[]{"Via"},"Tra 5 e 10", "0187738056", "https://www.linkedin.com/in/federico-solignani-596a1661", ROMA4.toString());
        LatLng ROMA5 =new LatLng(41.9000, 12.5000);
        AddDoctors("Elisa", "Canossa", "federico.solignani@gmail.com", "26/12/1981",
                new String[]{"Chirurgia Generale"}, new String[]{"Via"}, "Tra 5 e 10", "0187738056", "https://www.linkedin.com/in/federico-solignani-596a1661", ROMA5.toString());
        LatLng ROMA6 =new LatLng(41.9000, 12.5000);
        AddDoctors("Marta", "Dezi", "dezitommaso@gmail.com", "26/12/1981",
                new String[]{"Chirurgia Cardiovascolare"}, new String[]{"Via"}, "Tra 5 e 10","0187738056", "https://www.linkedin.com/in/federico-solignani-596a1661", ROMA6.toString());
        LatLng ROMA7 =new LatLng(41.9000, 12.5000);
        AddDoctors("Laura", "Salminto", "dezitommaso@gmail.com", "26/12/1981",
                new String[]{"Chirurgia Cardiovascolare"}, new String[]{"Via"}, "Tra 5 e 10", "0187738056", "https://www.linkedin.com/in/federico-solignani-596a1661", ROMA7.toString());
        LatLng ROMA8 =new LatLng(41.9000, 12.5000);
        AddDoctors("Cristina", "Ferri", "dezitommaso@gmail.com", "26/12/1981",
                new String[]{"Chirurgia Cardiovascolare"}, new String[]{"Via"}, "Tra 5 e 10", "0187738056", "https://www.linkedin.com/in/federico-solignani-596a1661", ROMA8.toString());

    /**/

        Log.d("main", "adding doctor post");
    }




}
