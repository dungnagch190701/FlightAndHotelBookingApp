package com.example.travelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.travelapp.adapter.AirlineAdapter;
import com.example.travelapp.adapter.FlightSearchResultAdapter;
import com.example.travelapp.databinding.ActivitySearchFlightResultBinding;
import com.example.travelapp.databinding.BotFilterFlightBinding;
import com.example.travelapp.databinding.BotSheetSeatBinding;
import com.example.travelapp.databinding.BotSortFlightBinding;
import com.example.travelapp.model.FlightModel;
import com.example.travelapp.model.SearchRecentModel;
import com.example.travelapp.ui.fragment.FlightBottomSheetDialog;
import com.example.travelapp.viewmodel.FlightsViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class SearchFlightResultActivity extends AppCompatActivity {
    ActivitySearchFlightResultBinding binding;
    FlightsViewModel flightsViewModel;
    FlightSearchResultAdapter flightSearchResultAdapter;
    List<FlightModel> flightList;
    String seat_class;
    int passenger;
    int previous_option;
    List<String> selectedAirline;
    int size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchFlightResultBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.recyclerviewSearchResult.setLayoutManager(new LinearLayoutManager(this));
        flightsViewModel = new ViewModelProvider(this , (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())).get(FlightsViewModel.class);
        String from_id = getIntent().getStringExtra("from_id");
        String to_id = getIntent().getStringExtra("to_id");
        passenger = getIntent().getIntExtra("passenger",0);
        seat_class = getIntent().getStringExtra("seat_class");
        long departure_date = getIntent().getLongExtra("departure_date",0);
        long end_date = getIntent().getLongExtra("end_date",0);
        String city_name_from = getIntent().getStringExtra("city_name_from");
        String city_name_to = getIntent().getStringExtra("city_name_to");
        binding.title1.setText(city_name_from + " -> " + city_name_to);
        binding.pax.setText(passenger+ "pax");
        binding.seatClass.setText(seat_class);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"));
        calendar.setTimeInMillis(departure_date);
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        String formattedDate = sdf.format(date);
        binding.title2.setText(formattedDate);

        SearchRecentModel searchRecentModel = new SearchRecentModel(from_id,to_id,city_name_from,city_name_to,passenger,seat_class,departure_date,end_date);
        flightsViewModel.addSearchRecentFlight(searchRecentModel);
        flightsViewModel.searchFlight(from_id,to_id,passenger,seat_class,departure_date,end_date);
        flightsViewModel.getFlightLiveData().observe(this, new Observer<List<FlightModel>>() {
            @Override
            public void onChanged(List<FlightModel> flightModels) {
                flightList = flightModels;
                size = flightModels.size();
                binding.totalFlight.setText("Showing " + size+ " flight(s)");
                flightSearchResultAdapter = new FlightSearchResultAdapter(flightModels, seat_class, new FlightSearchResultAdapter.onClickFlight() {
                    @Override
                    public void onClick(FlightModel flightModel) {
                        FlightBottomSheetDialog flightBottomSheetDialog = FlightBottomSheetDialog.newInstance(flightModel,seat_class,passenger);
                        flightBottomSheetDialog.show(getSupportFragmentManager(),flightBottomSheetDialog.getTag());
                    }
                });
                binding.recyclerviewSearchResult.setAdapter(flightSearchResultAdapter);
            }
        });
        binding.sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBotSheet();
            }
        });
        binding.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFilterBotSheet();
            }
        });
        selectedAirline = new ArrayList<>();

    }

    private void openFilterBotSheet() {
        BotFilterFlightBinding botFilterFlightBinding;
        botFilterFlightBinding = BotFilterFlightBinding.inflate(getLayoutInflater());
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(botFilterFlightBinding.getRoot());
        bottomSheetDialog.show();
        bottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        List<String> airlineList = getAllAirlines(); // Hàm này trả về danh sách airline
        AirlineAdapter airlineAdapter = new AirlineAdapter(airlineList,selectedAirline);
        botFilterFlightBinding.recyclerView.setAdapter(airlineAdapter);
        botFilterFlightBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        botFilterFlightBinding.apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> selectedAirlines = airlineAdapter.getSelectedAirlines();
                selectedAirline = airlineAdapter.getSelectedAirlines();
                // Xử lý lọc theo danh sách airline đã chọn
                List<FlightModel> filterAirline = filterByAirlines(selectedAirlines);
                float minValue = botFilterFlightBinding.priceRangeSlider.getValues().get(0);
                float maxValue = botFilterFlightBinding.priceRangeSlider.getValues().get(1);
                List<FlightModel> filterPrice = filterByPrice(filterAirline,minValue,maxValue);
                flightSearchResultAdapter.updateFlightList(filterPrice);
                size = filterPrice.size();
                binding.totalFlight.setText("Showing " + size+ " flight(s)");
                bottomSheetDialog.dismiss();
            }
        });
        botFilterFlightBinding.reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flightSearchResultAdapter.updateFlightList(flightList);
                selectedAirline.clear();
                size = flightList.size();
                binding.totalFlight.setText("Showing " + size+ " flight(s)");
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
        botFilterFlightBinding.priceRangeSlider.setLabelFormatter(labelFormatter);
        botFilterFlightBinding.priceRangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                if (fromUser) {
                    // Xử lý logic khi người dùng thay đổi giá trị trên RangeSlider
                    int minValue = (int) Math.floor(slider.getValues().get(0));
                    int maxValue = (int) Math.floor(slider.getValues().get(1));
                    // Cập nhật hiển thị giá tối thiểu và tối đa
                    botFilterFlightBinding.textViewMinPrice.setText("$"+String.valueOf(minValue)+" - "+"$"+String.valueOf(maxValue));

                }
            }
        });
    }

    private List<FlightModel> filterByPrice(List<FlightModel> filterAirline, float minValue, float maxValue) {
        List<FlightModel> filteredListByPrice = new ArrayList<>();
        for (FlightModel flight : filterAirline) {
            float flightPrice = 0;
            if (seat_class.equals("Economy")){
                flightPrice = flight.getEconomy_price();
            } else if (seat_class.equals("Premium Economy")){
                flightPrice = flight.getPremium_economy_price();
            } else if (seat_class.equals("Business")){
                flightPrice = flight.getBusiness_price();
            } else
            {
                flightPrice = flight.getFirst_class_price();
            }
             // Giá của chuyến bay (có thể sử dụng giá khác nếu cần)
            if (flightPrice >= minValue && flightPrice <= maxValue) {
                filteredListByPrice.add(flight);
            }
        }
        return filteredListByPrice;
    }

    private List<FlightModel> filterByAirlines(List<String> selectedAirlines) {
        List<FlightModel> filteredFlightList = new ArrayList<>();
        if (!selectedAirlines.isEmpty()) {
            for (FlightModel flight : flightList) {
                if (selectedAirlines.contains(flight.getAirline())) {
                    // Nếu airline của chuyến bay nằm trong danh sách airline đã chọn, thêm chuyến bay vào danh sách lọc
                    filteredFlightList.add(flight);
                }
            }
        } else {
            filteredFlightList.addAll(flightList);
        }
        return filteredFlightList;
    }

    private List<String> getAllAirlines() {
        List<String> airlines = new ArrayList<>();
        for (FlightModel flight : flightList) {
            String airline = flight.getAirline();
            airlines.add(airline);
        }
        return airlines;
    }


    private void openBotSheet() {
        BotSortFlightBinding botSortFlightBinding;
        botSortFlightBinding = BotSortFlightBinding.inflate(getLayoutInflater());
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(botSortFlightBinding.getRoot());
        bottomSheetDialog.show();
        botSortFlightBinding.buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        switch (previous_option) {
            case 1:
                botSortFlightBinding.lowestPrice.setChecked(true);
                break;
            case 2:
                botSortFlightBinding.earlyDeparture.setChecked(true);
                break;
            case 3:
                botSortFlightBinding.lastDeparture.setChecked(true);
                break;
            case 4:
                botSortFlightBinding.earlyArrival.setChecked(true);
                break;
        }
        botSortFlightBinding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.lowest_price:
                        flightList.sort(new Comparator<FlightModel>() {
                            @Override
                            public int compare(FlightModel f1, FlightModel f2) {
                                return Float.compare(f1.getEconomy_price(), f2.getEconomy_price());
                            }
                        });
                        flightSearchResultAdapter.notifyDataSetChanged();
                        previous_option = 1;
                        bottomSheetDialog.dismiss();
                        break;
                    case R.id.early_departure:
                        flightList.sort(new Comparator<FlightModel>() {
                            @Override
                            public int compare(FlightModel flight1, FlightModel flight2) {
                                return flight1.getDeparture_time().compareTo(flight2.getDeparture_time());
                            }
                        });

                        flightSearchResultAdapter.notifyDataSetChanged();
                        previous_option = 2;
                        bottomSheetDialog.dismiss();
                        break;
                    case R.id.last_departure:
                        flightList.sort(new Comparator<FlightModel>() {
                            @Override
                            public int compare(FlightModel flight1, FlightModel flight2) {
                                return flight2.getDeparture_time().compareTo(flight1.getDeparture_time());
                            }
                        });
                        flightSearchResultAdapter.notifyDataSetChanged();
                        previous_option = 3;
                        bottomSheetDialog.dismiss();
                        break;
                    case R.id.early_arrival:
                        flightList.sort(new Comparator<FlightModel>() {
                            @Override
                            public int compare(FlightModel flight1, FlightModel flight2) {
                                return flight1.getArrival_time().compareTo(flight2.getArrival_time());
                            }
                        });
                        flightSearchResultAdapter.notifyDataSetChanged();
                        previous_option = 4;
                        bottomSheetDialog.dismiss();
                        break;
                    case R.id.last_arrival:
                        flightList.sort(new Comparator<FlightModel>() {
                            @Override
                            public int compare(FlightModel flight1, FlightModel flight2) {
                                return flight2.getArrival_time().compareTo(flight1.getArrival_time());
                            }
                        });
                        flightSearchResultAdapter.notifyDataSetChanged();
                        previous_option = 5;
                        bottomSheetDialog.dismiss();
                        break;
                    case R.id.short_duration:
                        flightList.sort(new Comparator<FlightModel>() {
                            @Override
                            public int compare(FlightModel f1, FlightModel f2) {
                                long f1Duration = f1.getArrival_time().getTime() - f1.getDeparture_time().getTime();
                                long f2Duration = f2.getArrival_time().getTime() - f2.getDeparture_time().getTime();
                                return Long.compare(f1Duration, f2Duration);
                            }
                        });
                        flightSearchResultAdapter.notifyDataSetChanged();
                        previous_option = 6;
                        break;
                }
            }
        });


    }
}