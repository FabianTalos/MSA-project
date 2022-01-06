package com.example.publictransportationapp.adapter;

import static com.example.publictransportationapp.Tools.UsefulMethods.getHashMapKeyFromIndex;

import android.widget.GridLayout;
import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.publictransportationapp.R;
import com.example.publictransportationapp.model.Station;
import com.example.publictransportationapp.model.Transport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteHolder> {
    Context context;
    Transport transport;

    public RouteAdapter(Context context, Transport transport) {
        this.context = context;
        this.transport = transport;
    }

    @NonNull
    @Override
    public RouteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transport_item, parent, false);
        //return new RouteHolder(view);
        return new RouteHolder(LayoutInflater.from(context).inflate(R.layout.transport_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RouteHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.tv_routeName.setText(transport.getRouteName().toUpperCase());  //set name of route
        ArrayList<String> directions = transport.getDirections();

        HashMap<String, ArrayList<Station>> stationsMap = transport.getStationsMap();
        ArrayList<Station> stationsList = stationsMap.get(getHashMapKeyFromIndex(stationsMap, position));

        holder.tv_direction.setText(getHashMapKeyFromIndex(stationsMap, position));
        holder.llContainer.removeAllViews();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int len = stationsList.size();
        Map<Integer, View> viewsMap = new HashMap<>();

        for(int i = 0; i < stationsList.size(); i++)
        {
            View view = layoutInflater.inflate(R.layout.row_station_timestamp, null);
            TextView tv_stationName = view.findViewById(R.id.tv_stationName);
            TextView tv_arrivalTime = view.findViewById(R.id.tv_arrivalTime);
            tv_stationName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            tv_arrivalTime.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            tv_stationName.setText(stationsList.get(i).getStationName());
            tv_arrivalTime.setText(stationsList.get(i).getArrivalTime());
            //view.invalidate();
            if (view.getParent() != null) {
                ((ViewGroup)view.getParent()).removeView(view);
                Log.e("MainTag", "Removed view");
            }
            else {
                Log.e("MainTag", "Added view");
            }
            holder.llContainer.addView(view, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        }


    }

    @Override
    public int getItemCount() {
        return transport.getStations().size();
    }

    public class RouteHolder extends RecyclerView.ViewHolder {
        TextView tv_routeName, tv_direction;
        LinearLayout llContainer;
        public RouteHolder(@NonNull View view) {
            super(view);

            this.tv_routeName = view.findViewById(R.id.tv_routeName);
            this.tv_direction = view.findViewById(R.id.tv_direction);
            this.llContainer = view.findViewById(R.id.llContainer);
        }
    }
}
