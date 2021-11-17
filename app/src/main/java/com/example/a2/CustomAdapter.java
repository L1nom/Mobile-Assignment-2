package com.example.a2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    //    arraylist of Location object, locationSource holds all entries
    private Context context;
    private Activity activity;
    private ArrayList<Location> locations;
    private ArrayList<Location> locationSource;


    CustomAdapter(Activity activity, Context context, ArrayList locations) {
        this.activity = activity;
        this.context = context;
        this.locations = locations;
        locationSource = locations;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    // On click of card in recycle view, get postion of the card and retrieve the data for viewing in the next activity
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.location_id_txt.setText(String.valueOf((position + 1)));
        holder.location_lat_txt.setText(String.valueOf(locations.get(position).getLatitude()));
        holder.location_lng_txt.setText(String.valueOf(locations.get(position).getLongitude()));
        holder.location_adr_txt.setText(String.valueOf(locations.get(position).getAddress()));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(locations.get(position).getId()));
                intent.putExtra("latitude", String.valueOf(locations.get(position).getLatitude()));
                intent.putExtra("longitude", String.valueOf(locations.get(position).getLongitude()));
                intent.putExtra("address", String.valueOf(locations.get(position).getAddress()));

                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }


    //    find ids to set for text values
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView location_id_txt, location_lat_txt, location_lng_txt, location_adr_txt;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            location_id_txt = itemView.findViewById(R.id.location_id_text);
            location_lat_txt = itemView.findViewById(R.id.location_lat_text);
            location_lng_txt = itemView.findViewById(R.id.location_lng_text);
            location_adr_txt = itemView.findViewById(R.id.location_adr_text);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }


    //    Search function. We pass each entry temp to location and display it.
    public void searchLocations(final String searchKeyword) {

        if (searchKeyword.trim().isEmpty() || searchKeyword.trim().equals("") || searchKeyword.trim().equals(" ")) {
            locations = locationSource;
        } else {
            ArrayList<Location> temp = new ArrayList<>();
            for (Location location : locationSource) {
                if (location.getAddress().toLowerCase().contains(searchKeyword.toLowerCase())) {
                    temp.add(location);
                }
            }
            locations = temp;
        }

        new Handler(Looper.getMainLooper()).post(() -> notifyDataSetChanged());

    }


}
