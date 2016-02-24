package com.doctorfinderapp.doctorfinder.functions;

import android.util.Log;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.Arrays;


public class UsersDB {

    private static ParseObject fQuery=null;

    private static void UsersDB(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");

        try {
            fQuery=query.getFirst();
        }

        catch (ParseException e) {
            Log.d("UsersDB Class",e.getMessage());
        }

        ParseObject User = new ParseObject("User");
        User.put("_id", GlobalVariable.idUsers);
    }
}