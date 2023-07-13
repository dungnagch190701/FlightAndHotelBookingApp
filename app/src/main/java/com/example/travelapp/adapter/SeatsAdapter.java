package com.example.travelapp.adapter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.R;
import com.example.travelapp.databinding.ItemSeatsBinding;
import com.example.travelapp.model.SeatModel;

import java.util.List;
import java.util.Objects;

public class SeatsAdapter extends RecyclerView.Adapter<SeatsAdapter.SeatsViewHolder>{
    private List<SeatModel> seatModels;
    SeatClick seatClick;
    private String seat_class;
    private boolean isSeatSelected = false;
    int passenger;
    int count = 0;

    public SeatsAdapter(List<SeatModel> seatModels, int passenger, String seat_class, SeatClick seatClick) {
        this.seatModels = seatModels;
        this.seatClick = seatClick;
        this.seat_class = seat_class;
        this.passenger = passenger;
    }

    @NonNull
    @Override
    public SeatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSeatsBinding binding = ItemSeatsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new SeatsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatsViewHolder holder, int position) {
        SeatModel seatModel = seatModels.get(position);
        if (seatModel.getStatus().equals("sold")){
            holder.binding.seat.setSelected(true);
            holder.binding.seat.setEnabled(false);
        }
        if (!seatModel.getSeat_class().equals(seat_class)){
            holder.binding.seat.setBackgroundDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.not_match_seat_class));
            holder.binding.seat.setEnabled(false);
        }
        holder.binding.seat.setText(seatModel.getSeat_number());
        holder.binding.seat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (holder.binding.seat.isSelected()){
                        isSeatSelected = false;
                        holder.binding.seat.setSelected(false);
                        count--;
                        seatClick.OnSeatClick(seatModel,isSeatSelected);
                    }
                    else
                    {
                        if (count<passenger){
                            isSeatSelected = true;
                            holder.binding.seat.setSelected(true);
                            count++;
                            seatClick.OnSeatClick(seatModel,isSeatSelected);
                        }else {
                            Toast.makeText(view.getContext(), "MAX", Toast.LENGTH_SHORT).show();
                        }


                    }



            }
        });
    }

    @Override
    public int getItemCount() {
        if (seatModels!= null){
            return seatModels.size();
        }
        return 0;
    }

    public static class SeatsViewHolder extends RecyclerView.ViewHolder {
        ItemSeatsBinding binding;
        public SeatsViewHolder(@NonNull ItemSeatsBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
    public interface SeatClick{
        public void OnSeatClick(SeatModel seatModel,Boolean isSeatSelected);
    }
}
