package com.locusapp.locus.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.locusapp.locus.R;
import com.locusapp.locus.activities.app.BountyDetailActivity;
import com.locusapp.locus.models.GeoHandler;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private static final String TAG = "ListAdapter";

    private ArrayList<LatLng> locations = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> ids = new ArrayList<>();
    private Context context;

    public ListAdapter(ArrayList<String> ids, ArrayList<LatLng> locations, ArrayList<String> titles, Context context) {
        this.ids = ids;
        this.locations = locations;
        this.titles = titles;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        holder.lblTitle.setText(titles.get(position));

        GeoHandler geoHandler = new GeoHandler();
        String token = context.getString(R.string.access_token);

        geoHandler.reverseGeocode(token, locations.get(position), new GeoHandler.GeoHandlerCallback() {
            @Override
            public void onCallback(String place) {
                holder.lblLocation.setText(place);
            }
        });



        holder.listLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on:");
                Intent intent = new Intent(context, BountyDetailActivity.class);
                intent.putExtra("id", ids.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView lblTitle, lblLocation;
        ConstraintLayout listLayout;

        ViewHolder(View itemView) {
            super(itemView);

            lblTitle = itemView.findViewById(R.id.lblTitle);
            lblLocation = itemView.findViewById(R.id.lblLocation);
            listLayout = itemView.findViewById(R.id.listLayout);
        }
    }

}
