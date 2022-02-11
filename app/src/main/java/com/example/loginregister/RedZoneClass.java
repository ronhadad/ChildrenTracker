package com.example.loginregister;

import com.example.loginregister.polygon.Point;
import com.example.loginregister.polygon.Polygon;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

public class RedZoneClass {
    String redZoneName;
    LatLng[] points;
    Polygon polygon;
    Boolean insideRedZone;
    public RedZoneClass(String redZoneName, LatLng[] points) {
        this.redZoneName = redZoneName;
        this.points = points;
        this.polygon = Polygon.Builder()
                .addVertex(new Point(points[0].latitude, points[0].longitude))
                .addVertex(new Point(points[1].latitude, points[1].longitude))
                .addVertex(new Point(points[2].latitude, points[2].longitude))
                .addVertex(new Point(points[3].latitude, points[3].longitude))
                .build();
        this.insideRedZone=false;
    }

    public RedZoneClass() {
    }

    public String getRedZoneName() {
        return redZoneName;
    }

    public void setRedZoneName(String redZoneName) {
        this.redZoneName = redZoneName;
    }

    public LatLng[] getPoints() {
        return points;
    }

    public void setPoints(LatLng[] points) {
        this.points = points;
    }
}
