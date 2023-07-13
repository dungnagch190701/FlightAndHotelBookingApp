package com.example.travelapp.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.bumptech.glide.Glide;
import com.example.travelapp.databinding.ItemAllFacilitiesBinding;
import com.example.travelapp.databinding.ItemFacilitiesBinding;
import com.example.travelapp.model.FacilitiesModel;

import java.util.List;

public class FacilitiesAdapter extends RecyclerView.Adapter<FacilitiesAdapter.FacilitiesViewHolder>{
    private List<FacilitiesModel> facilitiesModels;
    private boolean mShowAll;
    private static final int TYPE_LONG = 1;
    private static final int TYPE_SHORT = 2;

    public FacilitiesAdapter(List<FacilitiesModel> facilitiesModels,boolean showAll) {
        this.facilitiesModels = facilitiesModels;
        this.mShowAll = showAll;
    }

    @Override
    public int getItemViewType(int position) {
        if (mShowAll) {
            return TYPE_LONG;
        } else {
            return TYPE_SHORT;
        }
    }

    @NonNull
    @Override
    public FacilitiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_SHORT) {
            ItemFacilitiesBinding binding = ItemFacilitiesBinding.inflate(inflater, parent, false);
            return new FacilitiesViewHolder(binding);
        } else {
            ItemAllFacilitiesBinding binding = ItemAllFacilitiesBinding.inflate(inflater, parent, false);
            return new FacilitiesViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FacilitiesViewHolder holder, int position) {
        FacilitiesModel facilitiesModel = facilitiesModels.get(position);
        String url = facilitiesModel.getIcon_url();
        Uri uri = Uri.parse(url);
//        holder.binding.icon.setImageURI(uri);
//        holder.binding.name.setText(facilitiesModel.getName());
        if (holder.binding instanceof ItemFacilitiesBinding) {
            ((ItemFacilitiesBinding) holder.binding).icon.setImageURI(uri);
            ((ItemFacilitiesBinding) holder.binding).name.setText(facilitiesModel.getName());
        } else {
            ((ItemAllFacilitiesBinding) holder.binding).icon.setImageURI(uri);
            ((ItemAllFacilitiesBinding) holder.binding).name.setText(facilitiesModel.getName());
        }
    }

    @Override
    public int getItemCount() {
        if (facilitiesModels!=null){
            if (mShowAll) {
                return facilitiesModels.size();
            } else {
                return Math.min(facilitiesModels.size(), 3);
            }
        }
        return 0;
    }

    public static class FacilitiesViewHolder extends RecyclerView.ViewHolder{
        ViewBinding binding;
        public FacilitiesViewHolder(@NonNull ViewBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
