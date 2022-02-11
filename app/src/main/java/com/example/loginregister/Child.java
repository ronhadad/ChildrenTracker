package com.example.loginregister;

import java.util.ArrayList;

public class Child {
    String username;
    double lat;
    double lon;
    String date;
    String time;
    String dateTimeFormat;
    String gender;
    ArrayList<RedZoneClass> redZones;

    public Child(String username, double lat, double lon, String date, String time, String dateTimeFormat, String gender, ArrayList<RedZoneClass> redZones) {
        this.username = username;
        this.lat = lat;
        this.lon = lon;
        this.date = date;
        this.time = time;
        this.dateTimeFormat = dateTimeFormat;
        this.gender = gender;
        this.redZones = redZones;
    }

    public ArrayList<RedZoneClass> getRedZones() {
        return redZones;
    }

    public void setRedZones(ArrayList<RedZoneClass> redZones) {
        this.redZones = redZones;
    }

    public Child() {
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
