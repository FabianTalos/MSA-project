package com.example.publictransportationapp.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Transport {

    String transportType, routeName;
    ArrayList<String> directions;
    Map<String,Station> stations; //Stations = Map(Direction1 : [{Station 1 : time 1}, {Station 2 : time 2}...]

    public Transport(){}

    public Transport(String transportType, String routeName) {
        this.transportType = transportType;
        this.routeName = routeName;
        this.directions = new ArrayList<>();
        this.stations = new HashMap<>();
    }

    public void addDirection(String direction){
        if(!directions.contains(direction))
        {
            directions.add(direction);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)  //set API lvl to 24
    public void addStation(String direction, Station station){
        stations.putIfAbsent(direction, station);
    }

    public String getTransportType() {
        return transportType;
    }

    public String getRouteName() {
        return routeName;
    }

    public ArrayList<String> getDirections() {
        return directions;
    }

    public void setDirections(ArrayList<String> directions) {
        this.directions = directions;
    }

    public Map<String, Station> getStations() {
        return stations;
    }

    public void setStations(Map<String, Station> stations) {
        this.stations = stations;
    }

    @Override
    public String toString() {
        return "Transport{" +
                "transportType='" + transportType + '\'' +
                ", routeName='" + routeName + '\'' +
                ", directions=" + directions +
                ", stations=" + stations +
                '}';
    }
}
