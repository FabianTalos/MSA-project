package com.example.publictransportationapp.Tools;

import com.example.publictransportationapp.model.Transport;

public interface CustomCallback {
    void onCallback(String value);

    void onCallback(Transport selectedTransport);
}
