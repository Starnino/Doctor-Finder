package com.doctorfinderapp.doctorfinder.Doctor;

/**
 * * Created by vindel100 on 17/01/16.
 */
public class Doctor {
private String name;
private String special;
private String feedback;

public Doctor(String name, String special, String feedback) {
this.name = name;
this.special = special;
this.feedback = feedback;
            }

public String getName() {
return this.name;
     }

     public String getSpecial() {
     return this.special;
      }

    public String getFeedback() {
    return this.feedback;
    }
}

