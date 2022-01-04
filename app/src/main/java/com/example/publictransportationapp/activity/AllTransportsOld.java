package com.example.publictransportationapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.publictransportationapp.R;
import com.example.publictransportationapp.adapter.MapAdapter;
import com.example.publictransportationapp.model.Station;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllTransportsOld extends AppCompatActivity {
    DatabaseReference reference;

    MapAdapter mapAdapter;
    Context mcontext;
    RecyclerView myRecyclerView;
    HashMap<String, HashMap<String, ArrayList<Station>>> allTransports;

    public AllTransportsOld() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_transports_old);

        mcontext = this;
        Log.e("check", "ShowTransports activity, on create");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("transports");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("Var", "Good");
                Iterable<DataSnapshot> transportTypes = snapshot.getChildren();
                ArrayList<String> keys = fetchKeys(transportTypes); //get the important key names (bus, tram, etc)

                allTransports = getTransportsFromFB(keys, snapshot);

                ArrayList<HashMap<String, ArrayList<Station>>> stationMaps = new ArrayList();
                //Log.e("Var", "Size: " + allTransports.size());
                for (Map.Entry<String, HashMap<String, ArrayList<Station>>> pair : allTransports.entrySet()) {
                    Log.e("Var", "Key allTransports: " + pair.getKey());
                    Log.e("Var", "Values allTransports: " + pair.getValue());
                    HashMap<String, ArrayList<Station>> stationsByDirection = pair.getValue();
                    for (String key : stationsByDirection.keySet())
                    {
                        Log.e("Var", "Key direction: " + key);
                        //Log.e("Var", "Values: " + stationsByDirection.get(key));


                    }
                    stationMaps.add(stationsByDirection);
                }

                setContentView(R.layout.recycler_view);
                myRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false);
                if(myRecyclerView == null)
                {
                    Log.e("Var", "Not good");
                }
                else {
                    myRecyclerView.setLayoutManager(linearLayoutManager);
                    //Log.e("Var", "Good");
                    mapAdapter = new MapAdapter(mcontext, stationMaps.get(0));
                    myRecyclerView.setAdapter(mapAdapter);
                }
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private HashMap<String, HashMap<String, ArrayList<Station>>> getTransportsFromFB(ArrayList<String> transportTypeKeys, DataSnapshot snapshot) {

        HashMap<String, HashMap<String, ArrayList<Station>>> allTransports = new HashMap<>();

        String transportTypeFirebase, routeNameFirebase;
        ArrayList<String> directionsFirebase;
        ArrayList<Station> stationsFirebase = new ArrayList<>();
        HashMap<String, ArrayList <Station>> stationsMapFirebase = new HashMap<>();

        for(String transportTypeKey : transportTypeKeys)
        {
            transportTypeFirebase = transportTypeKey;

            Iterable<DataSnapshot> values = snapshot.child(transportTypeKey).child("route").getChildren();
            ArrayList<String> routeNames = fetchKeys(values); //For each transport type, get the transport routes (ex: 33b, e8 etc)

            for(String route : routeNames)
            {
                routeNameFirebase = route;

                Iterable<DataSnapshot> actualTransports = snapshot.child(transportTypeFirebase).child("route").child(routeNameFirebase).child("data").getChildren();
                directionsFirebase = fetchKeys(actualTransports);

                for(String direction : directionsFirebase)
                {
                    Iterable<DataSnapshot> actualStations = snapshot.
                            child(transportTypeFirebase).child("route").
                            child(routeNameFirebase).child("data").child(direction).getChildren();
                    for(DataSnapshot station : actualStations)
                    {
                        //Log.e("Var", "Key: " + station.getKey()); //0, 1, 2 ...
                        String stationName = station.child("0").getValue().toString();
                        String arrivalTime = station.child("1").getValue().toString();
                        Station tempStation = new Station(stationName, arrivalTime);
                        stationsFirebase.add(tempStation);
                    }
                    if(!stationsMapFirebase.containsKey(direction)) {
                        stationsMapFirebase.put(direction, stationsFirebase);
                    }
                }
            }
            if(!allTransports.containsKey(transportTypeKey)) {
                allTransports.put(transportTypeKey, stationsMapFirebase);
            }
        }

        return allTransports;
    }

    private ArrayList<String> fetchKeys(Iterable<DataSnapshot> values) {
        ArrayList<String> keys = new ArrayList<>();
        for(DataSnapshot value : values)
        {
            keys.add(value.getKey());
        }
        return keys;
    }
}