package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.travelapp.databinding.ActivityMyFlightBookingDetailBinding;
import com.example.travelapp.databinding.MyPassengerBinding;
import com.example.travelapp.databinding.MyPassengerBookingDetailBinding;
import com.example.travelapp.model.FlightReservation;
import com.example.travelapp.model.FlightReservationPassenger;
import com.example.travelapp.viewmodel.UserViewModel;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

public class MyFlightBookingDetailActivity extends AppCompatActivity {

    ActivityMyFlightBookingDetailBinding binding;
    UserViewModel userViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyFlightBookingDetailBinding.inflate(getLayoutInflater());
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        setContentView(binding.getRoot());
        FlightReservation flightReservation = (FlightReservation) getIntent().getSerializableExtra("flightReservation");
        Glide.with(this).load(flightReservation.getFlight().getLogo()).into(binding.logo);
        binding.airline.setText(flightReservation.getFlight().getAirline());
        if (flightReservation.getPayment() == 2){
            binding.paymentIcon.setImageResource(R.drawable.ic_baseline_payments_24);
        }
        else{
            binding.paymentIcon.setImageResource(R.drawable.zalo);
        }
        binding.airportCodeFrom.setText(flightReservation.getFlight().getOrigin_model().getAirport_code());
        binding.airportCodeTo.setText(flightReservation.getFlight().getDestination_model().getAirport_code());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        binding.departureDateText.setText(dateFormat.format(flightReservation.getFlight().getDeparture_time()));
        binding.arrivalDateText.setText(dateFormat.format(flightReservation.getFlight().getArrival_time()));
        binding.totalPassengerText.setText(String.valueOf(flightReservation.getTotal_passenger()));
        binding.cityNameFrom.setText(flightReservation.getFlight().getOrigin_model().getCity_name()+", "+flightReservation.getFlight().getOrigin_model().getCountry_name());
        binding.cityNameTo.setText(flightReservation.getFlight().getDestination_model().getCity_name()+", "+flightReservation.getFlight().getDestination_model().getCountry_name());
        binding.totalAmountText.setText("$"+flightReservation.getTotal_amount());


        userViewModel.getFlightPassenger(flightReservation.getReservation_id());
        userViewModel.getFlightReservationPassengerLiveData().observe(this, new Observer<List<FlightReservationPassenger>>() {
            @Override
            public void onChanged(List<FlightReservationPassenger> flightReservationPassengers) {
                for (FlightReservationPassenger list : flightReservationPassengers){
                    MyPassengerBookingDetailBinding myPassengerBinding = MyPassengerBookingDetailBinding.inflate(getLayoutInflater());
                    myPassengerBinding.namePassenger.setText(list.getPassenger_name());
                    myPassengerBinding.dobTxt.setText("09/12/2001");
                    myPassengerBinding.namePassenger.setText(list.getPassenger_name());
                    myPassengerBinding.nationTxt.setText(list.getNation());
                    myPassengerBinding.seatNumberText.setText(list.getSeatModel().getSeat_number());
                    binding.passengerInfoLayout.addView(myPassengerBinding.getRoot());
                }
            }
        });



    }
}