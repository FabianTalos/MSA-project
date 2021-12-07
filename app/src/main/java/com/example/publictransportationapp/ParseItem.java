package com.example.publictransportationapp;

import android.util.Log;

public class ParseItem {
    private String routeName;
    private String transportName;
    private String stationName;
    private String arrivalTime;

    public ParseItem() {
    }

    public ParseItem(String routeName, String transportName, String stationName, String arrivalTime) {
        this.routeName = routeName;
        this.transportName = transportName;
        this.stationName = stationName;
        this.arrivalTime = arrivalTime;

        Log.d("Initializer", "Initialized routeName: " + routeName + " transportName: " + transportName +
                " stationName: " + stationName + " arrivalTime: " + arrivalTime);
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getTransportName() {
        return transportName;
    }

    public void setTransportName(String transportName) {
        this.transportName = transportName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
