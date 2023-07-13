package com.example.travelapp.viewmodel;


import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travelapp.model.FlightReservationPassenger;
import com.example.travelapp.model.HotelModel;
import com.example.travelapp.model.SearchRecentModel;
import com.example.travelapp.model.UserModel;
import com.example.travelapp.repository.UserRepository;

import java.util.Date;
import java.util.List;

public class UserViewModel extends ViewModel {
    private UserRepository userRepository;
    private MutableLiveData<UserModel> userModelMutableLiveData;
    private MutableLiveData<List<SearchRecentModel>> searchRecentMutableLiveData;
    private MutableLiveData<List<FlightReservationPassenger>> flightReservationPassengerLiveData;
    private MutableLiveData<Boolean> isReviewLiveData;
    private MutableLiveData<List<HotelModel>> viewedHotelLiveData;

    public UserViewModel() {
        userRepository = new UserRepository();
        userModelMutableLiveData = userRepository.getUserModelMutableLiveData();
        userRepository.getUserDataFromFirestore();
        searchRecentMutableLiveData = userRepository.getSearchRecentMutableLiveData();
        flightReservationPassengerLiveData = userRepository.getFlightReservationPassenger();
        isReviewLiveData = userRepository.getIsReviewMutableLiveData();
        viewedHotelLiveData = userRepository.getHotelModelMutableLiveData();
    }

    public MutableLiveData<List<HotelModel>> getViewedHotelLiveData() {
        return viewedHotelLiveData;
    }

    public MutableLiveData<List<SearchRecentModel>> getSearchRecentMutableLiveData() {
        return searchRecentMutableLiveData;
    }

    public MutableLiveData<UserModel> getUserModelMutableLiveData() {
        return userModelMutableLiveData;
    }
    public MutableLiveData<Boolean> getIsReviewLiveData() {
        return isReviewLiveData;
    }

    public MutableLiveData<List<FlightReservationPassenger>> getFlightReservationPassengerLiveData() {
        return flightReservationPassengerLiveData;
    }
    public void reviewHotel(String hotel_id, float rating, Date date, String comment){
        userRepository.reviewHotel(hotel_id,rating,date,comment);
    }

    public void isReview(String id){userRepository.isReview(id);}
    public void getFlightPassenger(String id){userRepository.getFlightPassenger(id);}
    public void getRecentSearchFlight(){
        userRepository.getSearchRecentFlight();
    }
    public void clearAllRecentSearch(){
        userRepository.clearAllRecent();
    }
    public void getViewedHotel(){userRepository.getViewedHotel();}
    public void clearViewedHotel(){userRepository.clearViewedHotel();}


    public void updateImageProfile(Uri uri){ userRepository.updateImageProfile(uri);}
    public void updateName(String name){userRepository.updateName(name);}
    public void updateEmail(String email){userRepository.updateEmail(email);}


}
