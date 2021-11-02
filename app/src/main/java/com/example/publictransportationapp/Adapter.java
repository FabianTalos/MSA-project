package com.example.publictransportationapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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
        holder.number.setText(transport.getNumber());
        holder.type.setText(transport.getType());
        holder.start.setText(transport.getStart());
        holder.stop.setText(transport.getStop());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView number, type, start, stop; //the items we view inside the item.xml, where id=tvtype for example

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            number = itemView.findViewById(R.id.tvnumber);
            type = itemView.findViewById(R.id.tvtype);
            start = itemView.findViewById(R.id.tvstart);
            stop = itemView.findViewById(R.id.tvstop);

        }
    }

}