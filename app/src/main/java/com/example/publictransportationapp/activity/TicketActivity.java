package com.example.publictransportationapp.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.publictransportationapp.R;

public class TicketActivity extends AppCompatActivity {
    EditText etPhone, etMessage;
    Button btSend;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        etPhone = findViewById(R.id.et_phone);
        etMessage = findViewById(R.id.et_message);
        btSend = findViewById(R.id.btn_send);

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
        //Get values from edit text
        String sPhone = etPhone.getText().toString().trim();
        String sMessage = etMessage.getText().toString().trim();
        //check condition
        if(!sPhone.equals("") && !sMessage.equals(""))
        {
            //when textboxes contain a value
            SmsManager smsManager = SmsManager.getDefault();
            //send the message
            smsManager.sendTextMessage(sPhone, null, sMessage, null, null);
            Toast.makeText(getApplicationContext(), "Message sent successfully. Enjoy your ride!", Toast.LENGTH_SHORT).show();
        } else {
            //when no text value
            Toast.makeText(getApplicationContext(), "Enter value first", Toast.LENGTH_SHORT).show();
        }
    }
}

