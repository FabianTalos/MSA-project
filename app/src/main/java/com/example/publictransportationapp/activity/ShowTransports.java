package com.example.publictransportationapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.publictransportationapp.R;
import com.example.publictransportationapp.adapter.MapAdapter;
import com.example.publictransportationapp.model.Station;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

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
        final ArrayList<String> list = new ArrayList<>();
        //final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.transportlist_item, list); //the xml made for the transport
        //transports -> bus -> route -> route id + param
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                String key = snapshot.getKey();
                Log.e("Var", "How does it look?: " + snapshot.getKey());

                Iterable<DataSnapshot> transportTypes = snapshot.getChildren();
                for (DataSnapshot transportType : transportTypes){
                    Log.e("Var", "transportType: " + transportType);
                    String something = transportType.getValue().toString();
                    list.add(something);
                    Log.e("Var", "Element: " + something);
                }
                //adapter.notifyDataSetChanged();
                for(String string : list)
                {
                    Log.e("Var", "List component: " + string);
                }
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
}