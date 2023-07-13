package com.example.travelapp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FlightReservation implements Serializable {
    private String reservation_id;
    private String user_id;
    private FlightModel flight;
    private Date reservation_date;
    private int total_passenger;
    private float total_amount;
    private int payment;

    public FlightReservation() {
    }

    public FlightReservation(String user, FlightModel flight, Date reservation_date, int total_passenger, float total_amount) {
        this.user_id = user;
        this.flight = flight;
        this.reservation_date = reservation_date;
        this.total_passenger = total_passenger;
        this.total_amount = total_amount;
    }
    public Map<String, Object> toMap(int payment) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("user_id", user_id);
        result.put("flight_id", flight.getFlight_id());
        result.put("reservation_date", reservation_date);
        result.put("total_passenger", total_passenger);
        result.put("total_amount", total_amount);
        result.put("payment",payment);
        // Don't include profileImage in the map
        return result;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public String getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(String reservation_id) {
        this.reservation_id = reservation_id;
    }

    public String getUser() {
        return user_id;
    }

    public void setUser(String user) {
        this.user_id = user;
    }

    public FlightModel getFlight() {
        return flight;
    }

    public void setFlight(FlightModel flight) {
        this.flight = flight;
    }

    public Date getReservation_date() {
        return reservation_date;
    }

    public void setReservation_date(Date reservation_date) {
        this.reservation_date = reservation_date;
    }

    public int getTotal_passenger() {
        return total_passenger;
    }

    public void setTotal_passenger(int total_passenger) {
        this.total_passenger = total_passenger;
    }

    public float getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(float total_amount) {
        this.total_amount = total_amount;
    }
}
