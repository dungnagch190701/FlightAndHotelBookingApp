package com.example.travelapp.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.travelapp.ConfirmFlightActivity;
import com.example.travelapp.R;
import com.example.travelapp.adapter.SeatsAdapter;
import com.example.travelapp.databinding.BotSheetSeatBinding;
import com.example.travelapp.databinding.BottomSheetFlightBinding;
import com.example.travelapp.model.FlightModel;
import com.example.travelapp.model.SeatModel;
import com.example.travelapp.viewmodel.FlightsViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FlightBottomSheetDialog  extends BottomSheetDialogFragment {

    private FlightModel flightModel;
    private String seat_class;
    private float price;
    private int passenger;
    private ArrayList<String> seatListName;
    private ArrayList<String> seatId;
    private BottomSheetFlightBinding binding;
    public static FlightBottomSheetDialog newInstance(FlightModel flightModel, String seat_class, int passenger){
        FlightBottomSheetDialog bottomSheetDialogFragment = new FlightBottomSheetDialog();

        Bundle bundle = new Bundle();
        bundle.putSerializable("flight_model",flightModel);
        bundle.putString("seat_class",seat_class);
        bundle.putInt("passenger",passenger);
        bottomSheetDialogFragment.setArguments(bundle);
        return bottomSheetDialogFragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleGet = getArguments();
        if (bundleGet!=null){
            flightModel = (FlightModel) bundleGet.get("flight_model");
            seat_class = bundleGet.getString("seat_class");
            if (seat_class.equals("Economy")){
                price = flightModel.getEconomy_price();
            }
            else if (seat_class.equals("Premium Economy")){
                price = flightModel.getPremium_economy_price();
            }
            else if (seat_class.equals("Business")){
                price = flightModel.getBusiness_price();
            }
            else{
                price = flightModel.getFirst_class_price();
            }
            passenger = bundleGet.getInt("passenger",0);
            seatListName = new ArrayList<>();
            seatId =new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        binding = BottomSheetFlightBinding.inflate(LayoutInflater.from(getContext()));
        bottomSheetDialog.setContentView(binding.getRoot());
        binding.title.setText(flightModel.getAirline());
        binding.totalPayTxt.setText("$"+(int) (price*passenger));


        // Đặt kích thước của BottomSheetDialog để nó chiếm toàn bộ không gian màn hình
        bottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 5);
        binding.recyclerview.setLayoutManager(layoutManager);
        // Thiết lập Adapter cho RecyclerView
        FlightsViewModel flightsViewModel = new ViewModelProvider(this).get(FlightsViewModel.class);
        flightsViewModel.searchSeat(flightModel.getFlight_id());
        flightsViewModel.getSeatLiveData().observe(this, new Observer<List<SeatModel>>() {
            @Override
            public void onChanged(List<SeatModel> seatModels) {
                SeatsAdapter adapter = new SeatsAdapter(seatModels,passenger,seat_class, new SeatsAdapter.SeatClick() {
                    @Override
                    public void OnSeatClick(SeatModel seatModel,Boolean isSeatSelected) {
                        String seatNumber = seatModel.getSeat_number();
                        Log.d("SEATID",seatModel.getSeat_id());
                        if (isSeatSelected) {
                            seatListName.add(seatNumber);
                            seatId.add(seatModel.getSeat_id());
                        } else {
                            seatListName.remove(seatNumber);
                            seatId.remove(seatModel.getSeat_id());
                        }
                        String result = seatListName.stream()
                                .collect(Collectors.joining(", "));
                        binding.numberSeat.setText(result);
//
                    }
                });
                binding.recyclerview.setAdapter(adapter);
            }
        });
        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ConfirmFlightActivity.class);
                intent.putExtra("flight",flightModel);
                intent.putExtra("passenger",passenger);
                intent.putExtra("price",price);
                intent.putExtra("seat",seatId);
                intent.putExtra("seat_class",seat_class);
                intent.putStringArrayListExtra("seat_list_name",seatListName);
                startActivity(intent);

            }
        });


        return bottomSheetDialog;
    }

}
