package com.example.nastala.eventguide;

import android.graphics.Bitmap;

/**
 * Created by Nastala on 22.12.2016.
 */

public class Events {
    private Bitmap image;
    private String title, detail, date, city;
    private int event_id;

    public Events(int event_id, String title, String detail, String date, String city, Bitmap image){
        this.event_id = event_id;
        this.title = title;
        this.detail = detail;
        this.date = date;
        this.city = city;
        this.image = image;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
