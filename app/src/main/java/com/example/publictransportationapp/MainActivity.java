package com.example.publictransportationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Console;

public class MainActivity extends AppCompatActivity {

    EditText vehicleType, vehicleNumber, vehicleStart, vehicleStop;
    Button buttonAddTransport, buttonChange;
    DatabaseReference fbdb;

    float x1, x2, y1, y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vehicleType = findViewById(R.id.vehicleType);
        vehicleNumber = findViewById(R.id.vehicleNumber);
        vehicleStart = findViewById(R.id.stationStart);
        vehicleStop = findViewById(R.id.stationStop);
        buttonAddTransport = (Button) findViewById(R.id.buttonAddTransport);

        buttonChange = (Button) findViewById(R.id.buttonChange);

        fbdb = FirebaseDatabase.getInstance().getReference().child("Transport");

        buttonChange.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Activity2.class);
                startActivity(intent);
            }
        });

        buttonAddTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = vehicleType.getText().toString().trim();
                String number = (vehicleNumber.getText().toString().trim());
                String start = (vehicleStart.getText().toString().trim());
                String stop = (vehicleStop.getText().toString().trim());

                Transport transport = new Transport(type, number, start, stop);

                fbdb.push().setValue(transport);

                Toast.makeText(MainActivity.this, "Data added to database!", Toast.LENGTH_LONG).show();
            }
        });
    }
}