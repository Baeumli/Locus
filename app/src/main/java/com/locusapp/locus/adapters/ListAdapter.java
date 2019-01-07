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
import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private static final String TAG = "ListAdapter";

    private ArrayList<String> locations = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> ids = new ArrayList<>();
    private Context context;

    public ListAdapter(ArrayList<String> ids, ArrayList<String> locations, ArrayList<String> titles, Context context) {
        this.ids = ids;
        this.locations = locations;
        this.titles = titles;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        holder.lblTitle.setText(titles.get(position));
        holder.lblLocation.setText(locations.get(position));

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

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView lblTitle, lblLocation;
        ConstraintLayout listLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            lblTitle = itemView.findViewById(R.id.lblTitle);
            lblLocation = itemView.findViewById(R.id.lblLocation);
            listLayout = itemView.findViewById(R.id.listLayout);
        }
    }

}
