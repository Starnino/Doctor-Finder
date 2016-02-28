package com.doctorfinderapp.doctorfinder.Class;

/**
 * Created by francesco on 27/02/16.
 */

public class Doctor extends Person {

    private String profession, surname;
    boolean sesso; //true --> M, false --> F

    public Doctor(String name, String surname, int photoId, String profession, boolean sesso) {
        super(name, photoId);
        this.profession = profession;
        this.sesso = sesso;
    }

    @Override
    public int getPhotoId() {
        return super.getPhotoId();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    public String getProfession() {
        return profession;
    }

    public boolean havePisello() {return sesso;}

    public String getSurname() {return surname;}
}