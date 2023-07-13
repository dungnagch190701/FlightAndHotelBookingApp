package com.example.travelapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travelapp.HotelDetailActivity;
import com.example.travelapp.databinding.ItemViewedHotelBinding;
import com.example.travelapp.model.HotelModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ViewedHotelAdapter extends RecyclerView.Adapter<ViewedHotelAdapter.ViewedHotelViewHolder>{
    private List<HotelModel> hotelModelList;

    public ViewedHotelAdapter(List<HotelModel> hotelModelList) {
        this.hotelModelList = hotelModelList;
    }

    @NonNull
    @Override
    public ViewedHotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewedHotelBinding binding = ItemViewedHotelBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewedHotelViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewedHotelViewHolder holder, int position) {
        HotelModel hotelModel = hotelModelList.get(position);
        Glide.with(holder.itemView.getContext()).load(hotelModel.getPhotoURL().get(0)).into(holder.binding.imageView);
        holder.binding.hotelName.setText(hotelModel.getName());
        holder.binding.city.setText(hotelModel.getLocationModel().getCity()+", "+hotelModel.getLocationModel().getCountry());
        holder.binding.rating.setRating(hotelModel.getRating());
        holder.binding.priceStart.setText("Start from $"+hotelModel.getPrice_start_from());
        holder.binding.viewedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), HotelDetailActivity.class);
                intent.putExtra("hotelModel",hotelModel);
                Calendar calendar = Calendar.getInstance();
                Date currentDate = calendar.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String checkInDate = formatter.format(currentDate);
                // Cộng thêm một ngày cho ngày hiện tại
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                Date tomorrowDate = calendar.getTime();
                // Đặt giá trị mặc định cho check_out_date là ngày hôm sau
                String checkOutDate = formatter.format(tomorrowDate);
                intent.putExtra("check_in_date", checkInDate);
                intent.putExtra("check_out_date", checkOutDate);
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

    public static class ViewedHotelViewHolder extends RecyclerView.ViewHolder {
        ItemViewedHotelBinding binding;
        public ViewedHotelViewHolder(@NonNull ItemViewedHotelBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
