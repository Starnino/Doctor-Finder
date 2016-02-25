package com.doctorfinderapp.doctorfinder.functions;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.parse.FindCallback;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by fedebyes on 13/02/16.
 */

public class FacebookProfile {

    private  static String URL="";
    private static com.parse.ParseException ret = null;
    private static Bitmap propic;


   public static void getGraphRequest(final ParseUser   userP) {

       GraphRequest request = GraphRequest.newMeRequest(
               AccessToken.getCurrentAccessToken(),
               new GraphRequest.GraphJSONObjectCallback() {
                   @Override
                   public void onCompleted(
                           JSONObject object,
                           GraphResponse response) {

                       try {
                           Log.d("Graph Response", "user = " + response.toString());
                           Log.d("Graph Response", "Informazioni prelevate da Facebook");
                           if(object!=null) {

                               String email = response.getJSONObject().getString("email");
                               String lastname = response.getJSONObject().getString("last_name");
                               String firstname = response.getJSONObject().getString("first_name");
                               String friends = response.getJSONObject().getString("friends");

                               JSONObject friendsJSON = (JSONObject) response.getJSONObject().get("friends");

                               //Log.d("friendsJSON",friendsJSON.toString());
                               JSONArray friendsData = friendsJSON.getJSONArray("data");
                               //Log.d("friendsData",friendsData.toString());
                               List<String> friendsParse=new ArrayList<String>();
                               for(int i=0;i<friendsData.length();i++){
                                   String it= friendsData.getJSONObject(i).getString("id");
                                   friendsParse.add(it);

                               }

                               String facebookId = response.getJSONObject().getString("id");

                               //Log.d("friendsParse",friendsParse.toString());



                               //Log.d("Graph Response", "FriendS" + friends);

                               //Log.d("Graph Response", "email" + email);


                               userP.setUsername(email);
                               userP.setEmail(email);
                               userP.put("Facebook", "true");
                               userP.put("fName", firstname);
                               userP.put("lName",lastname);
                               userP.put("facebookId",facebookId);
                               //friends tha uses app

                               userP.put("friends", friendsParse.toString());
                               // URL=response.getJSONObject().getString("profile");

                               //dowload facebook image
                               //new DownloadImage().execute(URL);

                               JSONObject picture = response.getJSONObject().getJSONObject("picture");
                               JSONObject data = picture.getJSONObject("data");
                               final String pictureUrl = data.getString("url");


                               userP.saveInBackground();
                               //Log.d("Graph Response",userP.getString("lName"));

                               ParseQuery<ParseObject> query = ParseQuery.getQuery("UserPhoto");
                               query.whereEqualTo("username", email);
                               query.findInBackground(new FindCallback<ParseObject>() {
                                   public void done(List<ParseObject> results, com.parse.ParseException e) {

                                       try{
                                       if (e == null) {







                                          if(results.size()>0){
                                              //UserPhoto exists



                                          }else{

                                              //userphoto NOT exists
                                              //scarico l'immagine presa da facebook
                                              URL aURL = new URL(pictureUrl);
                                              URLConnection conn = aURL.openConnection();
                                              conn.connect();
                                              InputStream is = conn.getInputStream();
                                              BufferedInputStream bis = new BufferedInputStream(is);

                                              //comprilo l'immagine in byte[] e la invio come parseFile
                                              ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                              (BitmapFactory.decodeStream(bis)).compress(Bitmap.CompressFormat.JPEG, 70, stream);
                                              byte[] pp = stream.toByteArray();
                                              ParseFile file = new ParseFile("propic.jpg", pp);
                                              file.save();
                                              // Creazione di un ParseObject da inviare
                                              ParseObject userPhoto = new ParseObject("UserPhoto");
                                              userPhoto.put("username", userP.getUsername());
                                              userPhoto.put("profilePhoto", file);
                                              userPhoto.save();



                                          }


                                       } else {
                                           Log.d("Error get UserPhoto", "Error: " + e.getMessage());
                                       }
                                   }catch (IOException a) {
                                           e.printStackTrace();
                                       } catch (com.parse.ParseException e1) {
                                           e1.printStackTrace();
                                       }

                                   }
                               });

                           }
                     } catch (JSONException e) {
                           e.printStackTrace();
                           Log.d("Graph Response", "error JSON");
                       }



                   }
               });
       Bundle parameters = new Bundle();
       parameters.putString("fields", "id,name,email,link,first_name,last_name,friends,picture.type(large)");
       request.setParameters(parameters);
       request.executeAsync();

   }




}