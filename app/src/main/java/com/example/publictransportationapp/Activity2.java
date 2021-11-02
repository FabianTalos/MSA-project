package com.example.publictransportationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Activity2 extends AppCompatActivity
{
    EditText stationID, stationName, stationType, vehicles;
    Button buttonAddStation;
    DatabaseReference fbdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stationID = findViewById(R.id.stationID);
        stationName = findViewById(R.id.stationName);
        stationType = findViewById(R.id.stationType);
        vehicles = findViewById(R.id.vehicles);
        buttonAddStation = (Button)findViewById(R.id.buttonAddStation);

        fbdb = FirebaseDatabase.getInstance().getReference().child("Station");

        buttonAddStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sID = stationID.getText().toString().trim();
                String sName = stationName.getText().toString().trim();
                String sType = stationType.getText().toString().trim();
                String sVehicles = vehicles.getText().toString().trim();

                Station stations = new Station(sID, sName, sType, sVehicles);

                fbdb.push().setValue(stations);

                Toast.makeText(Activity2.this, "Station added to database!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
