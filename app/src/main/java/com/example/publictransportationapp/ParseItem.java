package com.example.publictransportationapp;

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
    }
}
