package com.example.publictransportationapp.activity;

import static com.example.publictransportationapp.Tools.UsefulMethods.fetchKeys;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.publictransportationapp.R;
import com.example.publictransportationapp.adapter.TransportsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowTransports extends AppCompatActivity {
    TransportsAdapter transportsAdapter;
    Context mcontext;
    RecyclerView myRecyclerView;

    public ShowTransports() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_transports);
        mcontext = this;
        //Toast.makeText(ShowTransports.this, "Inside show transports", Toast.LENGTH_LONG).show();

        //Fetch Real data
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("transports");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                Iterable<DataSnapshot> transportTypes = snapshot.getChildren();
                ArrayList<String> keys = fetchKeys(transportTypes); //get the important key names (bus, tram, etc)

                HashMap<String, ArrayList<String>> realRoutes;
                realRoutes = getAllRoutes(keys, snapshot); //Transport_type_1 -> Route_1, Route_2 etc

                //Open view and display data
                myRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewAllTransports);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false);
                if(myRecyclerView == null)
                {
                    Log.e("MainTag", "Not good");
                }
                else {
                    //Log.e("MainTag", "Good");
                    //GridLayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),3);
                    //myRecyclerView.setLayoutManager(mLayoutManager);

                    myRecyclerView.setLayoutManager(linearLayoutManager);
                    transportsAdapter = new TransportsAdapter(realRoutes, mcontext);
                    myRecyclerView.setAdapter(transportsAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private HashMap<String, ArrayList<String>> getAllRoutes(ArrayList<String> transportTypeKeys, DataSnapshot snapshot) {
        HashMap<String, ArrayList<String>> routes = new HashMap<>();
        String transportTypeFirebase;

        for(String transportTypeKey : transportTypeKeys)
        {
            transportTypeFirebase = transportTypeKey;

            Iterable<DataSnapshot> values = snapshot.child(transportTypeKey).child("route").getChildren();
            ArrayList<String> routeNames = fetchKeys(values); //For each transport type, get the transport routes (ex: 33b, e8 etc)

            routes.put(transportTypeFirebase, routeNames); //Update the map with the transports
        }

        return routes;
    }

}