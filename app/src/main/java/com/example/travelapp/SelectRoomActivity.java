package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.travelapp.adapter.RoomTypeAdapter;
import com.example.travelapp.databinding.ActivitySelectRoomBinding;
import com.example.travelapp.model.HotelModel;
import com.example.travelapp.model.RoomTypeModel;
import com.example.travelapp.viewmodel.HotelViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SelectRoomActivity extends AppCompatActivity {
    ActivitySelectRoomBinding binding;
    String in_string,out_string;
    Date check_in_date,check_out_date;
    int room,passenger;
    HotelViewModel hotelViewModel;
    RoomTypeAdapter roomTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hotelViewModel = new ViewModelProvider(this).get(HotelViewModel.class);
        binding = ActivitySelectRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        HotelModel hotelModel = (HotelModel) getIntent().getSerializableExtra("hotelModel");
        in_string = getIntent().getStringExtra("check_in_date");
        out_string = getIntent().getStringExtra("check_out_date");
        room = getIntent().getIntExtra("room",0);
        passenger = getIntent().getIntExtra("passenger",0);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            check_in_date = formatter.parse(in_string);
            check_out_date = formatter.parse(out_string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        hotelViewModel.getRoomType(hotelModel.getHotel_id(),check_in_date,check_out_date,room,passenger);
        binding.typeList.setLayoutManager(new LinearLayoutManager(this));
        hotelViewModel.getRoomTypeModelLiveData().observe(this, new Observer<List<RoomTypeModel>>() {
            @Override
            public void onChanged(List<RoomTypeModel> roomTypeModels) {
                roomTypeAdapter = new RoomTypeAdapter(roomTypeModels,getApplicationContext(),hotelModel,check_in_date,check_out_date,room,passenger);
                binding.typeList.setAdapter(roomTypeAdapter);
            }
        });


    }
}