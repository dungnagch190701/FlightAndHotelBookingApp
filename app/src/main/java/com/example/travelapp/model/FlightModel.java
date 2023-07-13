package com.example.travelapp.model;

import java.io.Serializable;
import java.util.Date;

public class FlightModel implements Serializable{
    private String flight_id;
    private String airline;
    private String flight_number;
    private Date departure_time;
    private Date arrival_time;
    private CityModel origin_model;
    private CityModel destination_model;
    private float economy_price;
    private float premium_economy_price;
    private float business_price;
    private float first_class_price;
    private int available_economy_seat;
    private int available_premium_economy_seat;
    private int available_business_seat;
    private int available_first_class_seat;
    private String logo;

    public FlightModel() {
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public FlightModel(String flight_id, String airline, String flight_number, Date departure_time, Date arrival_time, CityModel origin, CityModel destination, float economy_price, float premium_economy_price, float business_price, float first_class_price, int available_economy_seat, int available_premium_economy_seat, int available_business_seat, int available_first_class_seat) {
        this.flight_id = flight_id;
        this.airline = airline;
        this.flight_number = flight_number;
        this.departure_time = departure_time;
        this.arrival_time = arrival_time;
        this.origin_model = origin;
        this.destination_model = destination;
        this.economy_price = economy_price;
        this.premium_economy_price = premium_economy_price;
        this.business_price = business_price;
        this.first_class_price = first_class_price;
        this.available_economy_seat = available_economy_seat;
        this.available_premium_economy_seat = available_premium_economy_seat;
        this.available_business_seat = available_business_seat;
        this.available_first_class_seat = available_first_class_seat;
    }

    public String getFlight_id() {
        return flight_id;
    }

    public void setFlight_id(String flight_id) {
        this.flight_id = flight_id;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getFlight_number() {
        return flight_number;
    }

    public void setFlight_number(String flight_number) {
        this.flight_number = flight_number;
    }

    public Date getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(Date departure_time) {
        this.departure_time = departure_time;
    }

    public Date getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(Date arrival_time) {
        this.arrival_time = arrival_time;
    }

    public CityModel getOrigin_model() {
        return origin_model;
    }

    public void setOrigin_model(CityModel origin_model) {
        this.origin_model = origin_model;
    }

    public CityModel getDestination_model() {
        return destination_model;
    }

    public void setDestination_model(CityModel destination_model) {
        this.destination_model = destination_model;
    }

    public float getEconomy_price() {
        return economy_price;
    }

    public void setEconomy_price(float economy_price) {
        this.economy_price = economy_price;
    }

    public float getPremium_economy_price() {
        return premium_economy_price;
    }

    public void setPremium_economy_price(float premium_economy_price) {
        this.premium_economy_price = premium_economy_price;
    }

    public float getBusiness_price() {
        return business_price;
    }

    public void setBusiness_price(float business_price) {
        this.business_price = business_price;
    }

    public float getFirst_class_price() {
        return first_class_price;
    }

    public void setFirst_class_price(float first_class_price) {
        this.first_class_price = first_class_price;
    }

    public int getAvailable_economy_seat() {
        return available_economy_seat;
    }

    public void setAvailable_economy_seat(int available_economy_seat) {
        this.available_economy_seat = available_economy_seat;
    }

    public int getAvailable_premium_economy_seat() {
        return available_premium_economy_seat;
    }

    public void setAvailable_premium_economy_seat(int available_premium_economy_seat) {
        this.available_premium_economy_seat = available_premium_economy_seat;
    }

    public int getAvailable_business_seat() {
        return available_business_seat;
    }

    public void setAvailable_business_seat(int available_business_seat) {
        this.available_business_seat = available_business_seat;
    }

    public int getAvailable_first_class_seat() {
        return available_first_class_seat;
    }

    public void setAvailable_first_class_seat(int available_first_class_seat) {
        this.available_first_class_seat = available_first_class_seat;
    }
}
