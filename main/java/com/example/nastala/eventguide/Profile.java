package com.example.nastala.eventguide;

import android.graphics.Bitmap;

/**
 * Created by Nastala on 20.12.2016.
 */

public class Profile {
    private Bitmap profileImage;
    private String name, username, city, password, attributes;
    private int age;


    public Profile() {
    }


    public Profile(Bitmap profileImage, String name, String username, String city, int age, String password, String attributes) {
        this.profileImage = profileImage;
        this.name = name;
        this.username = username;
        this.city = city;
        this.age = age;
        this.password = password;
        this.attributes = attributes;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
