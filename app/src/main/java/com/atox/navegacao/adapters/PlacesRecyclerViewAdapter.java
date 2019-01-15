package com.atox.navegacao.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.atox.R;
import com.atox.navegacao.fragments.InicioFragment;
import com.google.android.gms.location.places.Place;

import java.util.List;

public class PlacesRecyclerViewAdapter extends
        RecyclerView.Adapter<PlacesRecyclerViewAdapter.ViewHolder> {

    private List<Place> placesList;
    private Context context;


    public PlacesRecyclerViewAdapter(List<Place> list, Context ctx) {
        placesList = list;
        context = ctx;

    }
    @Override
    public int getItemCount() {
        return placesList.size();
    }

    @Override
    public PlacesRecyclerViewAdapter.ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.places_item, parent, false);

        PlacesRecyclerViewAdapter.ViewHolder viewHolder =
                new PlacesRecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PlacesRecyclerViewAdapter.ViewHolder holder, int position) {
        final int itemPos = position;
        final Place place = placesList.get(position);

        holder.name.setText(place.getName());
        holder.address.setText(place.getAddress());
        //holder.phone.setText(place.getPhoneNumber());
        if(place.getWebsiteUri() != null){
            //holder.website.setText(place.getWebsiteUri().toString());
        }

        if(place.getRating() > -1){
            holder.ratingBar.setNumStars((int)place.getRating());
        }else{
            holder.ratingBar.setVisibility(View.GONE);
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView address;
        //public TextView phone;
        //public TextView website;
        public RatingBar ratingBar;

        public ViewHolder(View view) {

            super(view);

            name = view.findViewById(R.id.name);
            address = view.findViewById(R.id.address);
            //phone = view.findViewById(R.id.phone);
            //website = view.findViewById(R.id.website);
            ratingBar = view.findViewById(R.id.rating);

        }
    }

}