package com.doctorfinderapp.doctorfinder.functions;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;


/**
 * Created by fedebyes on 13/02/16.
 */

public class FacebookProfile {

    private static String name;
    private static String email;
    private static String lastname;

    private static com.parse.ParseException ret = null;



   public static void getGraphRequest(ParseUser user) {

       GraphRequest request = GraphRequest.newMeRequest(
               AccessToken.getCurrentAccessToken(),
               new GraphRequest.GraphJSONObjectCallback() {
                   @Override
                   public void onCompleted(
                           JSONObject object,
                           GraphResponse response) {
                       Log.d("Graph Response", "user = " + object.toString());
                       Log.d("Graph Response", "Informazioni prelevate da Facebook");
                       try {
                           email = response.getJSONObject().getString("email");
                           lastname = response.getJSONObject().getString("last_name");
                           name = response.getJSONObject().getString("first_name");
                           Log.d("Graph Response", "email" +email);
                           Log.d("Graph Response", "lastname" +lastname);
                       } catch (JSONException e) {
                           e.printStackTrace();
                           Log.d("Graph Response","error JSON");
                       }


                       // Application code
                   }
               });
       Bundle parameters = new Bundle();
       parameters.putString("fields", "id,name,link,first_name,last_name");
       request.setParameters(parameters);
       request.executeAsync();

   }

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