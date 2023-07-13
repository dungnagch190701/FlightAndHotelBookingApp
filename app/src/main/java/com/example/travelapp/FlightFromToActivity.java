package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.travelapp.adapter.CityAdapter;
import com.example.travelapp.databinding.ActivityFlightFromToAcitivityBinding;
import com.example.travelapp.model.CityModel;

import com.example.travelapp.myinterface.IClickItemCity;
import com.example.travelapp.viewmodel.FlightsViewModel;

import java.util.List;

public class FlightFromToActivity extends AppCompatActivity {
    ActivityFlightFromToAcitivityBinding binding;
    FlightsViewModel flightsViewModel;
    CityAdapter cityAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFlightFromToAcitivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.loading.setVisibility(View.VISIBLE);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        flightsViewModel = new ViewModelProvider(this , (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())).get(FlightsViewModel.class);
        flightsViewModel.getCitiesLiveData().observe(this, new Observer<List<CityModel>>() {
            @Override
            public void onChanged(List<CityModel> cityModels) {
                cityAdapter = new CityAdapter(cityModels, new IClickItemCity() {
                    @Override
                    public void OnClickItemCity(CityModel cityModel) {
                        Intent intent = new Intent();
                        intent.putExtra("cityId",cityModel.getCity_id());
                        intent.putExtra("cityName", cityModel.getCity_name());
                        intent.putExtra("countryName", cityModel.getCountry_name());
                        intent.putExtra("airportCode", cityModel.getAirport_code());
                        setResult(Activity.RESULT_OK, intent); // Đặt kết quả trả về là RESULT_OK và đính kèm Intent
                        finish(); // Kết thúc Activity
                    }
                });
                binding.recyclerview.setAdapter(cityAdapter);
                binding.loading.setVisibility(View.GONE);
            }
        });





    }
}