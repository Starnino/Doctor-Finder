package com.doctorfinderapp.doctorfinder.functions;

import java.util.ArrayList;

/**
 * Created by francesco on 02/03/16.
 */
public class Util {
    public static String setSpecialization(ArrayList<String> specialization){

        String specializationString="";
        //divido le spec
        specializationString += specialization.get(0);

        if (specialization.size() > 1) {
            if (specialization.get(0).length() >= 12)
                specializationString += ", " + specialization.get(1).subSequence(0, 6) + "...";
            else
                if (specialization.get(1).length() < 12)
                    specializationString += ", " + specialization.get(1);
                else specializationString += ", " + specialization.get(1).subSequence(0,6) + "...";
        }
        return specializationString;
        /**finish setting specialization*/
    }

    public static String setCity(ArrayList<String> city){
        String res = "";
        for (int i = 0; i < city.size(); i++) {
            if (i == city.size()-1) res += city.get(i);
            else res += city.get(i) + ", ";
        } return res;
    }
}
