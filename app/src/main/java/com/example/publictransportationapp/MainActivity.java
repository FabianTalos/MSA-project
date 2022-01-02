package com.example.publictransportationapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.publictransportationapp.activity.TransportList;
import com.example.publictransportationapp.adapter.MapAdapter;
import com.example.publictransportationapp.model.Station;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

//TO DO: GridLayoutManager for transports
public class MainActivity extends AppCompatActivity {
    DatabaseReference reference;

    ArrayList<Station> listStations1 = new ArrayList<>();
    ArrayList<Station> listStations2 = new ArrayList<>();
    MapAdapter mapAdapter;
    Context mcontext;
    RecyclerView myRecyclerView;
    HashMap<String, ArrayList<Station>> stationsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcontext = this;
        Log.e("check", "Main activity, on create");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("transports");

        Button openTransportListButton = (Button) findViewById(R.id.openTransportListButton);
        openTransportListButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openTransportList();
            }
        });

        //Maybe useful
        //Toast.makeText(MainActivity.this, "Data added to database!", Toast.LENGTH_LONG).show();
        // Get list of transports
        /*
        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.transportlist_item, list); //the xml made for the transport
        RecyclerView recyclerView = findViewById(R.id.transportList);
        recyclerView.setAdapter(adapter);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    list.add(dataSnapshot.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        */

        /* Recycler view stuff*/
        for(int i = 0; i < 4; i++)
        {
            listStations1.add(new Station("Station_1 " + i, "Time_1 " + i));
            listStations2.add(new Station("Station_2 " + i, "Time_2 " + i));
        }
        stationsMap.put("First direction", listStations1);
        stationsMap.put("Second direction", listStations2);
        //setContentView(R.layout.activity_main);
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

    public void openTransportList()
    {
        Intent intent = new Intent(this, TransportList.class);
        startActivity(intent);
    }

}
