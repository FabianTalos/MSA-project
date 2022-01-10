package com.example.publictransportationapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.publictransportationapp.R;
import com.example.publictransportationapp.activity.ShowTransports;
import com.example.publictransportationapp.activity.TicketActivity;

public class VehiclesFragment extends Fragment {
    Button btnTransport;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehicles, container, false);

        btnTransport = (Button) view.findViewById(R.id.btn_check_transport);

        btnTransport.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShowTransports.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
