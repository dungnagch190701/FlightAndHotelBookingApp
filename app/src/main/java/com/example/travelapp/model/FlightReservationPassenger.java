package com.example.travelapp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FlightReservationPassenger implements Serializable {
    private FlightReservation flightReservation;
    private String reservation_id;
    private SeatModel seatModel;
    private String seat_id;
    private String passenger_name;
    private Date dob;
    private String nation;
    private String passport_number;

    public FlightReservationPassenger() {
    }

    public FlightReservationPassenger(String seat_id, String passenger_name, Date dob, String nation, String passport_number) {
        this.seat_id = seat_id;
        this.passenger_name = passenger_name;
        this.dob = dob;
        this.nation = nation;
        this.passport_number = passport_number;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("flightReservation_id", reservation_id);
        result.put("seat_id", seat_id);
        result.put("passenger_name", passenger_name);
        result.put("dob", dob);
        result.put("nation", nation);
        result.put("passport_number", passport_number);
        // Don't include profileImage in the map
        return result;
    }





    public String getSeat_id() {
        return seat_id;
    }

    public String getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(String reservation_id) {
        this.reservation_id = reservation_id;
    }

    public void setSeat_id(String seat_id) {
        this.seat_id = seat_id;
    }

    public FlightReservation getFlightReservation() {
        return flightReservation;
    }

    public void setFlightReservation(FlightReservation flightReservation) {
        this.flightReservation = flightReservation;
    }

    public SeatModel getSeatModel() {
        return seatModel;
    }

    public void setSeatModel(SeatModel seatModel) {
        this.seatModel = seatModel;
    }

    public String getPassenger_name() {
        return passenger_name;
    }

    public void setPassenger_name(String passenger_name) {
        this.passenger_name = passenger_name;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getPassport_number() {
        return passport_number;
    }

    public void setPassport_number(String passport_number) {
        this.passport_number = passport_number;
    }
}
