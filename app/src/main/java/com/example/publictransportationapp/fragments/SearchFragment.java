package com.example.publictransportationapp.fragments;

import static com.example.publictransportationapp.Tools.UsefulMethods.tag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextParams;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.MotionEventCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
//import com.example.publictransportationapp.Manifest;
import com.example.publictransportationapp.R;
import com.example.publictransportationapp.Tools.DistanceComputer;
import com.example.publictransportationapp.Tools.FirebaseManager;
import com.example.publictransportationapp.activity.SelectedRoute;
import com.example.publictransportationapp.activity.ShowTransports;
import com.example.publictransportationapp.model.FirebaseRoute;
import com.example.publictransportationapp.model.FirebaseStation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;

public class SearchFragment extends Fragment {
    GoogleMap map;
    SupportMapFragment supportMapFragment;
    SearchView searchView;
    private View view;
    boolean firstChange = true;

    Button idBtnFindCurrentLocation; //get current location
    TextView currentLocationLatLong;
    Context mcontext;

    final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private ActivityResultContracts.RequestMultiplePermissions requestMultiplePermissionsContract;
    private ActivityResultLauncher<String[]> multiplePermissionActivityResultLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        searchView = view.findViewById(R.id.sv_location);
        mcontext = this.getContext();
        //Get current location here
        requestMultiplePermissionsContract = new ActivityResultContracts.RequestMultiplePermissions();
        multiplePermissionActivityResultLauncher = registerForActivityResult(requestMultiplePermissionsContract, isGranted -> {
            Log.e(tag, "Launcher result: " + isGranted.toString());
            if (isGranted.containsValue(false)) {
                Log.e(tag, "At least one of the permissions was not granted, launching again...");
                multiplePermissionActivityResultLauncher.launch(PERMISSIONS);
            }
        });

        //start this in the background and let it fetch the stations from the database
        new Thread( new Runnable() { @Override public void run() {
            FirebaseManager.getAllStationCoordinatesFromFirebase();
        } } ).start();

        ArrayList<FirebaseStation>  stationsInRange = new ArrayList<>();
        getCurrentUserLocation();

        //while(currentLocationLatLong.getText().toString().equals("You could see you current location if you want"));
        currentLocationLatLong.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                //if(firstChange == false) {
                    Log.e(tag, "Current user location: " + currentLocationLatLong.getText().toString());
                    String location = currentLocationLatLong.getText().toString();
                    String[] split_string = location.split(",");
                    Double latitudeUser = 0.0, longitudeUser = 0.0;

                    if (split_string.length == 2) {
                        latitudeUser = Double.valueOf(split_string[0]);
                        longitudeUser = Double.valueOf(split_string[1]);
                    }
                    ArrayList<FirebaseStation> allStations = FirebaseManager.getFirebaseStationsCompleteList();

                    if (latitudeUser > 0) {
                        for (FirebaseStation station : allStations) {
                            Double latitudeStation = station.getLatitude();
                            Double longitudeStation = station.getLongitude();
                            //Double distance = DistanceComputer.distance(latitudeUser, latitudeStation, longitudeUser, longitudeStation);
                            Double distance = DistanceComputer.distance_2(latitudeUser, longitudeUser, longitudeStation, latitudeStation);
                            if (distance <= 1000) {
                                stationsInRange.add(station);
                            } else {
                                //Log.e(tag, "Distance, " + distance + " to station too long: " + station);
                            }
                        }
                    }

