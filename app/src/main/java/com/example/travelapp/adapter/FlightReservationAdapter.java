package com.example.travelapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travelapp.HotelDetailActivity;
import com.example.travelapp.MyFlightBookingDetailActivity;
import com.example.travelapp.R;
import com.example.travelapp.databinding.ItemMyFlightBookingBinding;
import com.example.travelapp.model.FlightReservation;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

public class FlightReservationAdapter extends RecyclerView.Adapter<FlightReservationAdapter.FlightReservationViewHolder>{
    private List<FlightReservation> flightReservationList;

    public FlightReservationAdapter(List<FlightReservation> flightReservationList) {
        this.flightReservationList = flightReservationList;
    }

    @NonNull
    @Override
    public FlightReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMyFlightBookingBinding binding = ItemMyFlightBookingBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new FlightReservationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightReservationViewHolder holder, int position) {
        FlightReservation flightReservation = flightReservationList.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        String date = dateFormat.format(flightReservation.getFlight().getDeparture_time());
        holder.binding.date.setText(date);
        Glide.with(holder.itemView).load(flightReservation.getFlight().getLogo()).into(holder.binding.logo);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String start  = formatter.format(flightReservation.getFlight().getDeparture_time());
        String end = formatter.format(flightReservation.getFlight().getArrival_time());
        holder.binding.time.setText(start+" - "+end);
        holder.binding.airlineName.setText(flightReservation.getFlight().getAirline());
        holder.binding.passenger.setText(flightReservation.getTotal_passenger()+" passenger(s)");
        holder.binding.totalPrice.setText("$ "+flightReservation.getTotal_amount());
        if (flightReservation.getPayment()==2){
            holder.binding.payment.setText("Cash");
        }
        else {
            holder.binding.payment.setText("ZaloPay");
            holder.binding.payment.setBackgroundResource(R.drawable.zalo_pay_tag);
            holder.binding.payment.setTextColor(holder.itemView.getResources().getColor(R.color.white));
        }
        holder.binding.origin.setText(flightReservation.getFlight().getOrigin_model().getCity_name()+" - "+flightReservation.getFlight().getDestination_model().getCity_name());
        holder.binding.bookingLayoutFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MyFlightBookingDetailActivity.class);
                intent.putExtra("flightReservation",flightReservation);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (flightReservationList!=null){
            return flightReservationList.size();
        }
        return 0;
    }

    public static class FlightReservationViewHolder extends RecyclerView.ViewHolder {
        ItemMyFlightBookingBinding binding;
        public FlightReservationViewHolder(@NonNull ItemMyFlightBookingBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
