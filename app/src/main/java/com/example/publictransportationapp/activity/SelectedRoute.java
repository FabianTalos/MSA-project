package com.example.publictransportationapp.activity;

import static com.example.publictransportationapp.Tools.UsefulMethods.fetchKeys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.publictransportationapp.R;
import com.example.publictransportationapp.Tools.FirebaseManager;
import com.example.publictransportationapp.adapter.MapAdapter;
import com.example.publictransportationapp.adapter.RouteAdapter;
import com.example.publictransportationapp.adapter.RouteAdapter_2;
import com.example.publictransportationapp.model.Station;
import com.example.publictransportationapp.model.Transport;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectedRoute extends AppCompatActivity {
    Context mcontext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_route);
        mcontext = this;
        TextView routeName = findViewById(R.id.routeName);

        String routePlaceholder = "Route not set";
        Bundle extras = getIntent().getExtras(); //get the info passed to the intent from inside ShowTransports -> onClickOpenSelectedRoute
        if(extras != null)
        {
            routePlaceholder = extras.getString("routeName"); //get the value we need by sending corresponding key
        }
        routeName.setText(routePlaceholder);  //setText for the view of the SelectedRoute (activity_selected_route)

        DatabaseReference transportsReference = FirebaseManager.getTransportsReference();   //connect to database and get the times for this specific transport (given its name)
        String finalRouteName = routePlaceholder;
        transportsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> iterableTransportTypes = snapshot.getChildren();
                ArrayList<String> transportTypes = fetchKeys(iterableTransportTypes);
                Transport selectedTransport = fetchDataForTransportGivenName(finalRouteName, transportTypes, snapshot);
                //might need another thread to handle this fetching of data

                //Log.e("MainTag", "selectedTransport " + selectedTransport);
                RouteAdapter routeAdapter;
                RecyclerView myRecyclerView;

                myRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewSelectedRoute);

                if(myRecyclerView == null)
                {
                    Log.e("MainTag", "Not good");
                }
                else {
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false);
                    myRecyclerView.setLayoutManager(linearLayoutManager);
                    Log.e("MainTag", "Good");
                    RouteAdapter_2 routeAdapter_2;

                    String routeName = selectedTransport.getRouteName();
                    String direction = selectedTransport.getDirections().get(0);
                    ArrayList<Station> stations = selectedTransport.getStations(direction);
                    myRecyclerView.setHasFixedSize(true);
                    routeAdapter_2 = new RouteAdapter_2(mcontext, routeName, direction, stations);
                    //routeAdapter = new RouteAdapter(mcontext, selectedTransport.getRouteName(), selectedTransport.getDirections(), selectedTransport.getStationsMap());
                    myRecyclerView.setAdapter(routeAdapter_2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private Transport fetchDataForTransportGivenName(String routeName, ArrayList<String> transportTypes, DataSnapshot snapshot) {
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
            Log.e("MainTag", "Problem fetching transport type for given transport name");
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

    private ArrayList<String> fetchDirectionsForGivenRoute(String routeName, String routeType, DataSnapshot snapshot) {
        ArrayList<String> directions = new ArrayList<>();
        DataSnapshot dataSnapshot = snapshot.child(routeType).child("route").child(routeName).child("data");
        for(DataSnapshot child : dataSnapshot.getChildren()) //for this route, we will get to following directions
        {
            //Log.e("MainTag", "Child: " + child.getKey());
            directions.add(child.getKey());
        }
        return directions;
    }

    private ArrayList<Station> fetchStationsForGivenDirection(String routeName, String routeType, String direction, DataSnapshot snapshot) {
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