package com.example.travelapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.travelapp.adapter.RecentFlightAdapter;
import com.example.travelapp.adapter.ViewedHotelAdapter;
import com.example.travelapp.databinding.ActivityHotelBinding;
import com.example.travelapp.model.Destination;
import com.example.travelapp.model.HotelModel;
import com.example.travelapp.viewmodel.HotelViewModel;
import com.example.travelapp.viewmodel.UserViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HotelActivity extends AppCompatActivity {
    ActivityHotelBinding binding;
    String destination_id,destination_name;
    int room,people;
    Date check_in,check_out;
    UserViewModel userViewModel;
    ViewedHotelAdapter viewedHotelAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHotelBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getViewedHotel();
        binding.recentTxt.setVisibility(View.GONE);
        binding.clearAll.setVisibility(View.GONE);
        binding.clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userViewModel.clearViewedHotel();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        binding.recentSearch.setLayoutManager(layoutManager);
        userViewModel.getViewedHotelLiveData().observe(this, new Observer<List<HotelModel>>() {
            @Override
            public void onChanged(List<HotelModel> hotelModels) {
                if (hotelModels == null || hotelModels.isEmpty()) {
                    binding.clearAll.setVisibility(View.GONE);
                    binding.recentTxt.setVisibility(View.GONE);
                }
                else{
                    binding.recentTxt.setVisibility(View.VISIBLE);
                    binding.clearAll.setVisibility(View.VISIBLE);
                }
                viewedHotelAdapter = new ViewedHotelAdapter(hotelModels);
                binding.recentSearch.setAdapter(viewedHotelAdapter);
            }
        });

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.fromEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DestinationActivity.class);
                startActivityForResult(intent,100);
            }
        });
        binding.checkInEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(binding.checkInEdt);
            }
        });
        binding.checkOutEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(binding.checkOutEdt);
            }
        });
        binding.guestRoomEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog(binding.guestRoomEdt, new OnCompleteListener() {
                    @Override
                    public void onComplete(int roomNumber, int peopleNumber) {
                        room = roomNumber;
                        people = peopleNumber;
                    }
                });
            }
        });
        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateEditTexts(binding.fromEdt,binding.checkInEdt,binding.checkOutEdt,binding.guestRoomEdt)&&
                        isEndDateAfterStartDate(binding.checkInEdt,binding.checkOutEdt)){
                    Intent intent = new Intent(view.getContext(),HotelSearchResult.class);
                    intent.putExtra("destination_id",destination_id);
                    intent.putExtra("check_in_date",binding.checkInEdt.getText().toString());
                    intent.putExtra("check_out_date",binding.checkOutEdt.getText().toString());
                    intent.putExtra("room",room);
                    intent.putExtra("passenger",people);
                    intent.putExtra("destination_name",destination_name);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(HotelActivity.this, "Please enter valid information", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.checkOutEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.checkOutEdt.setError(null);
                binding.errorTextView.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private boolean isEndDateAfterStartDate(EditText startDateEditText, EditText endDateEditText) {
        // Định dạng ngày
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try {
            // Chuyển đổi chuỗi ngày thành đối tượng Date
            Date startDate = dateFormat.parse(startDateEditText.getText().toString());
            Date endDate = dateFormat.parse(endDateEditText.getText().toString());

            // So sánh hai đối tượng Date
            if (endDate.after(startDate)) {
                // Ngày kết thúc lớn hơn ngày đi
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Ngày kết thúc không lớn hơn ngày đi
        endDateEditText.setError("");
        binding.errorTextView.setVisibility(View.VISIBLE);
        return false;
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100){
            if (resultCode == Activity.RESULT_OK){
                Destination destination = (Destination) data.getSerializableExtra("destination");
                destination_id = destination.getDestination_id();
                binding.fromEdt.setText(destination.getCity()+", "+destination.getCountry());
                destination_name = destination.getCity();
            }
        }
    }
    private void showDatePickerDialog(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Format the selected date and set it to the EditText
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        calendar.set(year, month, dayOfMonth);
                        String date = dateFormat.format(calendar.getTime());
                        editText.setText(date);
                    }
                },
                year,
                month,
                day
        );
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }
    public interface OnCompleteListener {
        void onComplete(int roomNumber, int peopleNumber);
    }
    private void showBottomSheetDialog(final EditText editText,final OnCompleteListener listener) {
        // Tạo bottom sheet dialog
        View view = getLayoutInflater().inflate(R.layout.bot_sheet_hotel, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);

        // Lấy view trong bottom sheet dialog
        final NumberPicker roomNumberPicker = view.findViewById(R.id.roomNumberPicker);
        final NumberPicker peopleNumberPicker = view.findViewById(R.id.peopleNumberPicker);
        Button btnDone = view.findViewById(R.id.btnDone);

        // Thiết lập giá trị cho NumberPicker
        roomNumberPicker.setMinValue(1);
        roomNumberPicker.setMaxValue(10);
        roomNumberPicker.setValue(1);
        peopleNumberPicker.setMinValue(1);
        peopleNumberPicker.setMaxValue(10);
        peopleNumberPicker.setValue(1);

        // Thiết lập sự kiện khi nhấn nút "Xong"
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy số phòng và số người từ NumberPicker
                int roomNumber = roomNumberPicker.getValue();
                int peopleNumber = peopleNumberPicker.getValue();

                // Hiển thị số phòng và số người trong EditText
                editText.setText(roomNumber+" room(s) & " + peopleNumber+  " guest(s)");

                listener.onComplete(roomNumber, peopleNumber);
                // Đóng bottom sheet dialog
                dialog.dismiss();
            }
        });

        // Hiển thị bottom sheet dialog
        dialog.show();
    }




}