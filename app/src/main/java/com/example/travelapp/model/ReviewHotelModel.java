package com.example.travelapp.model;

import com.google.firebase.firestore.Exclude;

import java.util.Date;

public class ReviewHotelModel {

    private String review_id;
    private String hotel_id;
    private float rating;
    private String comment;
    private Date date;
    private UserModel user_id;

    public ReviewHotelModel() {
    }

    public UserModel getUser_id() {
        return user_id;
    }

    public void setUser_id(UserModel user_id) {
        this.user_id = user_id;
    }

    public ReviewHotelModel(String review_id, String hotel_id, float rating, String comment, Date date) {
        this.review_id = review_id;
        this.hotel_id = hotel_id;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
    }

    public String getReview_id() {
        return review_id;
    }

    public void setReview_id(String review_id) {
        this.review_id = review_id;
    }

    public String getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(String hotel_id) {
        this.hotel_id = hotel_id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
