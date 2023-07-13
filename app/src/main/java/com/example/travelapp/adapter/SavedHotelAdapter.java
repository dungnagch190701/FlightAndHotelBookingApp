package com.example.travelapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.HotelDetailActivity;
import com.example.travelapp.databinding.ItemSavedHotelBinding;
import com.example.travelapp.model.HotelModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SavedHotelAdapter extends RecyclerView.Adapter<SavedHotelAdapter.SavedHotelViewHolder>{
    private List<HotelModel> hotelModelList;
    PhotoAdapter photoAdapter;

    public SavedHotelAdapter(List<HotelModel> hotelModelList) {
        this.hotelModelList = hotelModelList;
    }

    @NonNull
    @Override
    public SavedHotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSavedHotelBinding binding = ItemSavedHotelBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new SavedHotelViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHotelViewHolder holder, int position) {
        Calendar calendar = Calendar.getInstance();
        Date startDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date endDate = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String check_in_date = formatter.format(startDate);
        String check_out_date = formatter.format(endDate);
        HotelModel hotelModel = hotelModelList.get(position);
        photoAdapter = new PhotoAdapter(holder.itemView.getContext(),hotelModel.getPhotoURL());
        holder.binding.image.setAdapter(photoAdapter);
        holder.binding.indicator.setViewPager(holder.binding.image);
        holder.binding.address.setText(hotelModel.getAddress());
        holder.binding.tag.setText(hotelModel.getTag());
        holder.binding.rating.setRating(hotelModel.getRating());
        holder.binding.location.setText(hotelModel.getLocationModel().getCity()+", "+hotelModel.getLocationModel().getCountry());
        photoAdapter.registerDataSetObserver(holder.binding.indicator.getDataSetObserver());
        holder.binding.layoutHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), HotelDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("hotelModel",hotelModel);
                intent.putExtra("check_in_date",check_in_date);
                intent.putExtra("check_out_date",check_out_date);
                intent.putExtra("room",1);
                intent.putExtra("passenger",1);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (hotelModelList!=null){
            return hotelModelList.size();
        }
        return 0;
    }

    public static class SavedHotelViewHolder extends RecyclerView.ViewHolder {
        ItemSavedHotelBinding binding;
        public SavedHotelViewHolder(@NonNull ItemSavedHotelBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
