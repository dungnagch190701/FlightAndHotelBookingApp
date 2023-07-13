package com.example.travelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.travelapp.adapter.AirlineAdapter;
import com.example.travelapp.adapter.HotelAdapter;
import com.example.travelapp.databinding.ActivityHotelSearchResultBinding;
import com.example.travelapp.databinding.BotFilterHotelBinding;
import com.example.travelapp.databinding.BotSortHotelBinding;
import com.example.travelapp.model.FlightModel;
import com.example.travelapp.model.HotelModel;
import com.example.travelapp.viewmodel.HotelViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;

import org.checkerframework.checker.units.qual.A;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class HotelSearchResult extends AppCompatActivity {
    ActivityHotelSearchResultBinding binding;
    HotelViewModel hotelViewModel;
    private Date check_in_date,check_out_date;
    private String in_str,out_str;
    private String id,name;
    private int room = 3;
    private int passenger = 4;
    HotelAdapter adapter;
    List<HotelModel> hotelModelList;
    List<String> selectedAirline;
    String previousSelectedValue;
    int previous_option;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHotelSearchResultBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        id = getIntent().getStringExtra("destination_id");
        name = getIntent().getStringExtra("destination_name");
        in_str = getIntent().getStringExtra("check_in_date");
        out_str = getIntent().getStringExtra("check_out_date");
        room = getIntent().getIntExtra("room",0);
        passenger = getIntent().getIntExtra("passenger",0);
        binding.title1.setText(name);
        binding.title2.setText(in_str);
        hotelViewModel = new ViewModelProvider(this).get(HotelViewModel.class);
        binding.room.setText(room +" room(s)");
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            check_in_date = dateFormat.parse(in_str);
            check_out_date = dateFormat.parse(out_str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long difference = check_out_date.getTime() - check_in_date.getTime();
        float daysBetween = (difference / (1000*60*60*24));
        int days = (int) daysBetween;
        binding.night.setText(days+" night(s)");
        binding.hotelRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        hotelViewModel.getHotel(id,check_in_date,room,check_out_date);
        hotelViewModel.getArrayListMutableLiveData().observe(this, new Observer<ArrayList<HotelModel>>() {
            @Override
            public void onChanged(ArrayList<HotelModel> hotelModels) {
                hotelModelList = hotelModels;
                adapter = new HotelAdapter(hotelModels,getApplicationContext(),check_in_date,check_out_date,room,passenger);
                binding.hotelRecyclerView.setAdapter(adapter);
            }
        });
        binding.sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSortBot();
            }
        });
        binding.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFilterBot();
            }
        });
        selectedAirline = new ArrayList<>();
    }
    private List<String> getAllTypes() {
        List<String> types = new ArrayList<>();
        for (HotelModel type : hotelModelList) {
            String tag = type.getTag();
            types.add(tag);
        }
        return types;
    }

    private void openFilterBot() {
        BotFilterHotelBinding botFilterHotelBinding;
        botFilterHotelBinding = BotFilterHotelBinding.inflate(getLayoutInflater());
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(botFilterHotelBinding.getRoot());
        bottomSheetDialog.show();
        List<String> airlineList = getAllTypes();
        AirlineAdapter airlineAdapter = new AirlineAdapter(airlineList,selectedAirline);
        botFilterHotelBinding.recyclerView.setAdapter(airlineAdapter);
        botFilterHotelBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (previousSelectedValue == null) {

        }
        else if (previousSelectedValue.equals("1")) {
            botFilterHotelBinding.b1.setChecked(true);
        } else if (previousSelectedValue.equals("2")) {
            botFilterHotelBinding.b2.setChecked(true);
        } else if (previousSelectedValue.equals("3")) {
            botFilterHotelBinding.b3.setChecked(true);
        } else if (previousSelectedValue.equals("4")) {
            botFilterHotelBinding.b4.setChecked(true);
        } else if (previousSelectedValue.equals("5")) {
            botFilterHotelBinding.b5.setChecked(true);
        }

        botFilterHotelBinding.reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.updateHotelList(hotelModelList);
                selectedAirline.clear();
//                size = flightList.size();
//                binding.totalFlight.setText("Showing " + size+ " flight(s)");
                bottomSheetDialog.dismiss();
            }
        });
        botFilterHotelBinding.apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> selectedAirlines = airlineAdapter.getSelectedAirlines();
                selectedAirline = airlineAdapter.getSelectedAirlines();
                List<HotelModel> filterByType = filterByType(selectedAirlines);
                int selectedId = botFilterHotelBinding.radioGrp.getCheckedRadioButtonId();
                String selectedValue = null;

                if (selectedId != View.NO_ID) {
                    RadioButton selectedRadioButton = botFilterHotelBinding.getRoot().findViewById(selectedId);
                    selectedValue = selectedRadioButton.getText().toString();
                }

                previousSelectedValue = selectedValue;
                int selectedIntValue;

                try {
                    selectedIntValue = Integer.parseInt(selectedValue);
                } catch (NumberFormatException e) {
                    selectedIntValue = 0;
                }
                List<HotelModel> filterByRating = filterByRating(filterByType,selectedIntValue);
                float minValue = botFilterHotelBinding.priceRangeSlider.getValues().get(0);
                float maxValue = botFilterHotelBinding.priceRangeSlider.getValues().get(1);
                List<HotelModel> filterPrice = filterByPrice(filterByRating,minValue,maxValue);
                adapter.updateHotelList(filterPrice);
                bottomSheetDialog.dismiss();
            }
        });
        LabelFormatter labelFormatter = new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                // Định dạng giá trị và thêm ký hiệu dollar ($)
                return "$" + String.valueOf((int) value);
            }
        };
        botFilterHotelBinding.priceRangeSlider.setLabelFormatter(labelFormatter);
        botFilterHotelBinding.priceRangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                if (fromUser) {
                    // Xử lý logic khi người dùng thay đổi giá trị trên RangeSlider
                    int minValue = (int) Math.floor(slider.getValues().get(0));
                    int maxValue = (int) Math.floor(slider.getValues().get(1));
                    // Cập nhật hiển thị giá tối thiểu và tối đa
                    botFilterHotelBinding.textViewMinPrice.setText("$"+String.valueOf(minValue)+" - "+"$"+String.valueOf(maxValue));

                }
            }
        });
    }

    private List<HotelModel> filterByPrice(List<HotelModel> filterByRating, float minValue, float maxValue) {
        List<HotelModel> filteredListByPrice = new ArrayList<>();
        int minIntValue = (int) Math.floor(minValue);
        int maxIntValue = (int) Math.floor(maxValue);
        for (HotelModel hotelModel : filterByRating) {

            // Giá của chuyến bay (có thể sử dụng giá khác nếu cần)
            if (hotelModel.getPrice_start_from() >= minIntValue && hotelModel.getPrice_start_from() <= maxIntValue) {
                filteredListByPrice.add(hotelModel);
            }
        }
        return filteredListByPrice;
    }

    private List<HotelModel> filterByRating(List<HotelModel> filterByType, int selectedIntValue) {
        List<HotelModel> filteredHotelList = new ArrayList<>();
        if (selectedIntValue!=0){
            for (HotelModel hotel : filterByType) {
                if (hotel.getRating() == selectedIntValue) {

                    filteredHotelList.add(hotel);
                }
            }
        } else{
            return filterByType;
        }
        return filteredHotelList;
    }

    private List<HotelModel> filterByType(List<String> selectedAirlines) {
        List<HotelModel> filteredHotelList = new ArrayList<>();
        if (!selectedAirlines.isEmpty()) {
            for (HotelModel hotel : hotelModelList) {
                if (selectedAirlines.contains(hotel.getTag())) {
                    // Nếu airline của chuyến bay nằm trong danh sách airline đã chọn, thêm chuyến bay vào danh sách lọc
                    filteredHotelList.add(hotel);
                }
            }
        } else {
            filteredHotelList.addAll(hotelModelList);
        }
        return filteredHotelList;
    }

    private void openSortBot() {
        BotSortHotelBinding botSortHotelBinding;
        botSortHotelBinding = BotSortHotelBinding.inflate(getLayoutInflater());
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(botSortHotelBinding.getRoot());
        bottomSheetDialog.show();

        switch (previous_option) {
            case 1:
                botSortHotelBinding.lowestPrice.setChecked(true);
                break;
            case 2:
                botSortHotelBinding.highPrice.setChecked(true);
                break;
            case 3:
                botSortHotelBinding.highRating.setChecked(true);
                break;

        }
        botSortHotelBinding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.lowest_price:
                        hotelModelList.sort(new Comparator<HotelModel>() {
                            @Override
                            public int compare(HotelModel hotelModel, HotelModel t1) {
                                return Integer.compare(hotelModel.getPrice_start_from(), t1.getPrice_start_from());
                            }
                        });
                        adapter.notifyDataSetChanged();
                        previous_option = 1;
                        bottomSheetDialog.dismiss();
                        break;
                    case R.id.high_price:
                        hotelModelList.sort(new Comparator<HotelModel>() {
                            @Override
                            public int compare(HotelModel hotelModel, HotelModel t1) {
                                return Integer.compare(t1.getPrice_start_from(), hotelModel.getPrice_start_from());
                            }
                        });
                        adapter.notifyDataSetChanged();
                        previous_option = 2;
                        bottomSheetDialog.dismiss();
                        break;
                        case R.id.high_rating:
                        hotelModelList.sort(new Comparator<HotelModel>() {
                            @Override
                            public int compare(HotelModel hotelModel, HotelModel t1) {
                                return Integer.compare(t1.getRating(), hotelModel.getRating());
                            }
                        });
                        adapter.notifyDataSetChanged();
                        previous_option = 3;
                        bottomSheetDialog.dismiss();
                        break;

                }
            }
        });
    }
}