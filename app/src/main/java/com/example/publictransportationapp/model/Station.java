package com.example.publictransportationapp.model;

import java.util.Date;

public class Station {
    String stationName;
    String arrivalTime;

    public Station(){}

    public Station(String stationName, String arrivalTime) {
        this.stationName = stationName;
        this.arrivalTime = arrivalTime;
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
