package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.travelapp.adapter.FacilitiesAdapter;
import com.example.travelapp.adapter.PhotoAdapter;
import com.example.travelapp.adapter.ReviewHotelAdapter;
import com.example.travelapp.databinding.ActivityHotelDetailBinding;
import com.example.travelapp.databinding.BotSheetAllReviewBinding;
import com.example.travelapp.model.FacilitiesModel;
import com.example.travelapp.model.HotelModel;
import com.example.travelapp.model.ReviewHotelModel;
import com.example.travelapp.ui.fragment.BotSheetAllFacilities;
import com.example.travelapp.ui.fragment.BotSheetAllReview;
import com.example.travelapp.viewmodel.HotelViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HotelDetailActivity extends AppCompatActivity {

    ActivityHotelDetailBinding binding;
    PhotoAdapter photoAdapter;
    HotelViewModel hotelViewModel;
    ReviewHotelAdapter reviewHotelAdapter;
    FacilitiesAdapter facilitiesAdapter;
    private BottomSheetBehavior behavior;
    String in_string,out_string;
    Date check_in_date,check_out_date;
    int room,passenger;
    boolean isSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hotelViewModel = new ViewModelProvider(this).get(HotelViewModel.class);
        binding = ActivityHotelDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        HotelModel hotelModel = (HotelModel) getIntent().getSerializableExtra("hotelModel");
        in_string = getIntent().getStringExtra("check_in_date");
        out_string = getIntent().getStringExtra("check_out_date");
        room = getIntent().getIntExtra("room",1);
        passenger = getIntent().getIntExtra("passenger",1);

        hotelViewModel.addViewedHotel(hotelModel);
        hotelViewModel.setIsSaved(hotelModel.getHotel_id());
        hotelViewModel.getIsSaved().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    isSaved = true;
                    binding.bookmark.setImageResource(R.drawable.ic_baseline_bookmark_24);
                }
                else{
                    isSaved = false;
                    binding.bookmark.setImageResource(R.drawable.ic_outline_bookmark_border_24);
                }
            }
        });
        hotelViewModel.getRecentReview(hotelModel.getHotel_id());
        hotelViewModel.getFacilities(hotelModel.getHotel_id());
        binding.comment.setLayoutManager(new LinearLayoutManager(this));
        hotelViewModel.getReviewRecentHotelModelLiveData().observe(this, new Observer<List<ReviewHotelModel>>() {
            @Override
            public void onChanged(List<ReviewHotelModel> reviewHotelModels) {
                reviewHotelAdapter = new ReviewHotelAdapter(reviewHotelModels,false);
                binding.comment.setAdapter(reviewHotelAdapter);
            }
        });
        photoAdapter = new PhotoAdapter(this,hotelModel.getPhotoURL());
        binding.image.setAdapter(photoAdapter);
        binding.indicator.setViewPager(binding.image);
        photoAdapter.registerDataSetObserver(binding.indicator.getDataSetObserver());
        binding.hotelName.setText(hotelModel.getName());
        binding.rating.setRating(hotelModel.getRating());
        binding.address.setText(hotelModel.getAddress());
        binding.location.setText(hotelModel.getLocationModel().getCity()+", "+hotelModel.getLocationModel().getCountry());
        float review_avg = hotelModel.getReview_avg();
        binding.reviewAvgNumber.setText(String.format(Locale.US,"%.1f",review_avg));
        binding.tag.setText(hotelModel.getTag());
        binding.listFac.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        hotelViewModel.getFacilitiesModelLiveData().observe(this, new Observer<List<FacilitiesModel>>() {
            @Override
            public void onChanged(List<FacilitiesModel> facilitiesModels) {
                facilitiesAdapter = new FacilitiesAdapter(facilitiesModels,false);
                binding.listFac.setAdapter(facilitiesAdapter);
            }
        });
        binding.textSeeAllFac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BotSheetAllFacilities botSheetAllFacilities = new BotSheetAllFacilities();
                botSheetAllFacilities.show(getSupportFragmentManager(),botSheetAllFacilities.getTag());

            }
        });
        binding.seeAllReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BotSheetAllReview botSheetAllReview = new BotSheetAllReview();
                botSheetAllReview.show(getSupportFragmentManager(),botSheetAllReview.getTag());
            }
        });
        TextView textView = binding.getRoot().findViewById(R.id.price_hotel);
        textView.setText("$ "+hotelModel.getPrice_start_from());
        Button button = binding.getRoot().findViewById(R.id.select_room);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SelectRoomActivity.class);
                intent.putExtra("hotelModel",hotelModel);
                intent.putExtra("check_in_date",in_string);
                intent.putExtra("check_out_date",out_string);
                intent.putExtra("room",room);
                intent.putExtra("passenger",passenger);
                startActivity(intent);
            }
        });

        binding.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSaved){
                    hotelViewModel.unSaveHotel(hotelModel.getHotel_id());
                }else{
                    hotelViewModel.saveHotel(hotelModel);
                }

            }
        });




    }

}