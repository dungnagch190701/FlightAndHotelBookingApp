package com.example.travelapp.model;

import java.io.Serializable;

public class Destination implements Serializable {
    private String destination_id;
    private String city;
    private String country;

    public Destination() {
    }

    public Destination(String destination_id, String city, String country) {
        this.destination_id = destination_id;
        this.city = city;
        this.country = country;
    }

    public String getDestination_id() {
        return destination_id;
    }

    public void setDestination_id(String destination_id) {
        this.destination_id = destination_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
