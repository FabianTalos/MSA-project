package com.example.publictransportationapp.Tools;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class UsefulMethods {

    public static ArrayList<String> fetchKeys(Iterable<DataSnapshot> values) {
        ArrayList<String> keys = new ArrayList<>();
        for(DataSnapshot value : values)
        {
            keys.add(value.getKey());
        }
        return keys;
    }
}
