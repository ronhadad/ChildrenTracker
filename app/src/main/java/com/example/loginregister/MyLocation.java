package com.example.loginregister;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;

public class MyLocation {
    String mail;
    double latitude;
    double longitude;
    String date;
    String time;
    String dateTimeFormat;

    public MyLocation(String mail, double latitude, double longitude, String date, String time, String dateTimeFormat) {
        this.mail = mail;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.time = time;
        this.dateTimeFormat = dateTimeFormat;
    }

    public MyLocation() {
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    public void setDateTimeFormat(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }
}
