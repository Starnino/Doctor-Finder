package com.doctorfinderapp.doctorfinder.functions;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.doctorfinderapp.doctorfinder.R;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;



public class AddDoctors {
    private static boolean exist;
    private static Context context;


    private static void AddDoctors2(final String FirstName, final String LastName, final String email, String data,
                                    final String[] Specialization, final String[] Work, final String anni,
                                    final String cellphone, final String description, final String latlng) {

        //codice per vedere se la mail del dottore esiste nel database

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Doctor");
        query.whereEqualTo("email", email);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (objects.isEmpty()) {
                    Log.d("add doctor", "adding " + email);
                    //IS EMPTY MUST CREATE DOCTOR
                    ParseObject Doctor = new ParseObject("Doctor");
                    Doctor.put("FirstName", FirstName);
                    Doctor.put("LastName", LastName);
                    Doctor.put("Email", email);
                    Doctor.put("Marker", latlng);
                    Doctor.put("Specialization", Arrays.asList(Specialization));
                    Doctor.put("Work", Arrays.asList(Work));
                    Doctor.put("Cellphone", cellphone);
                    Doctor.put("Description", description);
                    Doctor.put("Years", anni);


                    Bitmap avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.personavatar);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    avatar.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();


                    ParseFile file = new ParseFile("Doctorpropic.jpg", byteArray);
                    file.saveInBackground();
                    // Creazione di un ParseObject da inviare

                    //todo create photo only if doctor don't have one

                    ParseObject doctorPhoto = new ParseObject("DoctorPhoto");
                    doctorPhoto.put("username", email);
                    doctorPhoto.put("profilePhoto", file);
                    doctorPhoto.saveInBackground();


                    Doctor.saveInBackground();


                } else {

                    //IS NOT EMPTY
                    objects.get(0).put("FirstName", FirstName);
                    objects.get(0).put("LastName", LastName);
                    objects.get(0).put("Email", email);
                    objects.get(0).put("Marker", latlng);
                    objects.get(0).put("Specialization", Arrays.asList(Specialization));
                    objects.get(0).put("Work", Arrays.asList(Work));
                    objects.get(0).put("Cellphone", cellphone);
                    objects.get(0).put("Description", description);
                    Log.d("add doctor", "Updating doctor " + email);
                    objects.get(0).saveInBackground();

                }
                e.printStackTrace();

