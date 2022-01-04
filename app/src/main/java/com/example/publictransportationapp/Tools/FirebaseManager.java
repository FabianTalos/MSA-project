package com.example.publictransportationapp.Tools;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseManager {

    private static FirebaseDatabase database;

    public static FirebaseDatabase getInstance()
    {
        if(database == null)
        {
            database = FirebaseDatabase.getInstance();
        }
        return database;
    }

    public static DatabaseReference getTransportsReference()
    {
        if(database == null)
        {
            getInstance();
        }

        return database.getReference().child("transports");
    }
}
