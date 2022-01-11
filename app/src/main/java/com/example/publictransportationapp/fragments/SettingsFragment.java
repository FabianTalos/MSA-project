package com.example.publictransportationapp.fragments;

import static com.example.publictransportationapp.Tools.UsefulMethods.tag;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.publictransportationapp.MainActivity;
import com.example.publictransportationapp.R;
import com.example.publictransportationapp.activity.TicketActivity;

public class SettingsFragment extends Fragment {

    Button btn_changeTheme;
    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null)
            view = inflater.inflate(R.layout.fragment_settings, container, false);
        btn_changeTheme = (Button) view.findViewById(R.id.btn_changeTheme);
        Log.e(tag, "Ajuns in settings fragment");
        Log.e(tag, "Btn null ? " + (btn_changeTheme == null));

        SharedPreferences appSettingsPref = container.getContext().getSharedPreferences("AppSettingsPref", 0);
        SharedPreferences.Editor appSettingsEdit = appSettingsPref.edit();
        boolean isNightModeOn = appSettingsPref.getBoolean("NightMode", false);
        Log.e(tag, "is nightmode on ? " + isNightModeOn);

        if(isNightModeOn)
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            btn_changeTheme.setText("Disable dark mode");
        }
        else
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            btn_changeTheme.setText("Enable dark mode");
        }


        btn_changeTheme.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(isNightModeOn)
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    appSettingsEdit.putBoolean("NightMode", false);
                    appSettingsEdit.apply();

                    btn_changeTheme.setText("Enable dark mode");

                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    appSettingsEdit.putBoolean("NightMode", true);
                    appSettingsEdit.apply();

                    btn_changeTheme.setText("Disable dark mode");
                }

            }
        });

        return view;
    }


}
