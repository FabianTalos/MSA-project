package com.example.publictransportationapp.Tools;

import static com.example.publictransportationapp.Tools.UsefulMethods.fetchKeys;
import static com.example.publictransportationapp.Tools.UsefulMethods.tag;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.publictransportationapp.model.FirebaseRoute;
import com.example.publictransportationapp.model.FirebaseStation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseManager {

    private static FirebaseDatabase database;
    private static boolean updateExecuted = false;
    private static ArrayList<FirebaseRoute> firebaseRoutesCompleteList;
    private static ArrayList<FirebaseStation> firebaseStationsCompleteList;



    public static FirebaseDatabase getInstance()
    {
        if(database == null)
        {
            firebaseRoutesCompleteList = new ArrayList<>();
            firebaseStationsCompleteList = new ArrayList<>();
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
    public static DatabaseReference getCoordinatesReference()
    {
        if(database == null)
        {
            getInstance();
        }
        return database.getReference().child("coordinates");
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

    public static void getAllStationCoordinatesFromFirebase() {
        if(updateExecuted)
            return;

        DatabaseReference reference = getCoordinatesReference();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Iterable<DataSnapshot> children = snapshot.getChildren();
                ArrayList<String> stationNames = fetchKeys(children);
                //String transportType = getTransportType(favouriteRouteName, keys, snapshot);
                for(String station : stationNames)
                {
                    DataSnapshot stationSnapshot = snapshot.child(station).child("station_data"); //each station can have 1 or 2 children

                    DataSnapshot stationDetails = stationSnapshot.child("station_details");
                    String stationName = stationDetails.child("stationName").getValue().toString();
                    Double latitude = stationDetails.child("stationCoordinates").child("lat").getValue(Double.class);
                    Double longitude = stationDetails.child("stationCoordinates").child("long").getValue(Double.class);
                    FirebaseStation firebaseStation = new FirebaseStation(stationName, latitude, longitude);

                    firebaseStationsCompleteList.add(firebaseStation);

                    //if there also exists the station_traffic node:
                    if(stationSnapshot.getChildrenCount() == 2)
                    {
                        DataSnapshot stationTraffic = stationSnapshot.child("station_traffic");
                        String routeId, routeName, routeStart, routeEnd, routeType;
                        Iterable<DataSnapshot> children_traffic = stationTraffic.getChildren();
                        for(DataSnapshot child : children_traffic)
                        {
                            routeId = child.getKey();
                            Iterable<DataSnapshot> routeNames = stationTraffic.child(routeId).getChildren();
                            for(DataSnapshot route : routeNames)
                            {
                                routeName = route.getKey();
                                routeStart = stationTraffic.child(routeId).child(routeName).child("routeStart").getValue().toString();
                                routeEnd = stationTraffic.child(routeId).child(routeName).child("routeEnd").getValue().toString();
                                routeType = stationTraffic.child(routeId).child(routeName).child("routeType").getValue().toString();
                                FirebaseRoute firebaseRoute = new FirebaseRoute(routeName, routeStart, routeEnd, routeType);
                                firebaseRoute.addStationRouteIsPassingThrough(firebaseStation);
                                firebaseStation.addRouteThroughStation(firebaseRoute);

                                firebaseRoutesCompleteList.add(firebaseRoute);
                            }
                        }
                    }
                    //Log.e(tag, "Station: " + firebaseStation);
                }
                //callback.onCallback(transportType); //this will return the transport type value once it's fetched  from the db
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        updateExecuted = true;
    }

    public static ArrayList<FirebaseRoute> getFirebaseRoutesCompleteList() {
        return firebaseRoutesCompleteList;
    }

    public static ArrayList<FirebaseStation> getFirebaseStationsCompleteList() {
        return firebaseStationsCompleteList;
    }
}
