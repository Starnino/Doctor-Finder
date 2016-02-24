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
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseObject;
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
                               String pictureUrl = data.getString("url");


                               userP.saveInBackground();
                               //Log.d("Graph Response",userP.getString("lName"));


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




                       } catch (JSONException e) {
                           e.printStackTrace();
                           Log.d("Graph Response","error JSON");
                       } catch (MalformedURLException e) {
                           e.printStackTrace();
                       } catch (com.parse.ParseException e) {
                           e.printStackTrace();
                       } catch (IOException e) {
                           e.printStackTrace();
                       }


                       // Application code
                   }
               });
       Bundle parameters = new Bundle();
       parameters.putString("fields", "id,name,email,link,first_name,last_name,friends,picture.type(large)");
       request.setParameters(parameters);
       request.executeAsync();

   }



    // DownloadImage AsyncTask
    //// Execute DownloadImage AsyncTask
    //new DownloadImage().execute(URL);
    /*private static class DownloadImage extends AsyncTask<String, Void, Bitmap> {



        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            //
            propic=result;
        }
    }*/

    // Function to get information from FBuser and put them in user
    /*public static com.parse.ParseException getFacebookThings(ParseUser Puser) throws InterruptedException {
        //final View vi = v;
        final ParseUser user = Puser;


        Bundle parameters = new Bundle();

        String userId = user.getObjectId();

        // parameters from facebook
        parameters.putString("fields", "email,first_name,last_name");

        new GraphRequest(
                AccessToken.getCurrentAccessToken(), userId, parameters, HttpMethod.GET, new GraphRequest.Callback() {
            public void onCompleted(GraphResponse response) {

                // Prelevo il risultato
                try {
                    email = response.getJSONObject().getString("email");
                    lastname = response.getJSONObject().getString("last_name");
                    name = response.getJSONObject().getString("first_name");


                    Log.d("FacebookUtil", "Informazioni prelevate da Facebook");

                    // Inserisco le info nel ParseUser
                    user.setEmail(email);
                    user.put("facebookName", name.trim());

                    try {

                        user.save();
                        //Log.v("facebook profile", user.getEmail());

                        //ParseObject persona = new ParseObject("Persona");
                        //persona.put("username",user.getUsername());
                        //persona.put("lastname", lastname.trim());

                        //persona.put("city",citta.trim());
                        // persona.save();
                    } catch (com.parse.ParseException e) {
                        ret = e;
                        System.out.println("debug: ret = " + ret.getMessage().toString());

                    }
                } catch (JSONException e) {
                    System.out.println("debug: eccezione nell'ottenere info da facebook");

                }
            }
        }
        ).executeAndWait();
        return ret;
    }
    //get facebook friends  of a user that use app
    public static void getFriends(ParseUser user){

    }

    //link current user to facebook account
    public static void Link(ParseUser  user,Activity a){
        List<String> permissions = Arrays.asList("email", "public_profile");
        if (!ParseFacebookUtils.isLinked(user)) {
            ParseFacebookUtils.linkWithReadPermissionsInBackground(user, a, permissions, new SaveCallback() {
                @Override
                public void done(com.parse.ParseException ex) {
                    ParseUser user1=ParseUser.getCurrentUser();
                    if (ParseFacebookUtils.isLinked(user1)) {
                        Log.d("MyApp", "Woohoo, user logged in with Facebook!");
                    }
                }
            });
        }
    }


*/
    /*

    For Android, your code to make your users query-able by Facebook ID would look like this:

ParseFacebookUtils.logIn(this, new LogInCallback() {
  @Override
  public void done(ParseUser user, ParseException error) {
    // When your user logs in, immediately get and store its Facebook ID
    if (user != null) {
      getFacebookIdInBackground();
    }
  }
});

private static void getFacebookIdInBackground() {
  Request.executeMeRequestAsync(ParseFacebookUtils.getSession(), new Request.GraphUserCallback() {
    @Override
    public void onCompleted(GraphUser user, Response response) {
      if (user != null) {
        ParseUser.getCurrentUser().put("fbId", user.getId());
        ParseUser.getCurrentUser().saveInBackground();
      }
    }
  });
}
Then, when you are ready to search for your user's friends, you would issue another request:

Request.executeMyFriendsRequestAsync(ParseFacebookUtils.getSession(), new Request.GraphUserListCallback() {

  @Override
  public void onCompleted(List<GraphUser> users, Response response) {
    if (users != null) {
      List<String> friendsList = new ArrayList<String>();
      for (GraphUser user : users) {
        friendsList.add(user.getId());
      }

      // Construct a ParseUser query that will find friends whose
      // facebook IDs are contained in the current user's friend list.
      ParseQuery friendQuery = ParseQuery.getUserQuery();
      friendQuery.whereContainedIn("fbId", friendsList);

      // findObjects will return a list of ParseUsers that are friends with
      // the current user
      List<ParseObject> friendUsers = friendQuery.find();
    }
  }
});
     */

    /*example asynctask
    private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
     protected Long doInBackground(URL... urls) {
         int count = urls.length;
         long totalSize = 0;
         for (int i = 0; i < count; i++) {
             totalSize += Downloader.downloadFile(urls[i]);
             publishProgress((int) ((i / (float) count) * 100));
             // Escape early if cancel() is called
             if (isCancelled()) break;
         }
         return totalSize;
     }

     protected void onProgressUpdate(Integer... progress) {
         setProgressPercent(progress[0]);
     }

     protected void onPostExecute(Long result) {
         showDialog("Downloaded " + result + " bytes");
     }
 }
     */
}