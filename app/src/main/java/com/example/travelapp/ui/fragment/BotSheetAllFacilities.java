package com.example.travelapp.ui.fragment;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.travelapp.R;
import com.example.travelapp.adapter.FacilitiesAdapter;
import com.example.travelapp.databinding.BotSheetAllFacilitiesBinding;
import com.example.travelapp.model.FacilitiesModel;
import com.example.travelapp.viewmodel.HotelViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class BotSheetAllFacilities extends BottomSheetDialogFragment {
    BottomSheetDialog dialog;
    BottomSheetBehavior<View> bottomSheetBehavior;
    BotSheetAllFacilitiesBinding binding;
    HotelViewModel hotelViewModel;
    FacilitiesAdapter facilitiesAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        hotelViewModel = new ViewModelProvider(requireActivity()).get(HotelViewModel.class);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SomeStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BotSheetAllFacilitiesBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomSheetBehavior = BottomSheetBehavior.from((View)view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        binding.allFacilitiesLayout.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        binding.allFacRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        hotelViewModel.getFacilitiesModelLiveData().observe(requireActivity(), new Observer<List<FacilitiesModel>>() {
            @Override
            public void onChanged(List<FacilitiesModel> facilitiesModels) {
                facilitiesAdapter = new FacilitiesAdapter(facilitiesModels,true);
                binding.allFacRecyclerView.setAdapter(facilitiesAdapter);
            }
        });

    }
}
