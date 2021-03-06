package com.example.publictransportationapp.activity;

import static com.example.publictransportationapp.Tools.UsefulMethods.fetchKeys;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.publictransportationapp.R;
import com.example.publictransportationapp.Tools.CustomCallback;
import com.example.publictransportationapp.Tools.FirebaseManager;
import com.example.publictransportationapp.Tools.UserPreferences;
import com.example.publictransportationapp.adapter.RouteMultiAdapter;
import com.example.publictransportationapp.model.FavouriteRoute;
import com.example.publictransportationapp.model.GroupDirectionModel;
import com.example.publictransportationapp.model.ItemInterface;
import com.example.publictransportationapp.model.Station;
import com.example.publictransportationapp.model.Transport;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SelectedRoute extends AppCompatActivity {

    private UserPreferences userPreferences;
    Transport selectedTransport, tempTransport;
    Context mcontext;
    ArrayList<ItemInterface> mStationsAndSectionList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_route);
        mcontext = this;
        TextView routeName = findViewById(R.id.routeName);

        String routePlaceholder = "Route not set";
        Bundle extras = getIntent().getExtras(); //get the info passed to the intent from inside ShowTransports -> onClickOpenSelectedRoute
        routePlaceholder ="33b";
        if(extras != null)
        {
            if(getIntent().getStringArrayListExtra("names") != null)
            {
                routePlaceholder ="33b";
            }
            else{
                routePlaceholder = extras.getString("routeName"); //get the value we need by sending corresponding key
            }
        }
        routeName.setText(routePlaceholder);  //setText for the view of the SelectedRoute (activity_selected_route)
        String finalRouteName = routePlaceholder;

        TextView selectedRouteMessage = findViewById(R.id.selectedRouteMessage); //Showing direction for route ...
        String text = getString(R.string.selectedRoute) + finalRouteName.toUpperCase();
        selectedRouteMessage.setText(text);

        //Deal with saved route in preferences or not here
        userPreferences = UserPreferences.getInstance();

        ImageButton heartBtn = findViewById(R.id.idBtnHeart);
        if(userPreferences.isRouteInFavourites(finalRouteName, mcontext)) //if the route is already in the favorites list
        {
            heartBtn.setImageResource(R.drawable.ic_baseline_favorite_marked);
        }
        else //the route was not yet "hearted", load the "unmarked" resource
        {
            heartBtn.setImageResource(R.drawable.ic_baseline_favorite_unmarked);
        }

        heartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userPreferences.isRouteInFavourites(finalRouteName, mcontext)) //the route is already in the favorites list
                {
                    //clicking this button should "unheart" the route -> remove it from favourites
                    userPreferences.removeRouteFromFavourites(new FavouriteRoute(finalRouteName), mcontext);
                    heartBtn.setImageResource(R.drawable.ic_baseline_favorite_unmarked);
                }
                else //the route was not yet "hearted"
                {
                    userPreferences.addRouteToFavourites(new FavouriteRoute(finalRouteName), mcontext);
                    heartBtn.setImageResource(R.drawable.ic_baseline_favorite_marked);
                }
            }
        });
        //Deal with saved route in preferences


        FirebaseManager.fetchDataForTransportGivenName(finalRouteName, new CustomCallback() {
            @Override
            public void onCallback(String value) {

            }

            @Override
            public void onCallback(Transport value) {
                //do smth with transport here

                selectedTransport = value;
                useAdapter(selectedTransport);
            }
        });






    }

    private Transport fetchDataForTransportGivenName(String routeName, ArrayList<String> transportTypes, DataSnapshot snapshot) {
        Transport transport = new Transport();
        transport.setRouteName(routeName);      //set name of transport (ex: 33b, e8 etc)
        String routeType = "";
        int found = 0;
        for(String transportType : transportTypes)
        {
            Iterable<DataSnapshot> values = snapshot.child(transportType).child("route").getChildren();
            ArrayList<String> routeNames = fetchKeys(values); //For each transport type, get the transport routes (ex: 33b, e8 etc)

            if(routeNames.contains(routeName)) //if our transport was inside this category of transports, retrieve that category
            {
                routeType = transportType;
                found = 1;
                break;
            }
        }
        if(found == 0) //some kind of problem occurred while parsing the database, safe to quit
        {
            return null;
        }
        transport.setTransportType(routeType);  //set type of transport

        ArrayList<String> directions = fetchDirectionsForGivenRoute(routeName, routeType, snapshot);
        HashMap<String, ArrayList <Station>> stationsMap = new HashMap<>();
        ArrayList<Station> stationsForGivenDirection;
        for(String direction : directions) {
            transport.addDirection(direction);  //update list of directions inside the transport variable we return
            stationsForGivenDirection = fetchStationsForGivenDirection(routeName, routeType, direction, snapshot);
            stationsMap.put(direction, stationsForGivenDirection);
        }

        transport.setStations(stationsMap); //finally, update the stations for this transport
        return transport;
    }

    private ArrayList<String> fetchDirectionsForGivenRoute(String routeName, String routeType, DataSnapshot snapshot) {
        ArrayList<String> directions = new ArrayList<>();
        DataSnapshot dataSnapshot = snapshot.child(routeType).child("route").child(routeName).child("data");
        for(DataSnapshot child : dataSnapshot.getChildren()) //for this route, we will get to following directions
        {
            directions.add(child.getKey());
        }
        return directions;
    }

    private ArrayList<Station> fetchStationsForGivenDirection(String routeName, String routeType, String direction, DataSnapshot snapshot) {
        ArrayList<Station> stations = new ArrayList<>();
        DataSnapshot dataSnapshot = snapshot.child(routeType).child("route").child(routeName).child("data").child(direction);
        for(DataSnapshot child : dataSnapshot.getChildren()) //for each direction, we have a list of stations:times
        {
            DataSnapshot subChild = dataSnapshot.child(child.getKey()); //each subchild of a direction is split into a station and a time
            Station station = new Station();
            for(DataSnapshot value : subChild.getChildren())
            {
                if(value.getKey().equals("0"))      //if the child has key 0, it's a station,
                {
                    station.setStationName(value.getValue().toString());
                }
                else                                //else, it's a time
                {
                    station.setArrivalTime(value.getValue().toString());
                }
            }
            stations.add(station);  //Add the new created station to the list of stations
        }

        return stations;
    }

    private void getSectionalList(ArrayList<Station> stationsList, ArrayList<ItemInterface> mStationsAndSectionList, String direction) {

        String lastHeader = "";
        int size = stationsList.size();
        for (int i = 0; i < size; i++) {
            Station station = stationsList.get(i);
            String header = direction;

            if (!TextUtils.equals(lastHeader, header)) {
                lastHeader = header;

                mStationsAndSectionList.add(new GroupDirectionModel(header));
            }
            mStationsAndSectionList.add(station);
        }
    }

    private void useAdapter(Transport selectedTransport)
    {
        RecyclerView myRecyclerView;
        myRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewSelectedRoute);

        if(myRecyclerView == null)
        {
            Log.e("MainTag", "Not good");
        }
        else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false);
            myRecyclerView.setLayoutManager(linearLayoutManager);
            mStationsAndSectionList = new ArrayList<>();
            ArrayList<String> directions = selectedTransport.getDirections();
            for(String direction : directions)
            {
                ArrayList<Station> stationsList = selectedTransport.getStations(direction);
                getSectionalList(stationsList, mStationsAndSectionList, direction);

            }
            RouteMultiAdapter multiAdapter;
            myRecyclerView.setHasFixedSize(true);
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(mcontext, 1);

            multiAdapter = new RouteMultiAdapter(mStationsAndSectionList, mcontext);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (RouteMultiAdapter.SECTION_VIEW == multiAdapter.getItemViewType(position)) {
                        return 1;
                    }
                    return 1;
                }
            });
            myRecyclerView.setLayoutManager(gridLayoutManager);
            myRecyclerView.setAdapter(multiAdapter);
    }

}
}