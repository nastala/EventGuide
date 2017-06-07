package com.example.nastala.eventguide;

import android.graphics.Bitmap;

/**
 * Created by Nastala on 20.12.2016.
 */

public class Settings {
    private Bitmap image;
    private String name;
    private String username;

    public Settings(Bitmap image, String name) {
        this.image = image;
        this.name = name;
        username = "";
    }

    public Settings(Bitmap image, String name, String username) {
        this.image = image;
        this.name = name;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
