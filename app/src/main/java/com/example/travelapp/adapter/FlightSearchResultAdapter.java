package com.example.travelapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.travelapp.databinding.ItemSearchFlightResultBinding;
import com.example.travelapp.model.CityModel;
import com.example.travelapp.model.FlightModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class FlightSearchResultAdapter extends RecyclerView.Adapter<FlightSearchResultAdapter.FlightSearchResultViewHolder>{
    private List<FlightModel> flightModels;
    String seatClass;
    float price;
    onClickFlight listener;

    public FlightSearchResultAdapter(List<FlightModel> flightModels, String seat_class,onClickFlight listener) {
        this.flightModels = flightModels;
        this.seatClass = seat_class;
        this.listener = listener;

    }

    @NonNull
    @Override
    public FlightSearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSearchFlightResultBinding itemSearchFlightResultBinding = ItemSearchFlightResultBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new FlightSearchResultViewHolder(itemSearchFlightResultBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightSearchResultViewHolder holder, int position) {
        FlightModel flightModel = flightModels.get(position);

        Date dateFrom = flightModel.getDeparture_time();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        String formattedDateFrom = sdf.format(dateFrom);
        Date dateTo = flightModel.getArrival_time();
        String formattedDateTo = sdf.format(dateTo);
        long diffInMilliseconds = dateTo.getTime() - dateFrom.getTime();
        long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(diffInMilliseconds);
        long hours = diffInSeconds / 3600;
        long minutes = (diffInSeconds % 3600) / 60;
        String formattedTime = hours + "h" + minutes + "m";
        holder.itemSearchFlightResultBinding.timeFrom.setText(formattedDateFrom);
        holder.itemSearchFlightResultBinding.timeTo.setText(formattedDateTo);
        holder.itemSearchFlightResultBinding.airportCodeFrom.setText(flightModel.getOrigin_model().getAirport_code());
        String airportcode = flightModel.getOrigin_model().getAirport_code();
        holder.itemSearchFlightResultBinding.airportCodeTo.setText(flightModel.getDestination_model().getAirport_code());
        String airportcode2 = flightModel.getDestination_model().getAirport_code();
        holder.itemSearchFlightResultBinding.airline.setText(flightModel.getAirline());
        holder.itemSearchFlightResultBinding.hour.setText(formattedTime);
        Glide.with(holder.itemView.getContext()).load(flightModel.getLogo()).into(holder.itemSearchFlightResultBinding.logo);
        if (seatClass.equals("Economy")) {
            price = flightModel.getEconomy_price();
        } else if (seatClass.equals("Premium Economy")) {
            price = flightModel.getPremium_economy_price();
        } else if (seatClass.equals("Business")) {
            price = flightModel.getBusiness_price();
        } else {
            price = flightModel.getFirst_class_price();
        }
        holder.itemSearchFlightResultBinding.total.setText("$" + price + "/pax");
        holder.itemSearchFlightResultBinding.itemFlightResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(flightModel);
            }
        });



    }

    @Override
    public int getItemCount() {
        if (flightModels!= null){
            return flightModels.size();
        }
        return 0;
    }
    public void updateFlightList(List<FlightModel> filteredFlightList) {
        this.flightModels = filteredFlightList;
        notifyDataSetChanged();
    }

    public static class FlightSearchResultViewHolder extends RecyclerView.ViewHolder{
        ItemSearchFlightResultBinding itemSearchFlightResultBinding;
        public FlightSearchResultViewHolder(@NonNull ItemSearchFlightResultBinding itemView) {
            super(itemView.getRoot());
            this.itemSearchFlightResultBinding = itemView;
        }
    }
    public interface onClickFlight {
        public void onClick(FlightModel flightModel);

    }
}
