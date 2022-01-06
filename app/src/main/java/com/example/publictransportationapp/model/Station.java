package com.example.publictransportationapp.model;

public class Station implements ItemInterface {
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

    @Override
    public String toString() {
        return "Station{" +
                "stationName='" + stationName + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                '}';
    }

    @Override
    public boolean isSection() {
        return false;
    }
}
