package com.example.publictransportationapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.publictransportationapp.model.Transport;

import java.util.ArrayList;
import java.util.HashMap;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    Context context;
    ArrayList<Transport> list;

    public Adapter(Context context, ArrayList<Transport> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false); //wish I knew
        return  new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Transport transport = list.get(position);
        holder.transportType.setText(transport.getTransportType());
        holder.routeName.setText(transport.getRouteName());
        holder.directions.setText((CharSequence) transport.getDirections());
        holder.stations.setText((CharSequence) transport.getStations());

    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView transportType, routeName, directions, stations; //the items we view inside the item.xml, where id=tvtype for example

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            transportType = itemView.findViewById(R.id.tv_transportType);
            routeName = itemView.findViewById(R.id.tv_routeName);
            directions = itemView.findViewById(R.id.tv_directions);
            stations = itemView.findViewById(R.id.tv_stations);
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}