package com.example.publictransportationapp.Tools;

import static com.example.publictransportationapp.Tools.UsefulMethods.fetchKeys;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseManager {

    private static FirebaseDatabase database;

    public static FirebaseDatabase getInstance()
    {
        if(database == null)
        {
            database = FirebaseDatabase.getInstance();
        }
        return database;
    }

    public static DatabaseReference getTransportsReference()
    {
        if(database == null)
        {
            getInstance();
        }

        return database.getReference().child("transports");
    }

    public static void getTransportTypeForRouteName(String favouriteRouteName, CustomCallback callback) {
        DatabaseReference reference = getTransportsReference();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Iterable<DataSnapshot> transportTypes = snapshot.getChildren();
                ArrayList<String> keys = fetchKeys(transportTypes);
                String transportType = getTransportType(favouriteRouteName, keys, snapshot);
                //Log.e("MainTag", favouriteRouteName + " route is a " + transportType);
                callback.onCallback(transportType); //this will return the transport type value once it's fetched  from the db
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static String getTransportType(String routeName, ArrayList<String> transportTypeKeys, DataSnapshot snapshot)
    {
        for(String transportTypeKey : transportTypeKeys)
        {
            Iterable<DataSnapshot> values = snapshot.child(transportTypeKey).child("route").getChildren();
            ArrayList<String> routeNames = fetchKeys(values); //For each transport type, get the transport routes (ex: 33b, e8 etc)
            if(routeNames.contains(routeName.toLowerCase()))
                return transportTypeKey;
        }
        return null;
    }
}
