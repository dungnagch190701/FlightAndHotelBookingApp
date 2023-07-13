package com.example.travelapp.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.travelapp.R;
import com.example.travelapp.adapter.PhotoAdapter;
import com.example.travelapp.adapter.SavedHotelAdapter;
import com.example.travelapp.databinding.FragmentFavoriteBinding;
import com.example.travelapp.model.HotelModel;
import com.example.travelapp.viewmodel.HotelViewModel;

import java.util.List;


public class FavoriteFragment extends Fragment {

    FragmentFavoriteBinding binding;
    SavedHotelAdapter savedHotelAdapter;
    HotelViewModel hotelViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        hotelViewModel = new ViewModelProvider(this).get(HotelViewModel.class);
        binding = FragmentFavoriteBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.favoritesHotel.setLayoutManager(new LinearLayoutManager(requireActivity()));
        hotelViewModel.getSavedHotel();
        hotelViewModel.getSavedHotelLiveData().observe(requireActivity(), new Observer<List<HotelModel>>() {
            @Override
            public void onChanged(List<HotelModel> hotelModels) {
                savedHotelAdapter = new SavedHotelAdapter(hotelModels);
                binding.favoritesHotel.setAdapter(savedHotelAdapter);
            }
        });
    }
}