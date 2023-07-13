package com.example.travelapp.ui.fragment;

import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.example.travelapp.FlightActivity;
import com.example.travelapp.HotelActivity;
import com.example.travelapp.MainActivity;
import com.example.travelapp.R;
import com.example.travelapp.adapter.ActivitiesAdapter;
import com.example.travelapp.databinding.FragmentHomeBinding;
import com.example.travelapp.databinding.FragmentSettingsBinding;
import com.example.travelapp.model.ActivitiesModel;
import com.example.travelapp.model.HotelModel;
import com.example.travelapp.model.UserModel;
import com.example.travelapp.ui.auth.SignInActivity;
import com.example.travelapp.viewmodel.ActivitiesViewModel;
import com.example.travelapp.viewmodel.AuthViewModel;

import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {
    ActivitiesViewModel viewModel;
    FragmentHomeBinding binding;
    ActivitiesAdapter activitiesAdapter,activitiesAdapter2;
    AuthViewModel authViewModel;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this , (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(ActivitiesViewModel.class);
        authViewModel = new ViewModelProvider(this , (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(AuthViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
        binding.popularActivities.setLayoutManager(layoutManager);
        binding.popularActivities2.setLayoutManager(layoutManager2);
        authViewModel.getUserDataFromFirestore();
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        authViewModel.getUserFStore().observe(requireActivity(), new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                Glide.with(requireContext()).load(userModel.getProfileImage()).error(R.drawable.no_avatar).into(binding.avatarHome);
                binding.hi.setText("Hi, "+userModel.getName());
            }
        });
        binding.flight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mActivity = new Intent(getActivity(), FlightActivity.class);
                startActivity(mActivity);

            }
        });
        binding.hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mActivity = new Intent(getActivity(), HotelActivity.class);
                startActivity(mActivity);
            }
        });
//        List<ActivitiesModel> activitiesModels = viewModel.getActivitiesModelMutableLiveData().getValue();
//        activitiesAdapter = new ActivitiesAdapter(activitiesModels);
        authViewModel.getHotelHighRating();
        authViewModel.getHotelHighReview();


//        viewModel.getActivitiesModelMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<ActivitiesModel>>() {
//            @Override
//            public void onChanged(List<ActivitiesModel> activitiesModels) {
//                activitiesAdapter = new ActivitiesAdapter(activitiesModels);
//                binding.popularActivities.setAdapter(activitiesAdapter);
//                binding.popularActivities2.setAdapter(activitiesAdapter);
//            }
//        });
        authViewModel.getHighRatingHotel().observe(requireActivity(), new Observer<List<HotelModel>>() {
            @Override
            public void onChanged(List<HotelModel> hotelModels) {
                binding.processBar.setVisibility(View.GONE);
                binding.popular.setVisibility(View.VISIBLE);
                activitiesAdapter = new ActivitiesAdapter(hotelModels);
                binding.popularActivities.setAdapter(activitiesAdapter);
            }
        });
        authViewModel.getHighReviewHotel().observe(requireActivity(), new Observer<List<HotelModel>>() {
            @Override
            public void onChanged(List<HotelModel> hotelModels) {
                binding.popular2.setVisibility(View.VISIBLE);
                hotelModels.sort(new Comparator<HotelModel>() {
                    @Override
                    public int compare(HotelModel hotelModel, HotelModel t1) {
                        return Float.compare(t1.getReview_avg(), hotelModel.getReview_avg());
                    }
                });
                activitiesAdapter2 = new ActivitiesAdapter(hotelModels);
                binding.popularActivities2.setAdapter(activitiesAdapter2);
            }
        });


    }
}