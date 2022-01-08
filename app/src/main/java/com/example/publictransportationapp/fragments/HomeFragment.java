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
import com.example.publictransportationapp.activity.TicketActivity;

public class HomeFragment extends Fragment {

    Button btnCheck, btnBuy;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        btnCheck = (Button) view.findViewById(R.id.btn_check_prices);
        btnBuy = (Button) view.findViewById(R.id.btn_buy_ticket);

        btnCheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                gotoUrl("http://www.ratt.ro/preturi.html");
            }
        });

        //doesn't open activity as intended; needs to be fixed
        btnBuy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TicketActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}
