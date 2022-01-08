package com.example.publictransportationapp.Tools;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UsefulMethods {
    public static final String tag = "MainTag";
    public static ArrayList<String> fetchKeys(Iterable<DataSnapshot> values) {
        ArrayList<String> keys = new ArrayList<>();
        for(DataSnapshot value : values)
        {
            keys.add(value.getKey());
        }
        return keys;
    }

    public static String getHashMapKeyFromIndex(HashMap hashMap, int index) {

        String key = null;
        HashMap<String, Object> hs = hashMap;
        int pos = 0;
        for (Map.Entry<String, Object> entry : hs.entrySet()) {
            if (index == pos) {
                key = entry.getKey();
            }
            pos++;
        }
        return key;
    }
}
