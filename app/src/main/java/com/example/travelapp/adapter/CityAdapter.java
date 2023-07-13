package com.example.travelapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.databinding.ItemCityBinding;
import com.example.travelapp.databinding.ItemPopularBinding;
import com.example.travelapp.model.ActivitiesModel;
import com.example.travelapp.model.CityModel;
import com.example.travelapp.myinterface.IClickItemCity;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder>{
    private List<CityModel> cityModels;
    private IClickItemCity iClickItemCity;

    public CityAdapter(List<CityModel> cityModels,IClickItemCity listener) {
        this.cityModels = cityModels;
        this.iClickItemCity = listener;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCityBinding itemCityBinding = ItemCityBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CityViewHolder(itemCityBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        CityModel cityModel = cityModels.get(position);
        if (cityModel==null){
            return;
        }
        holder.itemCityBinding.city.setText(cityModel.getCity_name()+", " + cityModel.getCountry_name() );
        holder.itemCityBinding.airport.setText(cityModel.getAirport_name());
        holder.itemCityBinding.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemCity.OnClickItemCity(cityModel);
            }
        });


    }

    @Override
    public int getItemCount() {
        if (cityModels!= null){
            return cityModels.size();
        }
        return 0;
    }

    public static class CityViewHolder extends RecyclerView.ViewHolder {
        ItemCityBinding itemCityBinding;
        public CityViewHolder(@NonNull ItemCityBinding itemView) {
            super(itemView.getRoot());
            this.itemCityBinding = itemView;
        }
    }
}
