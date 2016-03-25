package com.doctorfinderapp.doctorfinder.objects;

import com.doctorfinderapp.doctorfinder.functions.Util;

import java.util.ArrayList;

/**
 * Created by francesco on 27/02/16.
 */

public class Doctor {

    private String profession, surname, city, name;
    private boolean sesso; //true --> M, false --> F
    String email;

    public Doctor(String name, String surname, ArrayList<String> profession,
                  ArrayList<String> city, boolean sesso, String email) {

        this.email = email;
        this.name = name;
        this.profession = Util.setSpecialization(profession);
        this.sesso = sesso;
        this.surname = surname;
        this.city = Util.setCity(city);
    }



    public String getName() {return name;}

    public String getProfession() {
        return profession;
    }

    public boolean havePisello() {return sesso;}

    public String getCity() {return city;}

    public String getSurname() {return surname;}

    public String getEmail(){return email;}
}