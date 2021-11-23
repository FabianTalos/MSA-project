package com.example.publictransportationapp;

import android.content.Context;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ParseAdapter extends RecyclerView.Adapter<ParseAdapter.ViewHolder> {

    private ArrayList<ParseItem> parseItems;
    private Context context;

    public ParseAdapter(ArrayList<ParseItem> parseItems, Context context) {
        this.parseItems = parseItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ParseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parse_item, parent, false);
        return new RecyclerView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParseAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return parseItems.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder
    {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
