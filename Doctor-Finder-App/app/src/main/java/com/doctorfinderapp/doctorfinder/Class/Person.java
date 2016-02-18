package com.doctorfinderapp.doctorfinder.Class;


public class Person {
    private String name;
    private int photoId;

    public Person(String name, int photoId) {
        this.name = name;
        this.photoId = photoId;
    }

    public String getName() {return this.name;}
    public int getPhotoId() {return this.photoId;}
}