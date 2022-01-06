package com.example.publictransportationapp.Tools;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.publictransportationapp.model.FavouriteRoute;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class UserPreferences { //thank you https://www.geeksforgeeks.org/how-to-save-arraylist-to-sharedpreferences-in-android/

    public void saveData(ArrayList<FavouriteRoute> favouriteRoutesList, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(favouriteRoutesList); // get data from gson and storing it in a string.

        editor.putString("favouriteRoutes", json); //save favourites
        editor.apply(); //apply the changes, asynchronously

        Toast.makeText(context, "Saved Array List to Shared preferences. ", Toast.LENGTH_SHORT).show();
    }

    public void loadData(ArrayList<FavouriteRoute> favouriteRoutesList, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);

        String json = sharedPreferences.getString("favouriteRoutes", null); //get the saved routes (null if value is empty)
        Type type = new TypeToken<ArrayList<FavouriteRoute>>() {}.getType(); // get the type of the array list

        Gson gson = new Gson();
        favouriteRoutesList = gson.fromJson(json, type); // deserialize the JSON stored data into data compatible with ArrayList<FavouriteRoute>

        if (favouriteRoutesList == null) { //return an empty list if deserialized list is null
            favouriteRoutesList = new ArrayList<>();
        }
    }
}
