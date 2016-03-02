package com.doctorfinderapp.doctorfinder.Class;

import com.doctorfinderapp.doctorfinder.functions.Util;

import java.util.ArrayList;

/**
 * Created by francesco on 27/02/16.
 */

public class Doctor extends Person {

    private String profession, surname, city;
    boolean sesso; //true --> M, false --> F

    public Doctor(String name, String surname, int photoId, ArrayList<String> profession, ArrayList<String> city, boolean sesso) {
        super(name, photoId);
        this.profession = Util.setSpecialization(profession);
        this.sesso = sesso;
        this.surname = surname;
        this.city = Util.setCity(city);
    }

    @Override
    public int getPhotoId() {return super.getPhotoId();}

    @Override
    public String getName() {
        return super.getName();
    }

    public String getProfession() {
        return profession;
    }

    public boolean havePisello() {return sesso;}

    public String getCity() {return city;}

    public String getSurname() {return surname;}
}