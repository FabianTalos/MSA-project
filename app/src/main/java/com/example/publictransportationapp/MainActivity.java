package com.example.publictransportationapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.publictransportationapp.Tools.UserPreferences;
import com.example.publictransportationapp.activity.ShowTransports;
import com.example.publictransportationapp.fragments.FavoritesFragment;
import com.example.publictransportationapp.fragments.HomeFragment;
import com.example.publictransportationapp.fragments.InfoFragment;
import com.example.publictransportationapp.fragments.SearchFragment;
import com.example.publictransportationapp.fragments.VehiclesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        bottomNav.setSelectedItemId(R.id.nav_vehicles);
        //UserPreferences.getInstance();
        //UserPreferences.getInstance().loadData(new ArrayList<>(), this);
        //SharedPreferences sharedPreferences = this.getSharedPreferences("favouriteRoutes", MODE_PRIVATE); //get an instance of the shared preferences
        // from this instance, grab the favourite routes
        //String json = sharedPreferences.getString("favouriteRoutes", null); //get the saved routes (null if value is empty)
//        Log.e("MainTag", "favouriteRoutes: " + sharedPreferences);
//        for(Map.Entry<String, ?> item : sharedPreferences.getAll().entrySet())
//        {
//            Log.e("MainTag", "Item key: " + item.getKey());
//            Log.e("MainTag", "Item value: " + item.getValue());
//        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_search:
                    selectedFragment = new SearchFragment();
                    break;
                case R.id.nav_vehicles:
                    selectedFragment = new VehiclesFragment();

                    Intent intent = new Intent(MainActivity.this, ShowTransports.class);
                    startActivity(intent);
                    break;
                case R.id.nav_favorites:
                    selectedFragment = new FavoritesFragment();
                    break;
                case R.id.nav_info:
                    selectedFragment = new InfoFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

            return true;
        }
    };

}
