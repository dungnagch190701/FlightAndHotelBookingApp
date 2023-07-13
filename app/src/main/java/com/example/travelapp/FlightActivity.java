package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.res.ColorStateList;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.example.travelapp.adapter.RecentFlightAdapter;
import com.example.travelapp.databinding.ActivityFlightBinding;
import com.example.travelapp.databinding.ActivityMainBinding;
import com.example.travelapp.model.SearchRecentModel;
import com.example.travelapp.ui.fragment.MultiCityFragment;
import com.example.travelapp.ui.fragment.OneWayFlightFragment;
import com.example.travelapp.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class FlightActivity extends AppCompatActivity {
    ActivityFlightBinding binding;
    RecentFlightAdapter recentFlightAdapter;
    UserViewModel userViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFlightBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getRecentSearchFlight();
//        binding.oneway.setSelected(true);
        replaceFragment(new OneWayFlightFragment());
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
//        binding.oneway.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                binding.oneway.setSelected(true);
//                OneWayFlightFragment oneWayFlightFragment = new OneWayFlightFragment();
//                replaceFragment(oneWayFlightFragment);
//            }
//        });

        binding.recentTxt.setVisibility(View.GONE);
        binding.clearAll.setVisibility(View.GONE);
        binding.clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userViewModel.clearAllRecentSearch();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        binding.recentSearch.setLayoutManager(layoutManager);
        userViewModel.getSearchRecentMutableLiveData().observe(this, new Observer<List<SearchRecentModel>>() {
            @Override
            public void onChanged(List<SearchRecentModel> searchRecentModels) {
                if (searchRecentModels == null || searchRecentModels.isEmpty()) {
                    binding.clearAll.setVisibility(View.GONE);
                    binding.recentTxt.setVisibility(View.GONE);
                }
                else{
                    binding.recentTxt.setVisibility(View.VISIBLE);
                    binding.clearAll.setVisibility(View.VISIBLE);
                }
                    recentFlightAdapter = new RecentFlightAdapter(searchRecentModels);
                    binding.recentSearch.setAdapter(recentFlightAdapter);


            }
        });







    }
    public  void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer,fragment);
        fragmentTransaction.commit();
    }
}