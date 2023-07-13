package com.example.travelapp.model;

public class ActivitiesModel {

        private int activityId;
        private String destination;
        private String name;
        private String description;
        private String imageUrl;
        private double price;
        private String currency;
        private int duration;
        private float rating;

        // Constructor
        public ActivitiesModel(int activityId, String destination, String name, String description,
                          String imageUrl, double price, String currency, int duration, float rating) {
            this.activityId = activityId;
            this.destination = destination;
            this.name = name;
            this.description = description;
            this.imageUrl = imageUrl;
            this.price = price;
            this.currency = currency;
            this.duration = duration;
            this.rating = rating;
        }

        // Getter methods
        public int getActivityId() {
            return activityId;
        }

        public String getDestination() {
            return destination;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public double getPrice() {
            return price;
        }

        public String getCurrency() {
            return currency;
        }

        public int getDuration() {
            return duration;
        }

        public float getRating() {
            return rating;
        }

        // Setter methods
        public void setActivityId(int activityId) {
            this.activityId = activityId;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public void setRating(float rating) {
            this.rating = rating;
        }



}
