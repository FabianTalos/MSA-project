package com.example.publictransportationapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.publictransportationapp.activity.ShowTransports;
import com.example.publictransportationapp.fragments.VehiclesFragment;
import com.example.publictransportationapp.fragments.HomeFragment;
import com.example.publictransportationapp.fragments.SearchFragment;
import com.example.publictransportationapp.fragments.FavoritesFragment;
import com.example.publictransportationapp.fragments.InfoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//TO DO: GridLayoutManager for transports
public class MainActivity extends AppCompatActivity {
    DatabaseReference reference;

    BottomNavigationView bottomNav;
    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        bottomNav.setSelectedItemId(R.id.nav_vehicles);
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
