package com.doctorfinderapp.doctorfinder.Class;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.doctorfinderapp.doctorfinder.functions.RoundedImageView;
import com.doctorfinderapp.doctorfinder.functions.Util;

import java.util.ArrayList;

/**
 * Created by francesco on 27/02/16.
 */

public class Doctor {

    private String profession, surname, city, name;
    private boolean sesso; //true --> M, false --> F
    private RoundedImageView photo;
    private int photoId;

    public Doctor(String name, String surname, Bitmap bitmap, ArrayList<String> profession, ArrayList<String> city, boolean sesso) {

        photo.setImageBitmap(bitmap);
        this.name = name;
        this.profession = Util.setSpecialization(profession);
        this.sesso = sesso;
        this.surname = surname;
        this.city = Util.setCity(city);
    }

    public Doctor(String name, String surname, int photoid, ArrayList<String> profession, ArrayList<String> city, boolean sesso) {
        this.photoId = photoid;
        this.name = name;
        this.profession = Util.setSpecialization(profession);
        this.sesso = sesso;
        this.surname = surname;
        this.city = Util.setCity(city);
    }

    public int getPhotoId() {return photoId;}

    public RoundedImageView getPhoto(){return photo;}

    public String getName() {return name;}

    public String getProfession() {
        return profession;
    }

    public boolean havePisello() {return sesso;}

    public String getCity() {return city;}

    public String getSurname() {return surname;}
}