package com.doctorfinderapp.doctorfinder.functions;

import android.graphics.Bitmap;
import com.doctorfinderapp.doctorfinder.Class.Doctor;
import com.parse.ParseFile;
import com.parse.ParseObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GlobalVariable {

    public static String idDoctors;

    public static List<String> permissions = Arrays.asList("email", "public_profile", "user_friends");

    public static List<ParseObject> DOCTORS;

    public static Bitmap UserPropic=null;
    public static List<Doctor> recentDoctors = new ArrayList<>();
    public static boolean FLAG_CARD_DOCTOR_VISIBLE = false;
    public static boolean FLAG_CARD_SEARCH_VISIBLE = false;

    public static List<byte[]> DOCTORPHOTO;

}
