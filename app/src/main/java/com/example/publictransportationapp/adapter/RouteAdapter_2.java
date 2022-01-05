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

public class RouteAdapter_2 extends RecyclerView.Adapter<RouteAdapter_2.RouteHolder> {
    Context context;
    String routeName;
    String direction;
    ArrayList<Station> stations;



    public RouteAdapter_2(Context mcontext, String routeName, String direction, ArrayList<Station> stations) {
        this.context = mcontext;
        this.routeName = routeName;
        this.direction = direction;
        this.stations = stations;
    }

    @NonNull
    @Override
    public RouteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RouteHolder(LayoutInflater.from(context).inflate(R.layout.transport_item, null, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull RouteAdapter_2.RouteHolder holder, int position) {
        //holder.setIsRecyclable(false);
        TextView tv_routeName = holder.tv_routeName;
        TextView tv_direction = holder.tv_direction;
        tv_routeName.setText(routeName.toUpperCase());  //set name of route
        tv_direction.setText(direction);
        holder.llContainer.removeAllViews();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.row_station_timestamp, holder.llContainer, false); //holder.llcontainer as root
        TextView tv_stationName, tv_arrivalTime;
        tv_stationName = view.findViewById(R.id.tv_stationName);
        tv_arrivalTime = view.findViewById(R.id.tv_arrivalTime);
        tv_stationName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv_arrivalTime.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        Station station = stations.get(position);
        tv_stationName.setText(station.getStationName());
        tv_arrivalTime.setText(station.getArrivalTime());
        Log.e("MainTag", "Pos: " + position + " and Station: " + station);
        holder.llContainer.addView(view, -1);
        view.invalidate();

    }

    @Override
    public int getItemCount() {
        Log.e("MainTag", "Inside getItemCount");
        return stations.size();
    }

    public class RouteHolder extends RecyclerView.ViewHolder {
        TextView tv_routeName, tv_direction;
        LinearLayout llContainer, pparent;
        public RouteHolder(@NonNull View view) {
            super(view);

            this.tv_routeName = view.findViewById(R.id.tv_routeName);
            this.tv_direction = view.findViewById(R.id.tv_direction);
            this.llContainer = view.findViewById(R.id.llContainer);
            this.pparent = view.findViewById(R.id.pparent);
        }
    }
}
