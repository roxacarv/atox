package com.atox.navegacao.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.atox.R;
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
        final Place place = placesList.get(position);

        holder.name.setText(place.getName());
        holder.address.setText(place.getAddress());
        if(place.getWebsiteUri() != null){
            //holder.website.setText(place.getWebsiteUri().toString());
        }

        if(place.getRating() > -1){
            holder.ratingBar.setNumStars((int)place.getRating());
        }else{
            holder.ratingBar.setVisibility(View.GONE);
        }
        holder.name.setOnClickListener(onClickListener(position));

    }

    private View.OnClickListener onClickListener(final int position) {
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.activity_popup_estabelecimento);
                dialog.setTitle("Position" + position);
                dialog.setCancelable(true);
                TextView name = dialog.findViewById(R.id.name);
                TextView address= dialog.findViewById(R.id.address);
                TextView resumo= dialog.findViewById(R.id.resumo);
                setDataToView(name,address,resumo,position);
                dialog.show();
            }
        };
    }

    private void setDataToView(TextView name,TextView address, TextView resumo,int position){
        final Place place = placesList.get(position);
        name.setText(place.getName());
        address.setText(place.getAddress());
        CharSequence conversorHtml = place.getAttributions();
        if (conversorHtml == null) {
            conversorHtml = "";
        }
        String saidaHtml = (String) conversorHtml;
        resumo.setText(Html.fromHtml(saidaHtml));
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView address;
        private RatingBar ratingBar;

        public ViewHolder(View view) {

            super(view);
            name = view.findViewById(R.id.name);
            address = view.findViewById(R.id.address);
            ratingBar = view.findViewById(R.id.rating);

        }
    }

}