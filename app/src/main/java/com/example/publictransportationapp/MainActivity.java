package com.example.publictransportationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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


    private Button openTransportListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vehicleType = findViewById(R.id.vehicleType);
        vehicleNumber = findViewById(R.id.vehicleNumber);
        vehicleStart = findViewById(R.id.vehicleStart);
        vehicleStop = findViewById(R.id.vehicleStop);
        buttonAdd = (Button)findViewById(R.id.buttonAdd);

        fbdb = FirebaseDatabase.getInstance().getReference().child("Transport");

        openTransportListButton = (Button) findViewById(R.id.openTransportListButton);
        openTransportListButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openTransportList();
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
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

    public void openTransportList()
    {
        Intent intent = new Intent(this, transportlist.class);
        startActivity(intent);
    }
}