                Log.d("Add doctor", "Saving doctor that not exist");
                Log.d("Doctor", "adding" + email);

            }
        });


    }

    public static void addFile(final Resources res){
        int a=R.drawable.antonio_renna11_gmail_com;
        Bitmap bm = BitmapFactory.decodeResource(res, a);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] byteArrayImage = baos.toByteArray();
        ParseFile file = new ParseFile("fileprova_doctor_profile.jpg", byteArrayImage);

        try {
            file.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Creazione di un ParseObject da inviare
        ParseObject doctorPhoto = new ParseObject("DoctorPhoto");

        doctorPhoto.put("profilePhoto", file);
        try {
            doctorPhoto.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("addfile","file saved");
        ParseFile get = (ParseFile)doctorPhoto.get("profilePhoto");
        try {
            get.getData();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public static void addPhoto(final Resources res) {

        final int photId[] =
                {R.drawable.antonio_renna11_gmail_com,
                        R.drawable.antoniodecillis_virgilio_it,
                        R.drawable.canossaelisa_gmail_com,
                        R.drawable.cristinapoggi_psi_gmail_com,
                        R.drawable.daspoldi_tin_it,
                        R.drawable.dottordeigiudici_outlook_it,
                        R.drawable.fabiosichel_gmail_com,
                        R.drawable.federico_solignani_gmail_com,
                        R.drawable.gcalabretti_gmail_com,
                        R.drawable.graziaferramosca_gmail_com,
                        R.drawable.jessife_libero_it,
                        R.drawable.nicola_savarese_hotmail_it,
                        R.drawable.phisio_daniel_gmail_com,
                        R.drawable.psicologamilano_tiscali_it,
                        R.drawable.sarah_pederboni_gmail_com,
                        R.drawable.valeriagemmiti_gmail_com,
                        R.drawable.nicolina_capuano,
                        R.drawable.silviapiro_alice_it};
        final String emaildoc[] = {
                "antonio.renna11@gmail.com",
                "antoniodecillis@virgilio.it",
                "canossaelisa@gmail.com",
                "cristinapoggi.psi@gmail.com",
                "daspoldi@tin.it",
                "dottordeigiudici@outlook.it",
                "fabiosichel@gmail.com",
                "federico.solignani@gmail.com",
                "gcalabretti@gmail.com",
                "graziaferramosca@gmail.com",
                "jessife@libero.it",
                "nicola.savarese@hotmail.it",
                "phisio.daniel@gmail.com",
                "psicologamilano@tiscali.it",
                "sarah.pederboni@gmail.com",
                "valeriagemmiti@gmail.com",
                "capuanonicolina@gmail.com",
                "lucio.mucci@libero.it",
                "silviapiro@alice.it"


        };
        final String docid[] = {"56d76fb18f32d118c2ddab31", "56d76fb18f32d118c2ddab27",
                "56d76fb18f32d118c2ddab33", "56d76fb18f32d118c2ddab30", "56dc2c34e4b066b0efdff965",
                "56dc5a21e4b066b0efdffc70", "56dc2c07e4b066b0efdff95f", "56d76fb18f32d118c2ddab34",
                "56d76fb18f32d118c2ddab26", "56d76fb18f32d118c2ddab37", "56d76fb18f32d118c2ddab29",
                "56dc2bf3e4b066b0efdff95d", "56d76fb18f32d118c2ddab36", "56dc2b54e4b066b0efdff94d",
                "56dc2c7ae4b066b0efdff967", "56d76fb18f32d118c2ddab38", "56dc2baae4b066b0efdff956",
                "56deb3a1e4b088c9371befc8", "56dea028e4b0c05f88d04dd6"};


        Log.d("addphoto","length of email[]"+emaildoc.length);
        Log.d("addphoto","length of id[]"+docid.length);
        Log.d("addphoto","length of drawable[]"+photId.length);
        //for (int i = 0; i < emaildoc.length; i++) {
        for (int i = 0; i < emaildoc.length; i++) {
            //int i = 0;
            ParseQuery<ParseObject> query = ParseQuery.getQuery("DoctorPhoto");
            query.whereEqualTo("email", emaildoc[i]);
            final int ii = i;
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {

                    if (object == null) {
                        Bitmap bm = BitmapFactory.decodeResource(res, photId[ii]);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 70, baos); //bm is the bitmap object
                        byte[] byteArrayImage = baos.toByteArray();
                        ParseFile file = new ParseFile(emaildoc[ii]+"_doctor_profile.jpg", byteArrayImage);

                        //final ParseFile file = new ParseFile("_doctor_profile.png", byteArrayImage);
                        final ParseFile file1=file;
                       file.saveInBackground(new SaveCallback() {
                                                  @Override
                                                  public void done(ParseException e) {

                                                      Log.d("addPhoto", "file saved"+emaildoc[ii]);
                                                      final ParseObject drPhoto = new ParseObject("DoctorPhoto");
                                                      drPhoto.put("idDoctor", docid[ii]);
                                                      drPhoto.put("Email", emaildoc[ii]);
                                                      drPhoto.put("profilePhoto", file1);
                                                      if(e!=null){
                                                          Log.d("addPhoto",e.toString());
                                                      }


                                                      drPhoto.saveInBackground(new SaveCallback() {
                                                          @Override
                                                          public void done(ParseException e) {
                                                              Log.d("addPhoto", "photo saved " + emaildoc[ii]);
                                                              if(e!=null){
                                                                  Log.d("addPhoto",e.toString());


                                                              }
                                                              ParseFile get = (ParseFile)drPhoto.get("profilePhoto");
                                                              try {
                                                                  get.getData();
                                                              } catch (ParseException e1) {
                                                                  e1.printStackTrace();
                                                              }
                                                              Log.d("addPhoto","photosaved");


                                                          }
                                                      });
                                                  }
                                              }
                        );

                    }
                }

                //ParseFile file = new ParseFile("56d76fb18f32d118c2ddab37_doctor_profile.png",byteArrayImage);
                //file.saveInBackground();

            });


        }}

        public static void addData (Context c){

            //CREATE DOCTORS
        /*context=c;
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