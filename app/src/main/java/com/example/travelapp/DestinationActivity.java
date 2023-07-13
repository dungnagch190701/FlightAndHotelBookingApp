package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.travelapp.adapter.DestinationAdapter;
import com.example.travelapp.databinding.ActivityDestinationBinding;
import com.example.travelapp.model.Destination;
import com.example.travelapp.viewmodel.HotelViewModel;

import java.util.List;

public class DestinationActivity extends AppCompatActivity {

    ActivityDestinationBinding binding;
    HotelViewModel hotelViewModel;
    DestinationAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDestinationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        hotelViewModel = new ViewModelProvider(this).get(HotelViewModel.class);
        hotelViewModel.getDestination();
        hotelViewModel.getDestinationLiveData().observe(this, new Observer<List<Destination>>() {
            @Override
            public void onChanged(List<Destination> destinations) {
                adapter = new DestinationAdapter(destinations, new DestinationAdapter.clickDestination() {
                    @Override
                    public void clickItem(Destination destination) {
                        Intent intent = new Intent();
                        intent.putExtra("destination",destination);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });
                binding.recyclerview.setAdapter(adapter);
                binding.loading.setVisibility(View.GONE);
            }
        });

    }
}