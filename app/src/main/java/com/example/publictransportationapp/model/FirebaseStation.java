package com.example.publictransportationapp.model;

import java.util.ArrayList;
import java.util.Objects;

public class FirebaseStation {

    private String stationName;
    private double latitude, longitude;
    private ArrayList<FirebaseRoute> routesThatPassThroughStation;

    public FirebaseStation(String stationName, double latitude, double longitude) {
        this.stationName = stationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.routesThatPassThroughStation = new ArrayList<>();
    }

    public ArrayList<String> getRouteNamesThatPassThroughStation()
    {
        ArrayList<String> names = new ArrayList<>();
        for(FirebaseRoute route : routesThatPassThroughStation)
        {
            names.add(route.getRouteName());
        }
        return names;
    }
    public ArrayList<FirebaseRoute> getRoutesThatPassThroughStation()
    {
        return this.routesThatPassThroughStation;
    }

    public void addRouteThroughStation(FirebaseRoute firebaseRoute)
    {
        if(!routesThatPassThroughStation.contains(firebaseRoute))
            routesThatPassThroughStation.add(firebaseRoute);
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
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

    @Override
    public String toString() {
        return "FirebaseStation{" +
                "stationName='" + stationName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FirebaseStation that = (FirebaseStation) o;
        return Double.compare(that.latitude, latitude) == 0 && Double.compare(that.longitude, longitude) == 0 && Objects.equals(stationName, that.stationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationName, latitude, longitude);
    }
}


