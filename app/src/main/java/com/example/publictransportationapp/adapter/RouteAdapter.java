package com.example.publictransportationapp.adapter;

import static com.example.publictransportationapp.Tools.UsefulMethods.getHashMapKeyFromIndex;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.publictransportationapp.R;
import com.example.publictransportationapp.model.Station;
import com.example.publictransportationapp.model.Transport;

import java.util.ArrayList;
import java.util.HashMap;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteHolder> {
    Context context;
    String routeName;
    ArrayList<String> directions;
    HashMap<String, ArrayList<Station>> stations;
    View view;
    TextView tv_stationName, tv_arrivalTime;
    public RouteAdapter(Context mcontext, String routeName, ArrayList<String> directions, HashMap<String, ArrayList<Station>> stations) {
        this.context = mcontext;
        this.routeName = routeName;
        this.directions = directions;
        this.stations = stations;
    }

    @NonNull
    @Override
    public RouteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e("MainTag", "Created view holder");
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.view = layoutInflater.inflate(R.layout.row_station_timestamp, null);
        tv_stationName = view.findViewById(R.id.tv_stationName);
        tv_arrivalTime = view.findViewById(R.id.tv_arrivalTime);
        return new RouteHolder(LayoutInflater.from(context).inflate(R.layout.transport_item, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull RouteHolder holder, int position) {
        Log.e("MainTag", "Inside onBindViewHolder");
        holder.tv_routeName.setText(routeName.toUpperCase());  //set name of route

        HashMap<String, ArrayList<Station>> stationsMap = this.stations;
        ArrayList<Station> stationsList = stationsMap.get(getHashMapKeyFromIndex(stationsMap, position));

        holder.tv_direction.setText(getHashMapKeyFromIndex(stationsMap, position));
        holder.llContainer.removeAllViews();
        //LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View view = layoutInflater.inflate(R.layout.row_station_timestamp, null);

        //TextView tv_stationName = view.findViewById(R.id.tv_stationName);
        //TextView tv_arrivalTime = view.findViewById(R.id.tv_arrivalTime);
        //tv_stationName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        //tv_arrivalTime.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        for(Station station : stationsList)
        {
            tv_stationName.setText(station.getStationName());
            tv_arrivalTime.setText(station.getArrivalTime());
        }
        holder.llContainer.addView(view);
        /*
        for(int i = 0; i < stationsList.size(); i++)
        {
            tv_stationName.setText(stationsList.get(i).getStationName());
            tv_arrivalTime.setText(stationsList.get(i).getArrivalTime());
        }
            if (view.getParent() != null) {
                ((ViewGroup)view.getParent()).removeView(view);
                Log.e("MainTag", "Removed view");
            }
            else {
                Log.e("MainTag", "Added view");
            }*/
            //holder.llContainer.addView(view);//, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //holder.llContainer.onViewAdded(view);




    }

    @Override
    public int getItemCount() {
        Log.e("MainTag", "Inside getItemCount");
        return stations.size();
    }

    public class RouteHolder extends RecyclerView.ViewHolder {
        TextView tv_routeName, tv_direction;
        LinearLayout llContainer;
        public RouteHolder(@NonNull View view) {
            super(view);

            this.tv_routeName = view.findViewById(R.id.tv_routeName);
            this.tv_direction = view.findViewById(R.id.tv_direction);
            this.llContainer = view.findViewById(R.id.llContainer);
            Log.e("MainTag", "Inside RouteHolder constructor");
        }
    }
}
