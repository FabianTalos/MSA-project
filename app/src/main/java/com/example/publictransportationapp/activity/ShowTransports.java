package com.example.publictransportationapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.publictransportationapp.R;
import com.example.publictransportationapp.adapter.MapAdapter;
import com.example.publictransportationapp.model.Station;
import com.example.publictransportationapp.model.Transport;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

public class ShowTransports extends AppCompatActivity {
    DatabaseReference reference;

    ArrayList<Station> listStations1 = new ArrayList<>();
    ArrayList<Station> listStations2 = new ArrayList<>();
    MapAdapter mapAdapter;
    Context mcontext;
    RecyclerView myRecyclerView;
    HashMap<String, ArrayList<Station>> stationsMap = new HashMap<>();

    public ShowTransports() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_transports);

        mcontext = this;
        Log.e("check", "ShowTransports activity, on create");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("transports");

        /* Inside this part, parse the database  */
        //final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.transportlist_item, list); //the xml made for the transport

        reference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Iterable<DataSnapshot> transportTypes = snapshot.getChildren();
                ArrayList<String> keys = fetchKeys(transportTypes); //get the important key names (bus, tram, etc)

                Map<String, Map<String, ArrayList<Station>>> allTransports = getTransportsFromFB(keys, snapshot);

                Log.e("Var", "All keys: " + allTransports.keySet());
                Log.e("Var", "All values: " + allTransports.values());

                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        /* Inside this part, parse the database  */

        for(int i = 0; i < 4; i++)
        {
            listStations1.add(new Station("Station_1 " + i, "Time_1 " + i));
            listStations2.add(new Station("Station_2 " + i, "Time_2 " + i));
        }
        stationsMap.put("First direction", listStations1);
        stationsMap.put("Second direction", listStations2);

        setContentView(R.layout.recycler_view);
        myRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        if(myRecyclerView == null)
        {
            Log.e("check", "Instance is null");
        }
        else {
            myRecyclerView.setLayoutManager(linearLayoutManager);
            mapAdapter = new MapAdapter(mcontext, stationsMap);
            myRecyclerView.setAdapter(mapAdapter);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Map<String, Map<String, ArrayList<Station>>> getTransportsFromFB(ArrayList<String> transportTypeKeys, DataSnapshot snapshot) {
        ArrayList<Transport> transportList = new ArrayList<>();

        String transportTypeFirebase, routeNameFirebase;
        ArrayList<String> directionsFirebase;
        ArrayList<Station> stationsFirebase = new ArrayList<>();
        Map<String, Map<String, ArrayList<Station>>> allTransports = new HashMap<>();
        Map<String, ArrayList <Station>> stationsMapFirebase = new HashMap<>();

        for(String transportTypeKey : transportTypeKeys)
        {
            //Log.e("Var", "For key: " + transportTypeKey); //transport type
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
                    stationsMapFirebase.putIfAbsent(direction, stationsFirebase);
                }
            }
            allTransports.putIfAbsent(transportTypeKey, stationsMapFirebase);
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

    private void printHour()
    {
        Formatter formate = new Formatter();
        Calendar gfg_calender = Calendar.getInstance();
        formate = new Formatter();
        formate.format("%tl:%tM", gfg_calender,
                gfg_calender);

        Log.e("Var", "Time: " + formate);
    }
}