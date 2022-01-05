package com.example.publictransportationapp.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Transport {

    String transportType, routeName;
    ArrayList<String> directions = new ArrayList<>();
    HashMap<String, ArrayList <Station>> stationsMap = new HashMap<>(); //Stations = Map(Direction1 : [{Station 1 : time 1}, {Station 2 : time 2}...]

    public Transport(){}

    public Transport(String transportType, String routeName) {
        this.transportType = transportType;
        this.routeName = routeName;
    }

    public void addDirection(String direction){
        if(!directions.contains(direction))
        {
            directions.add(direction);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)  //set API lvl to 24
    public void addStations(String direction, ArrayList <Station> stations){
        stationsMap.putIfAbsent(direction, stations);
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
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


    public void setStations(HashMap<String, ArrayList <Station>> stations) {
        this.stationsMap = stations;
    }

    @Override
    public String toString() {
        return "Transport{" +
                "transportType='" + transportType + '\'' +
                ", routeName='" + routeName + '\'' +
                ", directions=" + directions +
                ", stations=" + stationsMap +
                '}';
    }

    public ArrayList<Station> getStations(String direction) {
        if(stationsMap.containsKey(direction))
            return stationsMap.get(direction);
        return null;
    }
    public HashMap<String, ArrayList<Station>> getStationsMap()
    {
        return this.stationsMap;
    }
}
