package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.travelapp.databinding.ActivityFlightPaymentBinding;
import com.example.travelapp.model.CreateOrder;
import com.example.travelapp.model.FlightModel;
import com.example.travelapp.model.FlightReservation;
import com.example.travelapp.model.FlightReservationPassenger;
import com.example.travelapp.model.HotelModel;
import com.example.travelapp.model.RoomTypeModel;
import com.example.travelapp.viewmodel.FlightsViewModel;
import com.example.travelapp.viewmodel.HotelViewModel;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class FlightPaymentActivity extends AppCompatActivity {

    float exchangeRate = 23000.0f;
    float dongAmount;
    float total_price;
    int selected;
    String myString,seat_class;
    ActivityFlightPaymentBinding binding;
    Date check_in, check_out;
    int room,passenger;
    int total_room_price;
    int total_night_price;
    int total_hotel_price;
    HotelViewModel hotelViewModel;
    FirebaseAuth firebaseAuth;
    HotelModel hotelModel;
    RoomTypeModel roomTypeModel;
    FlightsViewModel flightsViewModel;
    FlightReservation flightReservation;
    ArrayList<FlightReservationPassenger> flightReservationPassengers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFlightPaymentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseAuth = FirebaseAuth.getInstance();
        hotelViewModel = new ViewModelProvider(this).get(HotelViewModel.class);
        flightsViewModel = new ViewModelProvider(this , (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())).get(FlightsViewModel.class);
        int type = getIntent().getIntExtra("type",0);
        if (type==1){
            binding.cardType.setVisibility(View.VISIBLE);
            binding.cardHotel.setVisibility(View.GONE);
            binding.item2.setVisibility(View.GONE);
            binding.priceItem2.setVisibility(View.GONE);
            FlightModel flightModel = (FlightModel) getIntent().getSerializableExtra("flightModel");
            flightReservation = (FlightReservation) getIntent().getSerializableExtra("flightReservation");

            flightReservationPassengers = (ArrayList<FlightReservationPassenger>) getIntent().getSerializableExtra("flightReservationPassengers");
            int passenger = getIntent().getIntExtra("passenger",0);
            total_price = getIntent().getFloatExtra("total_price",0);
            seat_class = getIntent().getStringExtra("seat_class");
            binding.item1.setText(flightModel.getAirline()+" "+seat_class+" x "+passenger);
            binding.priceItem1.setText("$ "+ (int) total_price);
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            String formattedDate = dateFormat.format(flightModel.getDeparture_time());
            binding.direction.setText(flightModel.getOrigin_model().getAirport_code());
            binding.direction2.setText(flightModel.getDestination_model().getAirport_code());
            binding.date.setText(formattedDate);
            binding.passenger.setText(passenger+" passenger(s)");
            binding.price.setText("$ "+(int) total_price);
            dongAmount = total_price * exchangeRate;
            myString = String.format("%.0f", dongAmount);
        }
        else{

            binding.cardType.setVisibility(View.GONE);
            binding.cardHotel.setVisibility(View.VISIBLE);
            roomTypeModel = (RoomTypeModel) getIntent().getSerializableExtra("roomTypeModel");
            hotelModel = (HotelModel) getIntent().getSerializableExtra("hotelModel");
            String in_string = getIntent().getStringExtra("check_in_date");
            String out_string = getIntent().getStringExtra("check_out_date");
            room = getIntent().getIntExtra("room",0);
            passenger = getIntent().getIntExtra("passenger",passenger);
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy");
            try {
                check_in = formatter.parse(in_string);
                check_out = formatter.parse(out_string);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long difference = check_out.getTime() - check_in.getTime();
            float daysBetween = (difference / (1000*60*60*24));
            int days = (int) daysBetween;
            binding.hotelName.setText(hotelModel.getName());
            binding.checkInDate.setText(in_string);
            binding.checkOutDate.setText(out_string);
            binding.roomType.setText(roomTypeModel.getName());
            binding.guestPerRoom.setText(String.valueOf(roomTypeModel.getGuest_per_room())+ "guest(s)/room");
            binding.item2.setVisibility(View.VISIBLE);
            binding.priceItem2.setVisibility(View.VISIBLE);
            total_room_price = roomTypeModel.getPrice_per_night()*room;
            binding.item1.setText(roomTypeModel.getName() + " x "+room);
            binding.priceItem1.setText("$ "+total_room_price);
            binding.item2.setText(days + " night(s)");
            total_night_price = roomTypeModel.getPrice_per_night()*days;
            binding.priceItem2.setText("$ "+total_night_price);
            if (days==room){
                total_hotel_price = roomTypeModel.getPrice_per_night();
            }
            else{
                total_hotel_price = (roomTypeModel.getPrice_per_night()*room)+(roomTypeModel.getPrice_per_night()*days);
            }

            binding.price.setText("$ "+total_hotel_price);
            dongAmount = total_hotel_price * exchangeRate;
            myString = String.format("%.0f", dongAmount);



        }




        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(553, Environment.SANDBOX);



        binding.btnZalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type==1){
                    if (selected == 1){
                        CreateOrder orderApi = new CreateOrder();
                        try {
                            JSONObject data = orderApi.createOrder(myString);
                            String code = data.getString("returncode");

                            if (code.equals("1")) {
                                String token = data.getString("zptranstoken");
                                ZaloPaySDK.getInstance().payOrder(FlightPaymentActivity.this, token, "demozpdk://app", new PayOrderListener() {
                                    @Override
                                    public void onPaymentSucceeded(String s, String s1, String s2) {
                                        flightsViewModel.bookFlight(flightReservation,flightReservationPassengers,seat_class,1);
                                        Intent intent = new Intent(getApplicationContext(), BookingSuccessActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onPaymentCanceled(String s, String s1) {
                                        Toast.makeText(getApplicationContext(), "You need to finish payment", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {

                                    }
                                });

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        flightsViewModel.bookFlight(flightReservation,flightReservationPassengers,seat_class,2);
                        Intent intent = new Intent(getApplicationContext(), BookingSuccessActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                else{
                    if (selected == 1){
                        CreateOrder orderApi = new CreateOrder();
                        try {
                            JSONObject data = orderApi.createOrder(myString);
                            String code = data.getString("returncode");

                            if (code.equals("1")) {
                                String token = data.getString("zptranstoken");
                                ZaloPaySDK.getInstance().payOrder(FlightPaymentActivity.this, token, "demozpdk://app", new PayOrderListener() {
                                    @Override
                                    public void onPaymentSucceeded(String s, String s1, String s2) {
                                        hotelViewModel.bookHotel(firebaseAuth.getUid(),hotelModel.getHotel_id(),roomTypeModel.getRoom_type_id(),check_in,check_out,passenger,total_hotel_price,1);
                                        Intent intent = new Intent(getApplicationContext(), BookingSuccessActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onPaymentCanceled(String s, String s1) {
                                        Toast.makeText(getApplicationContext(), "You need to finish payment", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {

                                    }
                                });

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        Intent intent = new Intent(getApplicationContext(), BookingSuccessActivity.class);
                        hotelViewModel.bookHotel(firebaseAuth.getUid(),hotelModel.getHotel_id(),roomTypeModel.getRoom_type_id(),check_in,check_out,passenger,total_hotel_price,2);
                        startActivity(intent);
                        finish();
                    }

                }


            }
        });
        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.zalopay) {
                    binding.zalopay.setSelected(true);
                    binding.cash.setSelected(false);
                    binding.zalopay.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.zalo, 0, R.drawable.ic_baseline_check_24, 0);
                    binding.cash.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_payments_24, 0, 0, 0);
                    selected = 1;
                } else if (checkedId == R.id.cash) {
                    binding.cash.setSelected(true);
                    binding.zalopay.setSelected(false);
                    binding.zalopay.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.zalo, 0, 0, 0);
                    binding.cash.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_payments_24, 0, R.drawable.ic_baseline_check_24, 0);
                    selected = 2;
                }
            }
        });
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }


}