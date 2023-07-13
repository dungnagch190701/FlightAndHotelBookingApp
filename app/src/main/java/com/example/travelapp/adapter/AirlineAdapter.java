package com.example.travelapp.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.R;

import java.util.ArrayList;
import java.util.List;

public class AirlineAdapter extends RecyclerView.Adapter<AirlineAdapter.ViewHolder> {
    private List<String> airlineList;
    private List<String> selectedAirlines;

    public AirlineAdapter(List<String> airlineList,List<String> selectedAirlines) {
        this.airlineList = airlineList;
        if (selectedAirlines.isEmpty()){
            this.selectedAirlines = new ArrayList<>();
        }
        else{
            this.selectedAirlines = selectedAirlines;
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_airline, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AirlineAdapter.ViewHolder holder, int position) {
        String airline = airlineList.get(position);
        holder.airlineCheckBox.setText(airline);

        // Kiểm tra xem airline có nằm trong danh sách airline đã chọn hay không
        boolean isSelected = selectedAirlines.contains(airline);
        holder.airlineCheckBox.setChecked(isSelected);

        // Xử lý sự kiện khi chọn hoặc bỏ chọn airline
        holder.airlineCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedAirlines.add(airline);
                } else {
                    selectedAirlines.remove(airline);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (airlineList != null) {
            return airlineList.size();
        }
        return 0;
    }

    public List<String> getSelectedAirlines() {
        return selectedAirlines;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox airlineCheckBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            airlineCheckBox = itemView.findViewById(R.id.check_box);
        }
    }
}