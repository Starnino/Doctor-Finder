package com.doctorfinderapp.doctorfinder.functions;

import android.content.Context;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;

/**
 * Created by fedebyes on 11/02/16.
 */
public class StartParse {

    private static final String TAG = "StartParse" ;

    public static void startParse(Context c){
        Parse.enableLocalDatastore(c);
        try {
            Parse.initialize(new Parse.Configuration.Builder(c)
                            .applicationId("VfxaK2Tk5qApR5nvAulR")
                            .clientKey("wIgqO6QfRM4vFuD3pWJi")
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
