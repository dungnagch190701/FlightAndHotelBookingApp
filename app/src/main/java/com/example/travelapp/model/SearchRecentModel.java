package com.example.travelapp.model;

public class SearchRecentModel {
    String from_id,to_id,city_from,city_to;
    int passenger_number;
    String seat_class;
    long date,end_date;


    public SearchRecentModel() {
    }

    public SearchRecentModel(String from_id, String to_id, String city_from, String city_to, int passenger_number, String seat_class, long date, long end_date) {
        this.from_id = from_id;
        this.to_id = to_id;
        this.city_from = city_from;
        this.city_to = city_to;
        this.passenger_number = passenger_number;
        this.seat_class = seat_class;
        this.date = date;
        this.end_date = end_date;
    }

    public long getEnd_date() {
        return end_date;
    }

    public void setEnd_date(long end_date) {
        this.end_date = end_date;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getFrom_id() {
        return from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public String getTo_id() {
        return to_id;
    }

    public void setTo_id(String to_id) {
        this.to_id = to_id;
    }

    public String getCity_from() {
        return city_from;
    }

    public void setCity_from(String city_from) {
        this.city_from = city_from;
    }

    public String getCity_to() {
        return city_to;
    }

    public void setCity_to(String city_to) {
        this.city_to = city_to;
    }

    public int getPassenger_number() {
        return passenger_number;
    }

    public void setPassenger_number(int passenger_number) {
        this.passenger_number = passenger_number;
    }

    public String getSeat_class() {
        return seat_class;
    }

    public void setSeat_class(String seat_class) {
        this.seat_class = seat_class;
    }
}
