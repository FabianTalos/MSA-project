package com.example.publictransportationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText vehicleType, vehicleNumber, vehicleStart, vehicleStop;
    Button buttonAdd;
    DatabaseReference fbdb;
    Transport transport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vehicleType = (EditText) findViewById(R.id.vehicleType);
        vehicleNumber = (EditText)findViewById(R.id.vehicleNumber);
        vehicleStart = (EditText)findViewById(R.id.vehicleStart);
        vehicleStop = (EditText)findViewById(R.id.vehicleStop);
        buttonAdd = (Button)findViewById(R.id.buttonAdd);
        transport = new Transport();
        fbdb = FirebaseDatabase.getInstance().getReference().child("Transport");

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transport.setType(vehicleType.getText().toString().trim());
                transport.setNumber(vehicleNumber.getText().toString().trim());
                transport.setStart(vehicleStart.getText().toString().trim());
                transport.setStop(vehicleStop.getText().toString().trim());
                fbdb.push().setValue(transport);

                Toast.makeText(MainActivity.this, "Data added to database!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
