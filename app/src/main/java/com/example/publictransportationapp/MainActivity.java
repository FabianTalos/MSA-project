package com.example.publictransportationapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.publictransportationapp.activity.SelectedRoute;
import com.example.publictransportationapp.activity.ShowTransports;
import com.example.publictransportationapp.activity.TransportList;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//TO DO: GridLayoutManager for transports
public class MainActivity extends AppCompatActivity {
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MainTag", "Main activity, on create");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("transports");

        Button openTransportListButton = (Button) findViewById(R.id.openTransportListButton);
        openTransportListButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openTransportList();
            }
        });

        Button showTransportsButton = (Button) findViewById(R.id.selectedRouteButton);
        showTransportsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openShowTransports();
            }
        });

    }

    public void openTransportList()
    {
        Intent intent = new Intent(this, ShowTransports.class);
        startActivity(intent);
    }

    public void openShowTransports()
    {
        Intent intent = new Intent(this, SelectedRoute.class);
        startActivity(intent);
    }

}
