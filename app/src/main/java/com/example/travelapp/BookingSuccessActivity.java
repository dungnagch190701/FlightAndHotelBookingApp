package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.travelapp.databinding.ActivityBookingSuccessBinding;
import com.example.travelapp.ui.fragment.MyHotelBookingFragment;

public class BookingSuccessActivity extends AppCompatActivity {
    ActivityBookingSuccessBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingSuccessBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Glide.with(this).load(R.drawable.check).into(binding.check);
        binding.gotoMyBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookingSuccessActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }
}