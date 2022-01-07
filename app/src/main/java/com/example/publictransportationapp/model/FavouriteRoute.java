package com.example.publictransportationapp.model;

public class FavouriteRoute implements ItemInterface{
    private String favouriteRouteName;
    private String favouriteRouteType;

    public FavouriteRoute(String favouriteRouteName)
    {
        this.favouriteRouteName = favouriteRouteName;
    }

    public FavouriteRoute(String favouriteRouteName, String favouriteRouteType)
    {
        this.favouriteRouteName = favouriteRouteName;
        this.favouriteRouteType = favouriteRouteType;
    }
    public FavouriteRoute() {

    }

    public String getFavouriteRouteName()
    {
        return favouriteRouteName;
    }

    public void setFavouriteRouteType(String favouriteRouteType) {
        this.favouriteRouteType = favouriteRouteType;
    }

    public String getFavouriteRouteType() {
        return favouriteRouteType;
    }

    @Override
    public boolean isSection() {
        return false;
    }

    @Override
    public String toString() {
        return favouriteRouteName + " - " + favouriteRouteType;
    }

    public void setFavouriteRouteName(String routeName) {
        this.favouriteRouteName = routeName;
    }
}
