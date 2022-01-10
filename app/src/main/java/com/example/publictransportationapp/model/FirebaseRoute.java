package com.example.publictransportationapp.model;

import java.util.ArrayList;
import java.util.Objects;

public class FirebaseRoute {
    private String routeName, routeStart, routeEnd, routeType;
    private ArrayList<FirebaseStation> stationsRoutePassesThrough;

    public FirebaseRoute(String routeName, String routeStart, String routeEnd, String routeType) {
        this.routeName = routeName;
        this.routeStart = routeStart;
        this.routeEnd = routeEnd;
        this.routeType = routeType;
        this.stationsRoutePassesThrough = new ArrayList<>();
    }

    public void addStationRouteIsPassingThrough(FirebaseStation firebaseStation)
    {
        if(!stationsRoutePassesThrough.contains(firebaseStation))
        {
            stationsRoutePassesThrough.add(firebaseStation);
        }
    }

    public ArrayList<FirebaseStation> getStationsRouteIsPassingThrough()
    {
        return this.stationsRoutePassesThrough;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteStart() {
        return routeStart;
    }

    public void setRouteStart(String routeStart) {
        this.routeStart = routeStart;
    }

    public String getRouteEnd() {
        return routeEnd;
    }

    public void setRouteEnd(String routeEnd) {
        this.routeEnd = routeEnd;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    @Override
    public String toString() {
        return "FirebaseRoute{" +
                "routeName='" + routeName + '\'' +
                ", routeStart='" + routeStart + '\'' +
                ", routeEnd='" + routeEnd + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FirebaseRoute that = (FirebaseRoute) o;
        return Objects.equals(routeName, that.routeName) && Objects.equals(routeStart, that.routeStart) && Objects.equals(routeEnd, that.routeEnd) && Objects.equals(routeType, that.routeType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(routeName, routeStart, routeEnd, routeType);
    }
}
