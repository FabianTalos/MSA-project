package com.example.publictransportationapp.activity;

import static com.example.publictransportationapp.Tools.UsefulMethods.fetchKeys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.publictransportationapp.R;
import com.example.publictransportationapp.Tools.FirebaseManager;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_route);

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
                Log.e("MainTag", "selectedTransport " + selectedTransport);
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
        Map<String, ArrayList <Station>> stationsMap = new HashMap<>();
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

    }

    private ArrayList<Station> fetchStationsForGivenDirection(String routeName, String routeType, String direction, DataSnapshot snapshot) {
    }
}