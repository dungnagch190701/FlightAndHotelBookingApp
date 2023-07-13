package com.example.travelapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.Nullable;

import com.example.travelapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BotSheetPassenger extends BottomSheetDialogFragment {

    private NumberPicker numberPicker;
    private Button btnDone;

    public static BotSheetPassenger newInstance() {
        return new BotSheetPassenger();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bot_sheet_passenger, container, false);
        numberPicker = view.findViewById(R.id.numberPicker);
        btnDone = view.findViewById(R.id.btnDone);

        // Thiết lập giá trị số lượng hành khách tối thiểu và tối đa
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy giá trị số lượng hành khách được chọn
                int passengerCount = numberPicker.getValue();

                // Gọi phương thức callback để truyền giá trị số lượng hành khách về cho Activity hoặc Fragment gọi bottom sheet
                if (getTargetFragment() instanceof OnPassengerCountSelectedListener) {
                    ((OnPassengerCountSelectedListener) getTargetFragment()).onPassengerCountSelected(passengerCount);
                }

                // Đóng bottom sheet
                dismiss();
            }
        });

        return view;
    }

    public interface OnPassengerCountSelectedListener {
        void onPassengerCountSelected(int passengerCount);
    }
}

