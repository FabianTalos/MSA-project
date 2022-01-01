package com.example.publictransportationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.publictransportationapp.activity.TransportList;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
//TO DO: GridLayoutManager for transports
public class MainActivity extends AppCompatActivity {
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    }

    public void openTransportList()
    {
        Intent intent = new Intent(this, TransportList.class);
        startActivity(intent);
    }

}
