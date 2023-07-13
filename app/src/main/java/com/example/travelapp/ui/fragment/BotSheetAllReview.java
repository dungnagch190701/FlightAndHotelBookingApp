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
import com.example.travelapp.adapter.ReviewHotelAdapter;
import com.example.travelapp.databinding.BotSheetAllFacilitiesBinding;
import com.example.travelapp.databinding.BotSheetAllReviewBinding;
import com.example.travelapp.model.ReviewHotelModel;
import com.example.travelapp.viewmodel.HotelViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class BotSheetAllReview extends BottomSheetDialogFragment {
    BottomSheetDialog dialog;
    BottomSheetBehavior<View> bottomSheetBehavior;
    BotSheetAllReviewBinding binding;
    HotelViewModel hotelViewModel;
    ReviewHotelAdapter reviewHotelAdapter;

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
        binding = BotSheetAllReviewBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomSheetBehavior = BottomSheetBehavior.from((View)view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        binding.allReviewLayout.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        binding.allReviewRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        hotelViewModel.getReviewRecentHotelModelLiveData().observe(requireActivity(), new Observer<List<ReviewHotelModel>>() {
            @Override
            public void onChanged(List<ReviewHotelModel> reviewHotelModels) {
                binding.guestNumber.setText("From "+reviewHotelModels.size()+" guest(s)");
                float avg = calculateAverageRating(reviewHotelModels);
                binding.avg.setText(String.valueOf(avg));
                reviewHotelAdapter = new ReviewHotelAdapter(reviewHotelModels,true);
                binding.allReviewRecyclerView.setAdapter(reviewHotelAdapter);
            }
        });
    }
    public float calculateAverageRating(List<ReviewHotelModel> reviewHotelModels) {
        if (reviewHotelModels.isEmpty()) {
            return 0f;
        }

        float totalRating = 0f;
        for (ReviewHotelModel review : reviewHotelModels) {
            totalRating += review.getRating();
        }

        return totalRating / reviewHotelModels.size();
    }
}
