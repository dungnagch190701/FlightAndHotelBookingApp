package com.example.travelapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.FlightPaymentActivity;
import com.example.travelapp.HotelDetailActivity;
import com.example.travelapp.databinding.ItemRoomTypeBinding;
import com.example.travelapp.model.HotelModel;
import com.example.travelapp.model.RoomTypeModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RoomTypeAdapter extends RecyclerView.Adapter<RoomTypeAdapter.RoomTypeViewHolder> {
    private List<RoomTypeModel> roomTypeModels;
    PhotoAdapter photoAdapter;
    Context context;
    HotelModel hotelModel;
    String in_string,out_string;
    int room,passenger;

    public RoomTypeAdapter(List<RoomTypeModel> roomTypeModels, Context context, HotelModel hotelModel, Date check_in_date, Date check_out_date, int room, int passenger) {
        this.roomTypeModels = roomTypeModels;
        this.context = context;
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy");
        in_string = formatter.format(check_in_date);
        out_string = formatter.format(check_out_date);
        this.room = room;
        this.passenger = passenger;
        this.hotelModel = hotelModel;
    }

    @NonNull
    @Override
    public RoomTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRoomTypeBinding binding = ItemRoomTypeBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new RoomTypeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomTypeViewHolder holder, int position) {
        RoomTypeModel roomTypeModel = roomTypeModels.get(position);
        photoAdapter = new PhotoAdapter(context,roomTypeModel.getPhoto_url());
        holder.binding.typeName.setText(roomTypeModel.getName());
        holder.binding.guestPerRoom.setText(String.valueOf(roomTypeModel.getGuest_per_room()));
        holder.binding.image.setAdapter(photoAdapter);
        holder.binding.price.setText("$ "+roomTypeModel.getPrice_per_night());
        holder.binding.indicator.setViewPager(holder.binding.image);
        photoAdapter.registerDataSetObserver(holder.binding.indicator.getDataSetObserver());
        holder.binding.selectRoomType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FlightPaymentActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("hotelModel",hotelModel);
                intent.putExtra("roomTypeModel",roomTypeModel);
                intent.putExtra("check_in_date",in_string);
                intent.putExtra("check_out_date",out_string);
                intent.putExtra("room",room);
                intent.putExtra("passenger",passenger);
                intent.putExtra("type",2);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        if (roomTypeModels!=null){
            return roomTypeModels.size();
        }
        return 0;
    }

    public static class RoomTypeViewHolder extends RecyclerView.ViewHolder{
        ItemRoomTypeBinding binding;
        public RoomTypeViewHolder(@NonNull ItemRoomTypeBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
