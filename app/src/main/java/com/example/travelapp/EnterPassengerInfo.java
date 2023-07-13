package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.travelapp.databinding.ActivityEnterPassengerInfoBinding;
import com.example.travelapp.model.FlightReservation;
import com.example.travelapp.model.FlightReservationPassenger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EnterPassengerInfo extends AppCompatActivity {
    ActivityEnterPassengerInfoBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEnterPassengerInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        String seat_id = getIntent().getStringExtra("seatId");
        Boolean isVN = getIntent().getBooleanExtra("isVN",false);
        if (isVN){
            binding.passport.setVisibility(View.GONE);
        }

        setContentView(view);
        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.name.getText().toString();
                String dob = binding.dob.getText().toString();
                String pattern = "dd-MM-yyyy"; // Mẫu định dạng ngày tương ứng với chuỗi

                DateFormat dateFormat = new SimpleDateFormat(pattern);
                Date date = null;

                try {
                    date = dateFormat.parse(dob); // Chuyển đổi chuỗi thành đối tượng Date
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String nation = binding.nation.getText().toString();
                String passport = binding.passport.getText().toString();
                FlightReservationPassenger flightReservationPassenger = new FlightReservationPassenger(seat_id,name,date,nation,passport);
                Intent intent = new Intent();
                intent.putExtra("passengerObj",flightReservationPassenger);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }
}