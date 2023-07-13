package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.travelapp.databinding.ActivityConfirmFlightAcitivtyBinding;
import com.example.travelapp.model.FlightModel;
import com.example.travelapp.model.SeatModel;
import com.example.travelapp.ui.fragment.ConfirmFlightFragment;

import java.util.ArrayList;
import java.util.List;

public class ConfirmFlightActivity extends AppCompatActivity {
    ActivityConfirmFlightAcitivtyBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmFlightAcitivtyBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Intent intent = getIntent();
        FlightModel flightModel = (FlightModel) intent.getSerializableExtra("flight");
        int passenger = intent.getIntExtra("passenger",0);
        float price = intent.getFloatExtra("price",0);
        ArrayList<String> seatId = intent.getStringArrayListExtra("seat");
        String seat_class = intent.getStringExtra("seat_class");
        ArrayList<String> seat_list_name = intent.getStringArrayListExtra("seat_list_name");
        Bundle bundle = new Bundle();
        bundle.putSerializable("flightModel", flightModel);
        bundle.putInt("passenger",passenger);
        bundle.putFloat("price",price);
        bundle.putStringArrayList("seat",seatId);
        bundle.putString("seat_class",seat_class);
        bundle.putStringArrayList("seat_list_name",seat_list_name);
        ConfirmFlightFragment confirmFlightFragment = new ConfirmFlightFragment();
        confirmFlightFragment.setArguments(bundle);
        replaceFragment(confirmFlightFragment);

    }
    public  void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentConfirm,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}