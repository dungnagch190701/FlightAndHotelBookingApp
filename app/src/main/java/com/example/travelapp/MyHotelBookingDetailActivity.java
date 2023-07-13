package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.travelapp.adapter.PhotoAdapter;
import com.example.travelapp.databinding.ActivityMyHotelBookingDetailBinding;
import com.example.travelapp.model.HotelReservationModel;
import com.example.travelapp.viewmodel.UserViewModel;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class MyHotelBookingDetailActivity extends AppCompatActivity {

    ActivityMyHotelBookingDetailBinding binding;
    PhotoAdapter photoAdapter;
    UserViewModel userViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyHotelBookingDetailBinding.inflate(getLayoutInflater());
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        setContentView(binding.getRoot());
        HotelReservationModel hotelReservationModel = (HotelReservationModel) getIntent().getSerializableExtra("hotelReservationModel");
        binding.tag.setText(hotelReservationModel.getHotelModel().getTag());
        binding.hotelName.setText(hotelReservationModel.getHotelModel().getName());
        binding.addressText.setText(hotelReservationModel.getHotelModel().getAddress()+", "+hotelReservationModel.getHotelModel().getLocationModel().getCity()+", "+hotelReservationModel.getHotelModel().getLocationModel().getCountry());
        float review = hotelReservationModel.getHotelModel().getReview_avg();
        binding.reviewAvg.setText(String.valueOf(review));
        binding.rating.setRating(hotelReservationModel.getHotelModel().getRating());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        binding.checkInDateText.setText(dateFormat.format(hotelReservationModel.getCheck_in_date()));
        binding.checkOutDateText.setText(dateFormat.format(hotelReservationModel.getCheck_out_date()));
        binding.roomTypeText.setText(hotelReservationModel.getRoomTypeModel().getName());
        binding.totalPassengerText.setText(hotelReservationModel.getTotal_passenger() + " guest(s)");
        binding.totalAmountText.setText("$"+hotelReservationModel.getTotal_amount());
        if (hotelReservationModel.getPayment() == 2){
            binding.paymentIcon.setImageResource(R.drawable.ic_baseline_payments_24);
        }
        else{
            binding.paymentIcon.setImageResource(R.drawable.zalo);
        }
        photoAdapter = new PhotoAdapter(this,hotelReservationModel.getHotelModel().getPhotoURL());
        binding.image.setAdapter(photoAdapter);
        binding.indicator.setViewPager(binding.image);
        photoAdapter.registerDataSetObserver(binding.indicator.getDataSetObserver());
        userViewModel.isReview(hotelReservationModel.getHotelModel().getHotel_id());
        userViewModel.getIsReviewLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    binding.reviewBtn.setText("You have already rated");
                    binding.reviewBtn.setEnabled(false);
                }
            }
        });
        binding.reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),ReviewActivity.class);
                intent.putExtra("hotel_id",hotelReservationModel.getHotelModel().getHotel_id());
                startActivity(intent);
            }
        });

    }

}