package com.doctorfinderapp.doctorfinder.functions;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;

/**
 * Created by fedebyes on 11/02/16.
 */
public class App extends Application{

    private static final String TAG = "StartParse" ;

    @Override public void onCreate() {
        //Log.d(TAG,"on create Start parse");
        startParse(getApplicationContext());
        super.onCreate();


    }

    private static void startParse(Context c){

        try {
            //Parse.enableLocalDatastore(c);
            Parse.initialize(new Parse.Configuration.Builder(c)
                            .applicationId("VfxaK2Tk5qApR5nvAulR")
                            .clientKey("wIgqO6QfRM4vFuD3pWJi")
                            .enableLocalDataStore()
                            .server("http://doctor-finder-server.herokuapp.com/parse/")
                            .build()

            );
            ParseFacebookUtils.initialize(c);
            ParseInstallation.getCurrentInstallation().saveInBackground();
        } catch (Exception e) {
            e.printStackTrace();
            Log.v(TAG, "error tipo" + e.toString());
        }

    }
}
