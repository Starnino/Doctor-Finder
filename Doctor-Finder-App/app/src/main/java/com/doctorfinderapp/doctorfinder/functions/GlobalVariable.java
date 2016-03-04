package com.doctorfinderapp.doctorfinder.functions;

import android.graphics.Bitmap;
import com.doctorfinderapp.doctorfinder.Class.Doctor;
import com.parse.ParseObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GlobalVariable {

    public static String idDoctors;
    public static int idUsers;
    public static List<String> permissions = Arrays.asList("email", "public_profile", "user_friends");
    public static int numdoctors;
    public static List<ParseObject> DOCTORS;
    public static List<ParseObject> USER;
    public static boolean locationActive=false;
    public static Bitmap UserPropic=null;
    public static ArrayList<Doctor> recentDoctors = new ArrayList();
    public static boolean FLAG_CARD_DOCTOR_VISIBLE = false;
    public static boolean FLAG_CARD_SEARCH_VISIBLE = false;
    public static boolean FLAGCARDVISIBLE = false;

}
