package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.travelapp.databinding.ActivityReviewBinding;
import com.example.travelapp.viewmodel.UserViewModel;

import java.util.Date;

public class ReviewActivity extends AppCompatActivity {

    ActivityReviewBinding binding;
    UserViewModel userViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        String hotel_id = getIntent().getStringExtra("hotel_id");


        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.rating.getRating()>0 && !TextUtils.isEmpty(binding.editTextReview.getText())){
                    userViewModel.reviewHotel(hotel_id,binding.rating.getRating(),new Date(),binding.editTextReview.getText().toString());
                    finish();
                } else
                {
                    Toast.makeText(ReviewActivity.this, "Check your rating", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}