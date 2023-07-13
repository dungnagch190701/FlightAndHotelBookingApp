package com.example.travelapp.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.travelapp.R;
import com.example.travelapp.adapter.HotelReservationAdapter;
import com.example.travelapp.databinding.FragmentMyHotelBookingBinding;
import com.example.travelapp.model.HotelReservationModel;
import com.example.travelapp.viewmodel.HotelViewModel;

import java.util.List;

public class MyHotelBookingFragment extends Fragment {

    FragmentMyHotelBookingBinding binding;
    HotelViewModel hotelViewModel;
    HotelReservationAdapter hotelReservationAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        hotelViewModel = new ViewModelProvider(this).get(HotelViewModel.class);
        binding = FragmentMyHotelBookingBinding.inflate(getLayoutInflater());
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.myHotelBookingRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        hotelViewModel.getBookingHotel();
        hotelViewModel.getHotelReservationLiveData().observe(requireActivity(), new Observer<List<HotelReservationModel>>() {
            @Override
            public void onChanged(List<HotelReservationModel> hotelReservationModels) {
                hotelReservationAdapter = new HotelReservationAdapter(hotelReservationModels);
                binding.myHotelBookingRecyclerview.setAdapter(hotelReservationAdapter);
            }
        });

    }
}