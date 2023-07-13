package com.example.travelapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.SearchFlightResultActivity;
import com.example.travelapp.databinding.ItemRecentFlightBinding;
import com.example.travelapp.databinding.ItemSearchFlightResultBinding;
import com.example.travelapp.model.SearchRecentModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class RecentFlightAdapter extends RecyclerView.Adapter<RecentFlightAdapter.RecentFlightViewHolder>{
    private List<SearchRecentModel> arrayList;

    public RecentFlightAdapter(List<SearchRecentModel> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecentFlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecentFlightBinding binding = ItemRecentFlightBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new RecentFlightViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentFlightViewHolder holder, int position) {
        SearchRecentModel searchRecentModel = arrayList.get(position);
        String from_id = searchRecentModel.getFrom_id();
        String to_id = searchRecentModel.getTo_id();
        int passenger_number = searchRecentModel.getPassenger_number();
        String seat_class = searchRecentModel.getSeat_class();
        long longValue = searchRecentModel.getDate();
        String city_name_from = searchRecentModel.getCity_from();
        String city_name_to = searchRecentModel.getCity_to();
        long end_date = searchRecentModel.getEnd_date();
        holder.binding.origin.setText(searchRecentModel.getCity_from());
        holder.binding.destination.setText(searchRecentModel.getCity_to());
        holder.binding.pax.setText(searchRecentModel.getPassenger_number()+" pax");
        holder.binding.seatClass.setText(searchRecentModel.getSeat_class());
        Date date = new Date(searchRecentModel.getDate());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE,dd MMM, yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        String formattedDate = dateFormat.format(date);
        holder.binding.date.setText(formattedDate);
        holder.binding.recentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mActivity = new Intent(view.getContext(), SearchFlightResultActivity.class);
                mActivity.putExtra("from_id",from_id);
                mActivity.putExtra("to_id",to_id);
                mActivity.putExtra("passenger",passenger_number);
                mActivity.putExtra("seat_class",seat_class);
                mActivity.putExtra("departure_date",longValue);
                mActivity.putExtra("city_name_from",city_name_from);
                mActivity.putExtra("city_name_to",city_name_to);
                mActivity.putExtra("end_date",end_date);
                view.getContext().startActivity(mActivity);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (arrayList!=null){
            return arrayList.size();
        }
        return 0;
    }

    public static class RecentFlightViewHolder extends RecyclerView.ViewHolder{
        ItemRecentFlightBinding binding;
        public RecentFlightViewHolder(@NonNull ItemRecentFlightBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
