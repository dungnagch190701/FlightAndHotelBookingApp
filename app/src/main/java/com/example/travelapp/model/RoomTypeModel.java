package com.example.travelapp.model;

import java.io.Serializable;
import java.util.List;

public class RoomTypeModel implements Serializable {
    private String room_type_id;
    private String name;
    private String description;
    private int guest_per_room;
    private int price_per_night;
    private List<String> photo_url;
    private int room_available;

    public RoomTypeModel() {
    }

    public String getRoom_type_id() {
        return room_type_id;
    }

    public void setRoom_type_id(String room_type_id) {
        this.room_type_id = room_type_id;
    }

    public int getRoom_available() {
        return room_available;
    }

    public void setRoom_available(int room_available) {
        this.room_available = room_available;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGuest_per_room() {
        return guest_per_room;
    }

    public void setGuest_per_room(int guest_per_room) {
        this.guest_per_room = guest_per_room;
    }

    public int getPrice_per_night() {
        return price_per_night;
    }

    public void setPrice_per_night(int price_per_night) {
        this.price_per_night = price_per_night;
    }

    public List<String> getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(List<String> photo_url) {
        this.photo_url = photo_url;
    }
}
