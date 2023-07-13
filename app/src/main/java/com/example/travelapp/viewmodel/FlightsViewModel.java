package com.example.travelapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.travelapp.model.CityModel;
import com.example.travelapp.model.FlightModel;
import com.example.travelapp.model.FlightReservation;
import com.example.travelapp.model.FlightReservationPassenger;
import com.example.travelapp.model.SearchRecentModel;
import com.example.travelapp.model.SeatModel;
import com.example.travelapp.repository.FlightsRepository;

import java.util.ArrayList;
import java.util.List;

public class FlightsViewModel extends AndroidViewModel {
    private FlightsRepository flightRepository;
    private LiveData<List<CityModel>> citiesLiveData;
    private LiveData<List<FlightModel>> flightLiveData;
    private LiveData<List<SeatModel>> seatLiveData;
    private LiveData<List<FlightReservation>> flightReservationLiveData;

    public FlightsViewModel(@NonNull Application application) {
        super(application);
        flightRepository = new FlightsRepository(application);
        citiesLiveData = flightRepository.getCities();
        seatLiveData = flightRepository.getSeatModelMutableLiveData();
        flightReservationLiveData = flightRepository.getFlightReservationMutableLiveData();

    }

    public LiveData<List<FlightReservation>> getFlightReservationLiveData() {
        return flightReservationLiveData;
    }
    public void getFlightReservation(){flightRepository.getMyFlightBooking();}

    public LiveData<List<SeatModel>> getSeatLiveData() {
        return seatLiveData;
    }

    public void searchFlight(String from_id, String to_id, int passenger, String seat_class, long departure_date,long end_date){
        flightLiveData = flightRepository.searchFlight(from_id,to_id,passenger,seat_class,departure_date,end_date);
    }
    public void searchSeat(String flight_id){
        flightRepository.getSeat(flight_id);
    }

    public void addSearchRecentFlight(SearchRecentModel searchRecentModel){
        flightRepository.addSearchRecentFlight(searchRecentModel);
    }

    public LiveData<List<CityModel>> getCitiesLiveData() {
        return citiesLiveData;
    }

    public LiveData<List<FlightModel>> getFlightLiveData() {
        return flightLiveData;
    }

    public void bookFlight(FlightReservation flightReservation, ArrayList<FlightReservationPassenger> flightReservationPassengers,String seat_class,int payment)
    { flightRepository.bookFlight(flightReservation,flightReservationPassengers,seat_class,payment);}


}
