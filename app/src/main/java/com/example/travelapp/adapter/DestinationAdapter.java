package com.example.travelapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.databinding.ItemCityBinding;
import com.example.travelapp.model.Destination;

import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder>{
    private List<Destination> destinationList;
    clickDestination clickDestination;

    public DestinationAdapter(List<Destination> destinationList,clickDestination clickDestination) {
        this.destinationList = destinationList;
        this.clickDestination = clickDestination;
    }

    @NonNull
    @Override
    public DestinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCityBinding itemCityBinding = ItemCityBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new DestinationViewHolder(itemCityBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationViewHolder holder, int position) {
        Destination destination = destinationList.get(position);
        holder.binding.city.setText(destination.getCity());
        holder.binding.airport.setText(destination.getCountry());
        holder.binding.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickDestination.clickItem(destination);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (destinationList!=null){
            return destinationList.size();
        }
        return 0;
    }

    public static class DestinationViewHolder extends RecyclerView.ViewHolder {
        ItemCityBinding binding;
        public DestinationViewHolder(@NonNull ItemCityBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
    public interface clickDestination{
        public void clickItem(Destination destination);
    }
}
