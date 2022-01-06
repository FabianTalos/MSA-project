package com.example.publictransportationapp.model;

public class GroupFavouriteRouteModel implements ItemInterface {
    private String transportType;

    public GroupFavouriteRouteModel(String transportType) {
        this.transportType = transportType;
    }

    @Override
    public boolean isSection() {
        return true;
    }

    public String getTransportType() {
        return transportType;
    }
}
