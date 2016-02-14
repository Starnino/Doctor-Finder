package com.doctorfinderapp.doctorfinder.Doctor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fedebyes on 14/02/16.
 */
public class Specialization {
    private List<String> e;

    public Specialization(String s){
        if(e==null){
            e= new ArrayList<>();
            e.add(s);
        }
        else{
            e.add(s);
        }
    }
    public List<String>getSpecialization(){
        return e;
    }
    //todo methods add and remove
}
