package com.example.publictransportationapp.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.publictransportationapp.R;
import com.example.publictransportationapp.Tools.UserPreferences;
import com.example.publictransportationapp.adapter.FavouriteAdapter;
import com.example.publictransportationapp.model.FavouriteRoute;
import com.example.publictransportationapp.model.GroupFavouriteRouteModel;
import com.example.publictransportationapp.model.ItemInterface;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_favorites, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewFavourites);

        ArrayList<FavouriteRoute> favouriteRoutes = UserPreferences.getInstance().getListOfFavouriteRoutes(getContext());

        ArrayList<ItemInterface> mFavouriteRoutesAndSectionList = new ArrayList<>();
        ArrayList<String> transportTypes = new ArrayList<>();
        for(FavouriteRoute route : favouriteRoutes) //get the transport types that are present in the user's saved favourite transports
        {
            String tempTransportType = route.getFavouriteRouteType();
            if(!transportTypes.contains(tempTransportType))
            {
                transportTypes.add(tempTransportType);
            }
        }

        for(String transportType : transportTypes)
        {
            // for each different transport type (ex: bus, tram, trolley)
            // make a sectional list and append it to the list we will send to the adapter
            ArrayList<FavouriteRoute> transportListOfDifferentType = new ArrayList<>();
            for(FavouriteRoute route : favouriteRoutes)
            {
                if(route.getFavouriteRouteType().equals(transportType))
                    transportListOfDifferentType.add(route);
            }
            getSectionalList(transportListOfDifferentType, mFavouriteRoutesAndSectionList, transportType);

        }


        FavouriteAdapter favouriteAdapter = new FavouriteAdapter(getContext(), mFavouriteRoutesAndSectionList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(favouriteAdapter);

        return view;
    }

    private void getSectionalList(ArrayList<FavouriteRoute> favouriteRoutesList, ArrayList<ItemInterface> mFavouriteRoutesAndSectionList, String transportType) {

        String lastHeader = "";
        int size = favouriteRoutesList.size();
        for (int i = 0; i < size; i++) { //go through list of favourite routes of the same type
            FavouriteRoute favouriteRoute = favouriteRoutesList.get(i); //get the first route
            String header = transportType;  //the section header will be named as the transport type we passed in this method (bus, trolley etc)
            if (!TextUtils.equals(lastHeader, header))  //make sure to actually set the header for all transports (first the header, then the actual route)
            {
                lastHeader = header;
                mFavouriteRoutesAndSectionList.add(new GroupFavouriteRouteModel(header));
            }
            mFavouriteRoutesAndSectionList.add(favouriteRoute);
        }
    }

}
