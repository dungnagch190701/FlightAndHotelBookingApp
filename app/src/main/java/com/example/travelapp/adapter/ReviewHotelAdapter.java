package com.example.travelapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travelapp.R;
import com.example.travelapp.databinding.ItemCommentBinding;
import com.example.travelapp.model.ReviewHotelModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReviewHotelAdapter extends RecyclerView.Adapter<ReviewHotelAdapter.ReviewHotelViewHolder>{
    private List<ReviewHotelModel> reviewHotelModels;
    boolean showAll;

    public ReviewHotelAdapter(List<ReviewHotelModel> reviewHotelModels,boolean showAll) {
        this.reviewHotelModels = reviewHotelModels;
        this.showAll = showAll;
    }

    @NonNull
    @Override
    public ReviewHotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCommentBinding binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ReviewHotelViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHotelViewHolder holder, int position) {
        ReviewHotelModel reviewHotelModel = reviewHotelModels.get(position);
        float review_avg = reviewHotelModel.getRating();
        holder.binding.reviewScore.setText(String.format(Locale.US,"%.1f",review_avg));
        holder.binding.reviewContent.setText(reviewHotelModel.getComment());
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy");
        String date = formatter.format(reviewHotelModel.getDate());
        holder.binding.date.setText(date);
        Glide.with(holder.itemView).load(reviewHotelModel.getUser_id().getProfileImage()).error(R.drawable.no_avatar).into(holder.binding.avatar);


    }

    @Override
    public int getItemCount() {
        if (reviewHotelModels!=null){
            if (showAll) {
                return reviewHotelModels.size();
            } else {
                return Math.min(reviewHotelModels.size(), 3);
            }
        }
        return 0;
    }

    public static class ReviewHotelViewHolder extends RecyclerView.ViewHolder{
        ItemCommentBinding binding;
        public ReviewHotelViewHolder(@NonNull ItemCommentBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
