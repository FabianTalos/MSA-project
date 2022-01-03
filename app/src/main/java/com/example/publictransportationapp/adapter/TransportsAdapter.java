package com.example.publictransportationapp.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.publictransportationapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TransportsAdapter extends RecyclerView.Adapter<TransportsAdapter.TransportsHolder>{
    HashMap<String, ArrayList<String>> routes;
    Context context;
    private SparseBooleanArray expandState = new SparseBooleanArray();

    public TransportsAdapter(HashMap<String, ArrayList<String>> routes, Context context) {
        this.routes = routes;
        this.context = context;
    }

    @NonNull
    @Override
    public TransportsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_name_item, parent, false);
        return new TransportsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransportsHolder holder, int position) {
        final boolean isExpanded = expandState.get(position);

        //Do not recycle this view
        holder.setIsRecyclable(false);
        //Set the transport type (bus, tram, trolley)
        holder.transportType.setText(getHashMapKeyFromIndex(routes, position));
        //If clicked, set visibility or invisibility
        holder.expandableLayout.setVisibility(View.VISIBLE); //isExpanded ? View.VISIBLE : View.GONE
        //Remove old views, prepare holder for a new set of views
        holder.llContainer.removeAllViews();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ArrayList<String> routesList = routes.get(getHashMapKeyFromIndex(routes, position));

        for (int i = 0; i < routesList.size(); i++)
        {
            //Inflate a view with routes corresponding to this transport type
            View view = layoutInflater.inflate(R.layout.row_route_child, null);
            //ViewGroup.LayoutParams layoutParams = new GridLayout.LayoutParams();
            //view.setLayoutParams(layoutParams);
            TextView tv_routeName = view.findViewById(R.id.tv_routeName);
            tv_routeName.setText(routesList.get(i));
            //Log.e("MainTag", "tv_routeName is set to " + routesList.get(i));
            //Add this view to the container inside the initial xml file
            holder.llContainer.addView(view);
        }

        holder.buttonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButton(holder.expandableLayout, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    public static class TransportsHolder extends RecyclerView.ViewHolder{
        //textView_name should be transportType
        TextView transportType;
        LinearLayout expandableLayout;
        RelativeLayout buttonLayout;
        LinearLayout llContainer;

        public TransportsHolder(@NonNull View view) {
            super(view);
            transportType = view.findViewById(R.id.transportType);
            expandableLayout = view.findViewById(R.id.expandableLayout);
            buttonLayout = view.findViewById(R.id.button);
            llContainer = view.findViewById(R.id.llContainer);
        }
    }

    private static String getHashMapKeyFromIndex(HashMap hashMap, int index) {

        String key = null;
        HashMap<String, Object> hs = hashMap;
        int pos = 0;
        for (Map.Entry<String, Object> entry : hs.entrySet()) {
            if (index == pos) {
                key = entry.getKey();
            }
            pos++;
        }
        return key;
    }

    private void onClickButton(LinearLayout expandableLayout, int i) {

        if (expandableLayout.getVisibility() == View.VISIBLE) {
            expandableLayout.setVisibility(View.GONE);
            expandState.put(i, false);
        } else {
            expandableLayout.setVisibility(View.VISIBLE);
            expandState.put(i, true);
        }
    }
}
