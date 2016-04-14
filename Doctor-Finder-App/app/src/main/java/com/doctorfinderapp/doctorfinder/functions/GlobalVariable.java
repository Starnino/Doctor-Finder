package com.doctorfinderapp.doctorfinder.functions;

import android.graphics.Bitmap;
import com.doctorfinderapp.doctorfinder.objects.Doctor;
import com.parse.ParseObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GlobalVariable {

    public static boolean FLAG_DIALOG = false;
    public static String idDoctors;

    public static List<String> permissions = Arrays.asList("email", "public_profile", "user_friends");

    public static List<ParseObject> DOCTORS=new ArrayList<>();

    public static Bitmap UserPropic=null;
    public static List<byte[]> DOCTORPHOTO;

    public static String URLDoctorForm= "http://goo.gl/forms/0zI6yS6ox6";
}
