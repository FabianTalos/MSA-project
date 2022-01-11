package com.example.publictransportationapp.Tools;

import static com.example.publictransportationapp.Tools.UsefulMethods.fetchKeys;
import static com.example.publictransportationapp.Tools.UsefulMethods.tag;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.publictransportationapp.model.FirebaseRoute;
import com.example.publictransportationapp.model.FirebaseStation;
import com.example.publictransportationapp.model.Station;
import com.example.publictransportationapp.model.Transport;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

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

    public static Transport fetchDataForTransportGivenName(String routeName, CustomCallback callback)
    {
        DatabaseReference transportsReference = FirebaseManager.getTransportsReference();   //connect to database and get the times for this specific transport (given its name)
        transportsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> iterableTransportTypes = snapshot.getChildren();
                ArrayList<String> transportTypes = fetchKeys(iterableTransportTypes);
                Transport selectedTransport = fetchDataForTransportFromFirebase(routeName, transportTypes, snapshot);
                callback.onCallback(selectedTransport);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return new Transport();
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

    private static Transport fetchDataForTransportFromFirebase(String routeName, ArrayList<String> transportTypes, DataSnapshot snapshot) {
        Transport transport = new Transport();
        transport.setRouteName(routeName);      //set name of transport (ex: 33b, e8 etc)
        String routeType = "";
        int found = 0;
        for(String transportType : transportTypes)
        {
            Iterable<DataSnapshot> values = snapshot.child(transportType).child("route").getChildren();
            ArrayList<String> routeNames = fetchKeys(values); //For each transport type, get the transport routes (ex: 33b, e8 etc)

            if(routeNames.contains(routeName)) //if our transport was inside this category of transports, retrieve that category
            {
                routeType = transportType;
                found = 1;
                break;
            }
        }
        if(found == 0) //some kind of problem occurred while parsing the database, safe to quit
        {
            return null;
        }
        transport.setTransportType(routeType);  //set type of transport

        ArrayList<String> directions = fetchDirectionsForGivenRoute(routeName, routeType, snapshot);
        HashMap<String, ArrayList <Station>> stationsMap = new HashMap<>();
        ArrayList<Station> stationsForGivenDirection;
        for(String direction : directions) {
            transport.addDirection(direction);  //update list of directions inside the transport variable we return
            stationsForGivenDirection = fetchStationsForGivenDirection(routeName, routeType, direction, snapshot);
            stationsMap.put(direction, stationsForGivenDirection);
        }

        transport.setStations(stationsMap); //finally, update the stations for this transport
        return transport;
    }

    private static ArrayList<String> fetchDirectionsForGivenRoute(String routeName, String routeType, DataSnapshot snapshot) {
        ArrayList<String> directions = new ArrayList<>();
        DataSnapshot dataSnapshot = snapshot.child(routeType).child("route").child(routeName).child("data");
        for(DataSnapshot child : dataSnapshot.getChildren()) //for this route, we will get to following directions
        {
            directions.add(child.getKey());
        }
        return directions;
    }

    private static ArrayList<Station> fetchStationsForGivenDirection(String routeName, String routeType, String direction, DataSnapshot snapshot) {
        ArrayList<Station> stations = new ArrayList<>();
        DataSnapshot dataSnapshot = snapshot.child(routeType).child("route").child(routeName).child("data").child(direction);
        for(DataSnapshot child : dataSnapshot.getChildren()) //for each direction, we have a list of stations:times
        {
            DataSnapshot subChild = dataSnapshot.child(child.getKey()); //each subchild of a direction is split into a station and a time
            Station station = new Station();
            for(DataSnapshot value : subChild.getChildren())
            {
                if(value.getKey().equals("0"))      //if the child has key 0, it's a station,
                {
                    station.setStationName(value.getValue().toString());
                }
                else                                //else, it's a time
                {
                    station.setArrivalTime(value.getValue().toString());
                }
            }
            stations.add(station);  //Add the new created station to the list of stations
        }

        return stations;
    }

}
