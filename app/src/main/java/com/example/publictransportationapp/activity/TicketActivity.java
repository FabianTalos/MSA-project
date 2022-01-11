package com.example.publictransportationapp.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.publictransportationapp.R;

public class TicketActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    String etPhone, item;
    Button btSend;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        etPhone = "7442";
        btSend = findViewById(R.id.btn_send);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.transport_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);


        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check conditions
                if (ContextCompat.checkSelfPermission(TicketActivity.this, Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED){
                    //when granted
                    sendMessage();
                } else {
                    //when permission denied
                    ActivityCompat.requestPermissions(TicketActivity.this, new String[]{Manifest.permission.SEND_SMS}, 100);
                }
            }

            public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
                if(requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //when granted
                    sendMessage();
                }else{
                    //when denied
                    Toast.makeText(getApplicationContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendMessage() {
        //Get value from edit text
        String sMessage = "B" + item;
        //check condition
        if(!sMessage.equals(""))
        {
            //when textbox contains a value
            SmsManager smsManager = SmsManager.getDefault();
            //send the message
            smsManager.sendTextMessage(etPhone, null, sMessage, null, null);
            Toast.makeText(getApplicationContext(), "Message sent successfully. Enjoy your ride!", Toast.LENGTH_SHORT).show();
        } else {
            //when no text value
            Toast.makeText(getApplicationContext(), "Enter value first", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "Selected:" + item, Toast.LENGTH_SHORT).show();

        if(parent.getItemAtPosition(position).equals("Bus"))
        {
            Spinner bus_spinner = (Spinner) findViewById(R.id.transport_spinner);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.bus_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            bus_spinner.setAdapter(adapter);

            bus_spinner.setOnItemSelectedListener(this);
        }

        if(parent.getItemAtPosition(position).equals("Tramway"))
        {
            Spinner tram_spinner = (Spinner) findViewById(R.id.transport_spinner);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.tram_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            tram_spinner.setAdapter(adapter);

            tram_spinner.setOnItemSelectedListener(this);
        }

        if(parent.getItemAtPosition(position).equals("Trolley"))
        {
            Spinner trolley_spinner = (Spinner) findViewById(R.id.transport_spinner);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.trolley_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            trolley_spinner.setAdapter(adapter);

            trolley_spinner.setOnItemSelectedListener(this);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

