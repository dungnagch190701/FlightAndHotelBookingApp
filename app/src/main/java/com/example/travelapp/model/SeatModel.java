package com.example.travelapp.model;

import java.io.Serializable;

public class SeatModel implements Serializable {
    private String seat_id;
    private String flight_id;
    private String seat_class;
    private String seat_number;
    private String status;

    public SeatModel() {
    }

    public SeatModel(String seat_id, String flight_id, String seat_class, String seat_number, String status) {
        this.seat_id = seat_id;
        this.flight_id = flight_id;
        this.seat_class = seat_class;
        this.seat_number = seat_number;
        this.status = status;
    }

    public String getSeat_id() {
        return seat_id;
    }

    public void setSeat_id(String seat_id) {
        this.seat_id = seat_id;
    }

    public String getFlight_id() {
        return flight_id;
    }

    public void setFlight_id(String flight_id) {
        this.flight_id = flight_id;
    }

    public String getSeat_class() {
        return seat_class;
    }

    public void setSeat_class(String seat_class) {
        this.seat_class = seat_class;
    }

    public String getSeat_number() {
        return seat_number;
    }

    public void setSeat_number(String seat_number) {
        this.seat_number = seat_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
