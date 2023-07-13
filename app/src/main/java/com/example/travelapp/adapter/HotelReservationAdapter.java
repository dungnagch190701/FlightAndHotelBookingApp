package com.example.travelapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travelapp.MyFlightBookingDetailActivity;
import com.example.travelapp.MyHotelBookingDetailActivity;
import com.example.travelapp.databinding.ItemMyHotelBookingBinding;
import com.example.travelapp.model.HotelReservationModel;

import java.text.SimpleDateFormat;
import java.util.List;

public class HotelReservationAdapter extends RecyclerView.Adapter<HotelReservationAdapter.HotelReservationViewHolder>{

    private List<HotelReservationModel> HotelReservationAdapter;

    public HotelReservationAdapter(List<HotelReservationModel> hotelReservationAdapter) {
        HotelReservationAdapter = hotelReservationAdapter;
    }

    @NonNull
    @Override
    public HotelReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMyHotelBookingBinding binding = ItemMyHotelBookingBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new HotelReservationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelReservationViewHolder holder, int position) {
        HotelReservationModel hotelReservationModel = HotelReservationAdapter.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
        String check_in_date = dateFormat.format(hotelReservationModel.getCheck_in_date());
        String check_out_date = dateFormat.format(hotelReservationModel.getCheck_out_date());
        Glide.with(holder.itemView).load(hotelReservationModel.getHotelModel().getPhotoURL().get(0)).into(holder.binding.hotelImage);
        holder.binding.hotelAddress.setText(hotelReservationModel.getHotelModel().getAddress()+", "+hotelReservationModel.getHotelModel().getLocationModel().getCity()+", "+hotelReservationModel.getHotelModel().getLocationModel().getCountry());
        holder.binding.hotelName.setText(hotelReservationModel.getHotelModel().getName());
        holder.binding.roomTypeName.setText(hotelReservationModel.getRoomTypeModel().getName());
        holder.binding.checkInDate.setText(check_in_date);
        holder.binding.checkOutDate.setText(check_out_date);
        holder.binding.layoutHotelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MyHotelBookingDetailActivity.class);
                intent.putExtra("hotelReservationModel",hotelReservationModel);
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (HotelReservationAdapter!=null){
            return HotelReservationAdapter.size();
        }
        return 0;
    }

    public static class HotelReservationViewHolder extends RecyclerView.ViewHolder {
        ItemMyHotelBookingBinding binding;
        public HotelReservationViewHolder(@NonNull ItemMyHotelBookingBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
