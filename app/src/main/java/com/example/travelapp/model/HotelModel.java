package com.example.travelapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HotelModel implements Serializable {
    private String hotel_id;
    private String name;
    private String address;
    private ArrayList<String> photoURL;
    private Destination locationModel;
    private int rating;
    private String description;
    private float review_avg;
    private int total_review;
    private int price_start_from;
    private String tag;

    public HotelModel() {
    }

    public float getReview_avg() {
        return review_avg;
    }

    public void setReview_avg(float review_avg) {
        this.review_avg = review_avg;
    }

    public int getTotal_review() {
        return total_review;
    }

    public void setTotal_review(int total_review) {
        this.total_review = total_review;
    }

    public HotelModel(String hotel_id, String name, String address, ArrayList<String> photoURL, Destination location, int rating, String description, float review_avg, int total_review,int price_start_from,String tag) {
        this.hotel_id = hotel_id;
        this.name = name;
        this.address = address;
        this.photoURL = photoURL;
        this.locationModel = location;
        this.rating = rating;
        this.description = description;
        this.review_avg = review_avg;
        this.total_review = total_review;
        this.price_start_from = price_start_from;
        this.tag = tag;
    }
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("hotel_id",hotel_id);
        result.put("photoURL",photoURL);
        result.put("location",locationModel);
        result.put("address",address);
        result.put("name",name);
        result.put("tag",tag);
        result.put("rating",rating);
        result.put("total_review",total_review);
        result.put("price_start_from",price_start_from);
        result.put("review_avg",review_avg);
        result.put("description",description);
        return result;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getPrice_start_from() {
        return price_start_from;
    }

    public void setPrice_start_from(int price_start_from) {
        this.price_start_from = price_start_from;
    }

    public ArrayList<String> getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(ArrayList<String> photoURL) {
        this.photoURL = photoURL;
    }

    public String getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(String hotel_id) {
        this.hotel_id = hotel_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Destination getLocationModel() {
        return locationModel;
    }

    public void setLocationModel(Destination locationModel) {
        this.locationModel = locationModel;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

