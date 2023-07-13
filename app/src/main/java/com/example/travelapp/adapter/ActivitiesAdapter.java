package com.example.travelapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travelapp.R;
import com.example.travelapp.databinding.ItemPopularBinding;
import com.example.travelapp.model.ActivitiesModel;
import com.example.travelapp.model.HotelModel;

import java.util.List;

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ActivitiesViewHolder>{
    private List<HotelModel> activitiesModelList;


    public ActivitiesAdapter(List<HotelModel> activitiesModelList) {
        this.activitiesModelList = activitiesModelList;
    }

    @NonNull
    @Override
    public ActivitiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPopularBinding itemPopularBinding = ItemPopularBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ActivitiesViewHolder(itemPopularBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull ActivitiesViewHolder holder, int position) {

        HotelModel activitiesModel = activitiesModelList.get(position);
        if (activitiesModel==null){
            return;
        }
//        holder.binding.location.setText(activitiesModel.getDestination());
        holder.binding.title.setText(activitiesModel.getName());
        holder.binding.price.setText("$" + (int) activitiesModel.getPrice_start_from());
        holder.binding.location.setText(activitiesModel.getLocationModel().getCity()+", "+activitiesModel.getLocationModel().getCountry());
        holder.binding.rating.setRating(activitiesModel.getRating());
        if (activitiesModel.getPhotoURL()!=null){
            Glide.with(holder.itemView).load(activitiesModel.getPhotoURL().get(0))
                    .into(holder.binding.image);
        }
        holder.binding.reviewAvg.setText(String.valueOf(activitiesModel.getReview_avg()));

    }

    @Override
    public int getItemCount() {
        if (activitiesModelList!= null){
            return activitiesModelList.size();
        }
        return 0;
    }

    public static class ActivitiesViewHolder extends RecyclerView.ViewHolder{
        ItemPopularBinding binding;
        public ActivitiesViewHolder(@NonNull ItemPopularBinding itemPopularBinding) {
            super(itemPopularBinding.getRoot());
            this.binding = itemPopularBinding;

        }
    }
}
