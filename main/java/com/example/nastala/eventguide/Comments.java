package com.example.nastala.eventguide;

import android.graphics.Bitmap;

/**
 * Created by Nastala on 22.05.2017.
 */

public class Comments {
    private String username, comment, date;
    private Bitmap profileImage, rate;

    public Comments(String username, String comment, Bitmap profileImage, Bitmap rate, String date){
        this.username = username;
        this.comment = comment;
        this.profileImage = profileImage;
        this.rate = rate;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }

    public Bitmap getRate() {
        return rate;
    }

    public void setRate(Bitmap rate) {
        this.rate = rate;
    }
}
