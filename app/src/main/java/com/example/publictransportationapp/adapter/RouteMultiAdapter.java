package com.example.publictransportationapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.publictransportationapp.R;
import com.example.publictransportationapp.model.GroupDirectionModel;
import com.example.publictransportationapp.model.ItemInterface;
import com.example.publictransportationapp.model.Station;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class RouteMultiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final int SECTION_VIEW = 0;
    public static final int CONTENT_VIEW = 1;

    ArrayList<ItemInterface> mStationsAndSectionList;
    WeakReference<Context> mContextWeakReference;

    public RouteMultiAdapter(ArrayList<ItemInterface> usersAndSectionList, Context context) {

        this.mStationsAndSectionList = usersAndSectionList;
        this.mContextWeakReference = new WeakReference<Context>(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = mContextWeakReference.get();
        if (viewType == SECTION_VIEW) {
            return new SectionViewHolder(LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.row_item_group_direction, parent, false));
        }
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.row_station_item, parent, false), context);
    }

    public int getItemViewType(int position) {
        if (mStationsAndSectionList.get(position).isSection()) {
            return SECTION_VIEW;
        } else {
            return CONTENT_VIEW;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        Context context = mContextWeakReference.get();

        if (context == null) {
            return;
        }

        if (SECTION_VIEW == getItemViewType(position)) {

            SectionViewHolder sectionViewHolder = (SectionViewHolder) holder;
            GroupDirectionModel sectionItem = ((GroupDirectionModel) mStationsAndSectionList.get(position));

            sectionViewHolder.direction.setText(sectionItem.direction);
            return;
        }

        MyViewHolder myViewHolder = (MyViewHolder) holder;

        Station currentStation = ((Station) mStationsAndSectionList.get(position));

        myViewHolder.tv_stationName.setText(currentStation.getStationName());
        myViewHolder.tv_arrivalTime.setText(currentStation.getArrivalTime());

    }


    @Override
    public int getItemCount() {
        return mStationsAndSectionList.size();
    }

    //holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_stationName, tv_arrivalTime;
        public LinearLayout ll;


        public MyViewHolder(View itemView, final Context context) {

            super(itemView);
            tv_stationName = itemView.findViewById(R.id.tv_stationName);
            tv_arrivalTime = itemView.findViewById(R.id.tv_arrivalTime);
            ll = itemView.findViewById(R.id.ll_layout);

        }
    }

    public class SectionViewHolder extends RecyclerView.ViewHolder {
        TextView direction;

        public SectionViewHolder(View itemView) {
            super(itemView);
            direction = itemView.findViewById(R.id.tv_group_direction);
        }
    }
}
