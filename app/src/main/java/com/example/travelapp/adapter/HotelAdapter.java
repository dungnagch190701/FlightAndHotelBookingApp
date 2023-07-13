package com.example.travelapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travelapp.HotelDetailActivity;
import com.example.travelapp.databinding.ItemHotelSearchBinding;
import com.example.travelapp.model.FlightModel;
import com.example.travelapp.model.HotelModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder>{
    private List<HotelModel> hotelModels;
    private Context context;
    int room,passenger;
    String in_string,out_string;
    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHotelSearchBinding binding = ItemHotelSearchBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new HotelViewHolder(binding);
    }

    public HotelAdapter(ArrayList<HotelModel> hotelModels, Context context, Date check_in_date, Date check_out_date, int room, int passenger) {
        this.hotelModels = hotelModels;
        this.context = context;
        this.room = room;
        this.passenger = passenger;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        in_string = formatter.format(check_in_date);
        out_string = formatter.format(check_out_date);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        HotelModel hotelModel = hotelModels.get(position);
        holder.binding.hotelName.setText(hotelModel.getName());
        holder.binding.address.setText(hotelModel.getAddress());
        holder.binding.location.setText(hotelModel.getLocationModel().getCity()+", "+hotelModel.getLocationModel().getCountry());
        float review_avg = hotelModel.getReview_avg();
        holder.binding.review.setText(String.format(Locale.US,"%.1f",review_avg)+" ("+hotelModel.getTotal_review()+")");
        holder.binding.rating.setRating(hotelModel.getRating());
        holder.binding.tag.setText(hotelModel.getTag());
        holder.binding.priceStartFrom.setText("$ "+hotelModel.getPrice_start_from()+"/room(s)/night(s)");
        Glide.with(holder.itemView.getContext()).load(hotelModel.getPhotoURL().get(0)).into(holder.binding.image);
        holder.binding.layoutHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HotelDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("hotelModel",hotelModel);
                intent.putExtra("check_in_date",in_string);
                intent.putExtra("check_out_date",out_string);
                intent.putExtra("room",room);
                intent.putExtra("passenger",passenger);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (hotelModels!=null){
            return hotelModels.size();
        }
        return 0;
    }
    public void updateHotelList(List<HotelModel> filteredHotel) {
        this.hotelModels = filteredHotel;
        notifyDataSetChanged();
    }

    public static class HotelViewHolder extends RecyclerView.ViewHolder{
        ItemHotelSearchBinding binding;
        public HotelViewHolder(@NonNull ItemHotelSearchBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
