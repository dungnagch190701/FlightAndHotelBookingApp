package com.example.travelapp.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.travelapp.BookingSuccessActivity;
import com.example.travelapp.ConfirmFlightActivity;
import com.example.travelapp.EnterPassengerInfo;
import com.example.travelapp.FlightPaymentActivity;
import com.example.travelapp.R;
import com.example.travelapp.databinding.FragmentConfirmFlightBinding;
import com.example.travelapp.model.FlightModel;
import com.example.travelapp.model.FlightReservation;
import com.example.travelapp.model.FlightReservationPassenger;
import com.example.travelapp.model.UserModel;
import com.example.travelapp.viewmodel.FlightsViewModel;
import com.example.travelapp.viewmodel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.stream.Collectors;


public class ConfirmFlightFragment extends Fragment {
    FragmentConfirmFlightBinding binding;
    FlightModel flightModel;
    FlightReservation flightReservation;
    FirebaseAuth firebaseAuth;
    ArrayList<FlightReservationPassenger> flightReservationPassengers;
    ArrayList<String> seatIdList;
    ArrayList<String> seatListName;
    FlightsViewModel flightsViewModel;
    Boolean isVN = false;
    int passenger;
    float price,total_price;
    String seat_class;
    UserViewModel userViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firebaseAuth = FirebaseAuth.getInstance();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        binding = FragmentConfirmFlightBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        Bundle bundle = getArguments();
        flightModel = (FlightModel) bundle.getSerializable("flightModel");
        seatListName = bundle.getStringArrayList("seat_list_name");
        passenger = bundle.getInt("passenger",0);
        price = bundle.getFloat("price",0);
        seat_class = bundle.getString("seat_class");
        total_price = passenger*price;
        flightReservation = new FlightReservation(firebaseAuth.getCurrentUser().getUid(),flightModel,new Date(),passenger,total_price);
        flightReservationPassengers = new ArrayList<>();
        seatIdList = bundle.getStringArrayList("seat");
        flightsViewModel = new ViewModelProvider(this).get(FlightsViewModel.class);
        if (flightModel.getOrigin_model().getCountry_name().equals("Vietnam") && flightModel.getDestination_model().getCountry_name().equals("Vietnam")){
            isVN = true;
        }
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel.getUserModelMutableLiveData().observe(getViewLifecycleOwner(), new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                binding.name.setText(userModel.getName());
                binding.email.setText(userModel.getEmail());
                Glide.with(requireActivity()).load(userModel.getProfileImage()).into(binding.avatar);
            }
        });
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        String formatDate = dateFormat.format(flightModel.getDeparture_time());
        String timeFrom = timeFormat.format(flightModel.getDeparture_time());
        String timeTo = timeFormat.format(flightModel.getArrival_time());
        binding.dateTime.setText(formatDate);
        binding.time.setText(timeFrom+" - "+timeTo);
        binding.origin.setText(flightModel.getOrigin_model().getAirport_code());
        binding.destination.setText(flightModel.getDestination_model().getAirport_code());
        binding.airline.setText(flightModel.getAirline());
        binding.seatCLass.setText(seat_class);
        String result = seatListName.stream()
                .collect(Collectors.joining(", "));
        binding.listSeat.setText(result);
        binding.iconAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!seatIdList.isEmpty()){
                    Intent intent = new Intent(getActivity(), EnterPassengerInfo.class);
                    intent.putExtra("seatId", seatIdList.get(0));
                    intent.putExtra("isVN",isVN);
                    startActivityForResult(intent,1000);
                } else {
                    Toast.makeText(getActivity(), "LOI", Toast.LENGTH_SHORT).show();
                }

            }
        });
        binding.payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                flightsViewModel.bookFlight(flightReservation,flightReservationPassengers);
                Intent intent = new Intent(getActivity(), FlightPaymentActivity.class);
                intent.putExtra("flightReservation",flightReservation);
                intent.putExtra("flightReservationPassengers",flightReservationPassengers);
                intent.putExtra("flightModel",flightModel);
                intent.putExtra("passenger",passenger);
                intent.putExtra("total_price",total_price);
                intent.putExtra("seat_class",seat_class);
                intent.putExtra("type",1);
                startActivity(intent);
//                requireActivity().finish();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1000){
            if (resultCode== Activity.RESULT_OK){
                seatIdList.remove(seatIdList.get(0));
                FlightReservationPassenger flightReservationPassenger = (FlightReservationPassenger) data.getSerializableExtra("passengerObj");
                flightReservationPassengers.add(flightReservationPassenger);
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View view = inflater.inflate(R.layout.my_passenger, null);
                TextView name = view.findViewById(R.id.namePassenger);
                name.setText(flightReservationPassenger.getPassenger_name());
                TextView dob = view.findViewById(R.id.dob_txt);
                Date currentDate = flightReservationPassenger.getDob();
                SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                String dateString = formatter.format(currentDate);
                dob.setText(dateString);
                TextView nation = view.findViewById(R.id.nation_txt);
                nation.setText(flightReservationPassenger.getNation());
                LinearLayout linearLayout = binding.listPassenger;
                linearLayout.addView(view);
                if (seatIdList.isEmpty()){
                    binding.iconAdd.setVisibility(View.GONE);
                    binding.add.setVisibility(View.GONE);
                }

            }
        }
    }

}