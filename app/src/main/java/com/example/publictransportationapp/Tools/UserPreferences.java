package com.example.publictransportationapp.Tools;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.publictransportationapp.model.FavouriteRoute;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

public class UserPreferences { //thank you https://www.geeksforgeeks.org/how-to-save-arraylist-to-sharedpreferences-in-android/
    private static UserPreferences instance;
    public final String FAVORITE_ROUTES_PREF = "favouriteRoutes";

    public static UserPreferences getInstance() //this is a singleton class
    {
        if(instance == null)
            instance = new UserPreferences();
        return instance;
    }

    private UserPreferences() {}

    public void addListOfRoutesToFavourites(ArrayList<FavouriteRoute> favouriteRoutesList, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(favouriteRoutesList); // get data from gson and store it in a string

        editor.putString("favouriteRoutes", json); //save favourites
        editor.apply(); //apply the changes, asynchronously

        Toast.makeText(context, "Saved Array List to Shared preferences. ", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<FavouriteRoute> getListOfFavouriteRoutes(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FAVORITE_ROUTES_PREF, MODE_PRIVATE);
        ArrayList<FavouriteRoute> favouriteRoutesList = new ArrayList<>(); // deserialize the JSON stored data into data compatible with ArrayList<FavouriteRoute>

        for(Map.Entry<String, ?> value : sharedPreferences.getAll().entrySet())
        {
            FavouriteRoute tempRoute = new FavouriteRoute(value.getKey(), (String) value.getValue());
            favouriteRoutesList.add(tempRoute);
        }
        return favouriteRoutesList;
    }

    public void addRouteToFavourites(FavouriteRoute favouriteRoute, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FAVORITE_ROUTES_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(!sharedPreferences.contains(favouriteRoute.getFavouriteRouteName())) {
            FirebaseManager.getTransportTypeForRouteName(favouriteRoute.getFavouriteRouteName(), new CustomCallback() {
                @Override
                public void onCallback(String value) {
                    editor.putString(favouriteRoute.getFavouriteRouteName(), value); //save favourites
                    editor.apply(); //apply the changes, asynchronously
                }
            });
        }
    }

    public void removeRouteFromFavourites(FavouriteRoute favouriteRoute, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FAVORITE_ROUTES_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(sharedPreferences.contains(favouriteRoute.getFavouriteRouteName())) {
            editor.remove(favouriteRoute.getFavouriteRouteName());
            editor.apply();
        }
    }

    public boolean isRouteInFavourites(String routeName, Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FAVORITE_ROUTES_PREF, MODE_PRIVATE);

        return sharedPreferences.contains(routeName.toLowerCase());
    }
}