                    Log.e(tag, "stationsInRange: " + stationsInRange.size());
                    for(FirebaseStation station : stationsInRange)
                    {
                        setStationAsMarkerOnMap(station);
                        Log.e(tag, "Added marker for station: " + station.getStationName());

                    }
                //}
                //firstChange = false;
            }
        });

        //Search and map function below

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap)//when map loads store the reference in our map variable
            {
                map = googleMap;
                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        setPositionAsMarkerOnMap(latLng);
                    }

                });
                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        if(marker.getTag() != null)
                        {
                            ArrayList<String> names = (ArrayList<String>) marker.getTag();
                            StringBuilder stringBuilder = new StringBuilder("");
                            boolean firstIteration = true;
                            for(String name : names)
                            {
                                if(firstIteration)
                                {
                                    stringBuilder.append(name);
                                    firstIteration = false;
                                }
                                else{
                                    stringBuilder.append(", ");
                                    stringBuilder.append(name);
                                }

                            }
                            //marker.showInfoWindow();
                        }
                        Log.e(tag, "Clicked marker with position, but no routes to show ");
                        //Using position get Value from arraylist
                        //marker.showInfoWindow();

                        return false;
                    }
                });
                map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(@NonNull Marker marker) {
                        if(marker.getTag() != null)
                        {
                            ArrayList<String> names = (ArrayList<String>) marker.getTag();

                            StringBuilder stringBuilder = new StringBuilder();
                            boolean firstIteration = true;
                            for(String name : names)
                            {
                                if(firstIteration)
                                {
                                    stringBuilder.append(name);
                                    firstIteration = false;
                                }
                                else{
                                    stringBuilder.append(", ");
                                    stringBuilder.append(name);
                                }
                            }
                            Log.e(tag, "Displaying info window: " + stringBuilder);

                            Intent intent = new Intent(mcontext, ShowTransports.class);
                            intent.putStringArrayListExtra("names", names);

                            startActivity(intent);

                        }
                    }
                });
                map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public View getInfoContents(Marker marker) {

                        View v = getLayoutInflater().inflate(R.layout.info_window,null);
                        TextView showTitle = (TextView)  v.findViewById(R.id.showTitle);
                        TextView showLat   = (TextView) v.findViewById(R.id.showLat);
                        TextView showLong  = (TextView) v.findViewById(R.id.showLong);
                        GridLayout llContainer = (GridLayout) v.findViewById(R.id.llContainer);

                        LatLng latLng = marker.getPosition();
                        showTitle.setText(marker.getTitle());
                        showLat.setText("Latitude:" + latLng.latitude);
                        showLong.setText("Longitude:" + latLng.longitude);

                        if(marker.getTag() != null)
                        {
                            ArrayList<String> names = (ArrayList<String>) marker.getTag();
                            /*
                            StringBuilder stringBuilder = new StringBuilder();
                            boolean firstIteration = true;
                            for(String name : names)
                            {
                                if(firstIteration)
                                {
                                    stringBuilder.append(name);
                                    firstIteration = false;
                                }
                                else{
                                    stringBuilder.append(", ");
                                    stringBuilder.append(name);
                                }
                            }
                            Log.e(tag, "Displaying info window: " + stringBuilder);
                            //allRoutes.setText(stringBuilder.toString());

                             */
                            llContainer.removeAllViews();
                            LayoutInflater layoutInflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            for(String name : names)
                            {
                                View view = layoutInflater.inflate(R.layout.row_route_child, null);
                                TextView tv_routeName = view.findViewById(R.id.tv_routeName);
                                tv_routeName.setText(name);
                                llContainer.addView(view);
                            }

                        }


                        return v;
                    }
                });
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                boolean markerSet = setLocationAsMarkerOnMapCustomZoom(location, 17);

                return markerSet;
            }

            @Override
            public boolean onQueryTextChange(String newText) { //returns false while typing
                return false;
            }
        });


        return view;
    }

    private boolean getCurrentUserLocation()
    {
        idBtnFindCurrentLocation = view.findViewById(R.id.idBtnFindCurrentLocation);
        currentLocationLatLong = view.findViewById(R.id.currentLocationLatLong);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(getActivity());

        idBtnFindCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hasPermissions()) //check if location permissions are set
                {
                    getLocation(client); //if everything in order, proceed to get location of client
                }
                else //we don't have permission, ask for it
                {
                    multiplePermissionActivityResultLauncher.launch(PERMISSIONS);
                }
            }
        });

        return true;
    }

    @SuppressLint("MissingPermission")
    private void getLocation(FusedLocationProviderClient client) {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if(location != null) //we got a location and we can show the user where it's at
                    {
                        setLocation(location);
                    }
                    else // we didn't get a location, request location
                    {
                        LocationRequest locationRequest = LocationRequest.create();
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();
                                setLocation(location1); //we have the last location, use that
                            }
                        };
                        //request location update
                        client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper()); //request periodic updates
                    }
                }
            });
        }
        else //location service is disabled
        {
            //open location setting
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void setLocation(Location location) {
        String latlong = location.getLatitude() + ", " + location.getLongitude();
        currentLocationLatLong.setText(latlong);
    }

    private boolean hasPermissions()
    {
        if(ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;

    }


    private void setStationAsMarkerOnMap(FirebaseStation station)
    {
        LatLng stationPosition = new LatLng(station.getLongitude(), station.getLatitude());
        Log.e(tag, "Coordinates: " + stationPosition);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(stationPosition);  //set marker properties
        markerOptions.title(station.getStationName());
        ArrayList<String> routeNameThatPassThrough = station.getRouteNamesThatPassThroughStation();
        StringBuilder stringBuilder = new StringBuilder();
        boolean firstIteration = true;
        for(String name : routeNameThatPassThrough)
        {
            if(firstIteration) continue;
            else{
                stringBuilder.append(", ");
                stringBuilder.append(name);
            }
        }
        Log.e(tag, "Routes for this station " + routeNameThatPassThrough.size());
        if(map != null) {
            //map.clear();
            Marker marker = map.addMarker(markerOptions);
            marker.setSnippet(stringBuilder.toString());
            marker.setTag(routeNameThatPassThrough);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(stationPosition, 14)); //animate to zoom marker
            map.addMarker(markerOptions); //add marker on map
        }
    }

    private void setPositionAsMarkerOnMap(LatLng latLng){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);  //set marker properties
        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
        if(map != null) {
            map.clear();
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10)); //animate to zoom marker
            map.addMarker(markerOptions); //add marker on map
        }
    }

    private boolean setLocationAsMarkerOnMapCustomZoom(String location, int zoomFactor)
    {
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
                if(geoResults.size() != 0)
                {
                    Address addressFromGeocoder = geoResults.get(0);
                    LatLng position = new LatLng(addressFromGeocoder.getLatitude(), addressFromGeocoder.getLongitude());
                    if(map != null){
                        map.clear();
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, zoomFactor));
                        map.addMarker(new MarkerOptions().position(position).title(location));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public void onDestroyView()
    {
        super.onDestroyView();
    }

}
