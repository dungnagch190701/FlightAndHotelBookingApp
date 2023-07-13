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
import com.example.travelapp.adapter.FlightReservationAdapter;
import com.example.travelapp.databinding.FragmentMyFlightBookingBinding;
import com.example.travelapp.model.FlightModel;
import com.example.travelapp.model.FlightReservation;
import com.example.travelapp.viewmodel.FlightsViewModel;

import java.text.SimpleDateFormat;
import java.util.List;


public class MyFlightBookingFragment extends Fragment {

    FragmentMyFlightBookingBinding binding;
    FlightsViewModel flightsViewModel;
    FlightReservationAdapter flightReservationAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyFlightBookingBinding.inflate(getLayoutInflater());
        flightsViewModel = new ViewModelProvider(this).get(FlightsViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.myBookingFlightRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        flightsViewModel.getFlightReservation();
        flightsViewModel.getFlightReservationLiveData().observe(requireActivity(), new Observer<List<FlightReservation>>() {
            @Override
            public void onChanged(List<FlightReservation> flightReservations) {
                flightReservationAdapter = new FlightReservationAdapter(flightReservations);
                binding.myBookingFlightRecyclerview.setAdapter(flightReservationAdapter);
            }
        });
    }
}