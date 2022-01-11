package com.example.publictransportationapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.publictransportationapp.R;
import com.example.publictransportationapp.activity.SelectedRoute;
import com.example.publictransportationapp.model.FavouriteRoute;
import com.example.publictransportationapp.model.GroupFavouriteRouteModel;
import com.example.publictransportationapp.model.ItemInterface;

import java.util.ArrayList;

public class FavouriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int SECTION_VIEW = 0;
    public static final int CONTENT_VIEW = 1;

    Context context;
    ArrayList<ItemInterface> mFavouriteRoutesAndSectionList;

    public FavouriteAdapter(Context context, ArrayList<ItemInterface> mFavouriteRoutesAndSectionList) {
        this.context = context;
        this.mFavouriteRoutesAndSectionList = mFavouriteRoutesAndSectionList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == SECTION_VIEW)   //if the next view is of type section, inflate the layout with the transport image
        {
            view = layoutInflater.inflate(R.layout.row_item_group_transport_type_image, parent, false);

            return new SectionViewHolder(view);
        } //else, inflate the layout with the actual route names
        view = layoutInflater.inflate(R.layout.row_favourite_route, parent, false);

        return new FavouriteHolder(view);

    }

    public int getItemViewType(int position) {
        if (mFavouriteRoutesAndSectionList.get(position).isSection()) {
            return SECTION_VIEW;
        } else {
            return CONTENT_VIEW;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (context == null) { return; }

        if (SECTION_VIEW == getItemViewType(position))
        {
            SectionViewHolder sectionViewHolder = (SectionViewHolder) holder;
            GroupFavouriteRouteModel sectionItem = (GroupFavouriteRouteModel) mFavouriteRoutesAndSectionList.get(position); //get the header (aka the transport type)
            int imageId;
            switch(sectionItem.getTransportType()){ //depending on the type of the transport, set the image accordingly
                case "bus":
                    imageId = R.drawable.ic_baseline_bus;
                    break;
                case "tram":
                    imageId = R.drawable.ic_baseline_tram_24;
                    break;
                case "trolley":
                    imageId = R.drawable.ic_trolley_transport_svgrepo_com_resized;
                    break;
                default:
                    imageId = R.drawable.ic_baseline_bus;
            }
            sectionViewHolder.transportRouteTypeImage.setImageResource(imageId);
            return;
        }
        //the view was actually of type content and we set the route name in the corresponding layout accordingly
        FavouriteHolder contentHolder = (FavouriteHolder) holder;
        FavouriteRoute route = (FavouriteRoute) mFavouriteRoutesAndSectionList.get(position);
        contentHolder.routeName.setText(route.getFavouriteRouteName());

        contentHolder.routeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SelectedRoute.class);
                intent.putExtra("routeName", contentHolder.routeName.getText());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFavouriteRoutesAndSectionList.size();
    }

    public static class FavouriteHolder extends RecyclerView.ViewHolder {
        TextView routeName;
        public FavouriteHolder(@NonNull View itemView) {
            super(itemView);

            routeName = itemView.findViewById(R.id.routeName);
        }
    }

    public static class SectionViewHolder extends RecyclerView.ViewHolder {
        ImageView transportRouteTypeImage;

        public SectionViewHolder(View itemView) {
            super(itemView);
            transportRouteTypeImage = itemView.findViewById(R.id.routeTypeImage);
        }
    }


}
