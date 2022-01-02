package com.example.publictransportationapp.adapter;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.publictransportationapp.R;
import com.example.publictransportationapp.model.Station;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapAdapter extends RecyclerView.Adapter<MapAdapter.MyHolder>{
    HashMap<String, ArrayList<Station>> stationsMap;
    Context context;

    private SparseBooleanArray expandState = new SparseBooleanArray();  //What is this?

    public MapAdapter(Context context, HashMap<String, ArrayList<Station>> stationsMap) {
        this.stationsMap = stationsMap;
        this.context = context;
        for (int i = 0; i < stationsMap.size(); i++) {
            expandState.append(i, false);
        }

    }

    @NonNull
    @Override
    public MapAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_map, null, false);//wish I knew
                //LayoutInflater.from(context).inflate(R.layout.list_item_map,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setIsRecyclable(false);
        holder.textView_name.setText(getHashMapKeyFromIndex(stationsMap, position));
        final boolean isExpanded = expandState.get(position);

        Log.e("check", "is expanded? " + isExpanded);   //CHANGE THIS SHIT TO A CORRECT TRUE OR FALSE
        holder.expandableLayout.setVisibility(1); //isExpanded ? View.VISIBLE : View.GONE
        //Log.e("check", "" + stationsMap.get(getHashMapKeyFromIndex(stationsMap, position)).size());
        //llContainer
        holder.llContainer.removeAllViews();
        LayoutInflater layoutInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < stationsMap.get(getHashMapKeyFromIndex(stationsMap, position)).size(); i++) {
            //Log.e("check", stationsMap.get(getHashMapKeyFromIndex(stationsMap, position)).get(i).getStationName());
            View view = layoutInflator.inflate(R.layout.row_subcat_child, null);
            TextView tv_stationName = view.findViewById(R.id.tv_stationName);
            TextView tv_arrivalTime = view.findViewById(R.id.tv_arrivalTime);
            tv_stationName.setText(stationsMap.get(getHashMapKeyFromIndex(stationsMap, position)).get(i).getStationName());
            tv_arrivalTime.setText(String.valueOf(stationsMap.get(getHashMapKeyFromIndex(stationsMap, position)).get(i).getArrivalTime()));
            holder.llContainer.addView(view);
            //Log.e("check", "View added to holder");
        }


        holder.buttonLayout.setRotation(expandState.get(position) ? 180f : 0f);
        holder.buttonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onClickButton(holder.expandableLayout, holder.buttonLayout, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stationsMap.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {

        TextView textView_name,textView_total;
        LinearLayout expandableLayout, llContainer;
        RelativeLayout buttonLayout;

        public MyHolder(View view) {
            super(view);
            textView_name = view.findViewById(R.id.textView_name);
            expandableLayout = view.findViewById(R.id.expandableLayout);
            buttonLayout = view.findViewById(R.id.button);
            llContainer = view.findViewById(R.id.llContainer);
            //Log.e("check", "Holder initialized");
        }
    }

    public static String getHashMapKeyFromIndex(HashMap hashMap, int index) {

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

    private ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }

    private void onClickButton(final LinearLayout expandableLayout, final RelativeLayout buttonLayout, final int i) {

        //Simply set View to Gone if not expanded
        //Not necessary but I put simple rotation on button layout
        if (expandableLayout.getVisibility() == View.VISIBLE) {
            createRotateAnimator(buttonLayout, 180f, 0f).start();
            expandableLayout.setVisibility(View.GONE);
            expandState.put(i, false);
        } else {
            createRotateAnimator(buttonLayout, 0f, 180f).start();
            expandableLayout.setVisibility(View.VISIBLE);
            expandState.put(i, true);
        }
    }
}
