package com.doctorfinderapp.doctorfinder.functions;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONException;


/**
 * Created by fedebyes on 13/02/16.
 */

public class FacebookProfile {

    private static String name;
    private static String email;
    private static String lastname;

    private static com.parse.ParseException ret = null;

    // Function to get information from FBuser and put them in user
    public static com.parse.ParseException getFacebookThings(ParseUser Puser) throws InterruptedException {
        //final View vi = v;
        final ParseUser user = Puser;

        // Prelevo informazioni da facebook
        Bundle parameters = new Bundle();


        //prendo l'id dell'user
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
                    user.put("name", name.trim());

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
                        System.out.println("debug: ret = "+ret.getMessage().toString());

                    }
                } catch (JSONException e) {
                    System.out.println("debug: eccezione nell'ottenere info da facebook");

                }
            }
        }
        ).executeAndWait();
        return ret;
    }
}