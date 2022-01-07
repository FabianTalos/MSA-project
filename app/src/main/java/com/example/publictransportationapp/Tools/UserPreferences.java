package com.example.publictransportationapp.Tools;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.publictransportationapp.model.FavouriteRoute;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public class UserPreferences { //thank you https://www.geeksforgeeks.org/how-to-save-arraylist-to-sharedpreferences-in-android/
    private static UserPreferences instance;

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

    public ArrayList<String> getListOfFavouriteRoutesNames(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);

        ArrayList<String> favouriteRoutesList = new ArrayList<>(); // deserialize the JSON stored data into data compatible with ArrayList<FavouriteRoute>
        favouriteRoutesList.addAll(sharedPreferences.getAll().keySet());
        return favouriteRoutesList;
    }

    public void addRouteToFavourites(FavouriteRoute favouriteRoute, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("favouriteRoutes", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(!sharedPreferences.contains(favouriteRoute.getFavouriteRouteName())) {
            editor.putString(favouriteRoute.getFavouriteRouteName(), favouriteRoute.getFavouriteRouteName()); //save favourites
            editor.apply(); //apply the changes, asynchronously
        /*
        Log.e("MainTag", "favouriteRoutes (after add): " + sharedPreferences);
        for(Map.Entry<String, ?> item : sharedPreferences.getAll().entrySet())
        {
            Log.e("MainTag", "Item key: " + item.getKey());
        }
        */
            Toast.makeText(context, "Saved " + favouriteRoute.getFavouriteRouteName() + " to favourites.", Toast.LENGTH_SHORT).show();
        }
    }

    public void removeRouteFromFavourites(FavouriteRoute favouriteRoute, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("favouriteRoutes", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(sharedPreferences.contains(favouriteRoute.getFavouriteRouteName())) {
            editor.remove(favouriteRoute.getFavouriteRouteName());
            editor.apply();
            Log.e("MainTag", "favouriteRoutes (after delete): " + sharedPreferences);
            for (Map.Entry<String, ?> item : sharedPreferences.getAll().entrySet()) {
                Log.e("MainTag", "Item key: " + item.getKey());
            }
            Toast.makeText(context, "Removed " + favouriteRoute.getFavouriteRouteName() + " from favourites. (theoretically)", Toast.LENGTH_SHORT).show();
        }
    }
}
