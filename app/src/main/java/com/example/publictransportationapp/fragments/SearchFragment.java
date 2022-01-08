package com.example.publictransportationapp.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;

import com.example.publictransportationapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class SearchFragment extends Fragment {
    GoogleMap map;
    SupportMapFragment supportMapFragment;
    SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        searchView = view.findViewById(R.id.sv_location);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                if (location != null || (!location.equals("")))
                {
                    Geocoder geocoder = new Geocoder(view.getContext());
                    try {
                        List<Address> geoResults = geocoder.getFromLocationName(location, 1);
                        int callLimit = 3;
                        while (geoResults.size() == 0 && callLimit != 0)  //wait on the geocoder to fetch results for given location
                        {
                            geoResults = geocoder.getFromLocationName(location, 1);
                            callLimit--;
                        }
                        //geocoder fetched at least one result, get it through index of the first element of the list
                        //this address contains a lot more information than latitude and longitude
                        Address addressFromGeocoder = geoResults.get(0);
                        LatLng position = new LatLng(addressFromGeocoder.getLatitude(), addressFromGeocoder.getLongitude());
                        if(map != null){
                            map.clear();
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 17));
                            map.addMarker(new MarkerOptions().position(position).title(location));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) { //returns false while typing
                return false;
            }
        });

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) { //when map loads
                map = googleMap;    //store the reference in our map variable, for future use
                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);  //set marker properties
                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                        map.clear();
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10)); //animate to zoom marker
                        map.addMarker(markerOptions); //add marker on map
                    }
                });
            }
        });

        return view;
    }

}

        /*
        //async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //when map loads
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        //when clicked on map
                        MarkerOptions markerOptions = new MarkerOptions();
                        //set marker position
                        markerOptions.position(latLng);
                        //set marker title
                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                        //remove all markers
                        googleMap.clear();
                        //animate to zoom marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                        //add marker on map
                        googleMap.addMarker(markerOptions);
                    }
                });
            }
        });
        return view;
    }
}*/
