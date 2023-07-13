package com.example.travelapp.ui.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.travelapp.FlightFromToActivity;
import com.example.travelapp.HotelActivity;
import com.example.travelapp.MainActivity;
import com.example.travelapp.R;
import com.example.travelapp.SearchFlightResultActivity;
import com.example.travelapp.databinding.ActivityFlightBinding;
import com.example.travelapp.databinding.BotSheetSeatBinding;
import com.example.travelapp.databinding.FragmentOneWayFlightBinding;
import com.example.travelapp.ui.auth.SignInActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class OneWayFlightFragment extends Fragment implements BotSheetPassenger.OnPassengerCountSelectedListener {
    private static final int REQUEST_CODE_FROM = 100;
    private static final int REQUEST_CODE_TO = 200;
    FragmentOneWayFlightBinding binding;
    Long longValue,endLong;
    String from_id,to_id,city_name_from,city_name_to;
    int passenger_number = 1;
    String seat_class = "Economy";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOneWayFlightBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final OneWayFlightFragment fragment = this;

        binding.fromEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mActivity = new Intent(getActivity(), FlightFromToActivity.class);
                startActivityForResult(mActivity,REQUEST_CODE_FROM);
            }
        });
        binding.toEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mActivity = new Intent(getActivity(), FlightFromToActivity.class);
                startActivityForResult(mActivity,REQUEST_CODE_TO);
            }
        });
        binding.departureDateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        binding.passengerEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gọi bottom sheet từ Activity hoặc Fragment
                BotSheetPassenger bottomSheet = BotSheetPassenger.newInstance();
                bottomSheet.setTargetFragment(fragment, 0); // Đăng ký Fragment gọi bottom sheet là Fragment gửi kết quả về
                bottomSheet.show(getFragmentManager(), "passenger_bottom_sheet");



            }
        });
        binding.seatClassEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBotSheetSeat();
            }
        });
        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateEditTexts(binding.fromEdt,binding.toEdt,binding.departureDateEdt)){
                    Intent mActivity = new Intent(getActivity(), SearchFlightResultActivity.class);
                    mActivity.putExtra("from_id",from_id);
                    mActivity.putExtra("to_id",to_id);
                    mActivity.putExtra("passenger",passenger_number);
                    mActivity.putExtra("seat_class",seat_class);
                    mActivity.putExtra("departure_date",longValue);
                    mActivity.putExtra("city_name_from",city_name_from);
                    mActivity.putExtra("city_name_to",city_name_to);
                    mActivity.putExtra("end_date",endLong);
                    startActivity(mActivity);
                }
                else{
                    Toast.makeText(requireContext(), "Please enter valid information", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void openBotSheetSeat() {
        BotSheetSeatBinding bindingSeat;
        bindingSeat = BotSheetSeatBinding.inflate(getLayoutInflater());
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(bindingSeat.getRoot());
        bottomSheetDialog.show();
        bindingSeat.radioGroup.check(bindingSeat.radioButtonEconomy.getId());
        bindingSeat.buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedRadioButtonId = bindingSeat.radioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = bindingSeat.getRoot().findViewById(selectedRadioButtonId);
                String selectedOption = selectedRadioButton.getText().toString();
                binding.seatClassEdt.setText(selectedOption);
                seat_class = selectedOption;
                bottomSheetDialog.dismiss();
            }
        });


    }
    private boolean validateEditTexts(EditText... editTexts) {
        for (EditText editText : editTexts) {
            if (TextUtils.isEmpty(editText.getText())) {
                // EditText không có dữ liệu
                return false;
            }
        }
        // Tất cả các EditText đều có dữ liệu
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FROM){
            if (resultCode == Activity.RESULT_OK) {
                binding.fromEdt.setText(data.getStringExtra("cityName")+", "+data.getStringExtra("countryName")+" ("+data.getStringExtra("airportCode")+")");
                from_id = data.getStringExtra("cityId");
                city_name_from = data.getStringExtra("cityName");
            }

        }
        if (requestCode == REQUEST_CODE_TO){
            if (resultCode == Activity.RESULT_OK) {
                binding.toEdt.setText(data.getStringExtra("cityName")+", "+data.getStringExtra("countryName")+" ("+data.getStringExtra("airportCode")+")");
                to_id = data.getStringExtra("cityId");
                city_name_to = data.getStringExtra("cityName");
            }

        }

    }
    private void showDatePickerDialog() {
        // Lấy ngày, tháng, năm hiện tại
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Xử lý dữ liệu ngày tháng năm được chọn
                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                binding.departureDateEdt.setText(selectedDate); // Đặt dữ liệu ngày tháng năm vào EditText
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeZone(TimeZone.getTimeZone("GMT+7"));
                calendar.set(year, month, dayOfMonth);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                // Chuyển đổi đối tượng Calendar sang dạng Date


                longValue = calendar.getTimeInMillis();
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MILLISECOND, 999);

                endLong = calendar.getTimeInMillis();
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
    }

    @Override
    public void onPassengerCountSelected(int passengerCount) {
        binding.passengerEdt.setText(Integer.toString(passengerCount));
        passenger_number = passengerCount;
    }
}