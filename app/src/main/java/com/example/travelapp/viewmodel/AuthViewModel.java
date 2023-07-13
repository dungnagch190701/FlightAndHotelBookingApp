package com.example.travelapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.travelapp.model.HotelModel;
import com.example.travelapp.model.UserModel;
import com.example.travelapp.repository.AuthenticationRepository;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class AuthViewModel extends AndroidViewModel {
    private AuthenticationRepository repository;
    private MutableLiveData<FirebaseUser> userData;
    private MutableLiveData<Boolean> loggedStatus;
    private MutableLiveData<UserModel> userFStore;
    private MutableLiveData<List<HotelModel>> highRatingHotel;
    private MutableLiveData<List<HotelModel>> highReviewHotel;

    public MutableLiveData<List<HotelModel>> getHighReviewHotel() {
        return highReviewHotel;
    }

    public MutableLiveData<List<HotelModel>> getHighRatingHotel() {
        return highRatingHotel;
    }

    public MutableLiveData<FirebaseUser> getUserData() {
        return userData;
    }

    public MutableLiveData<Boolean> getLoggedStatus() {
        return loggedStatus;
    }

    public MutableLiveData<UserModel> getUserFStore() {
        return userFStore;
    }

    public AuthViewModel(@NonNull  Application application) {
        super(application);
        repository = new AuthenticationRepository(application);
        userData = repository.getFirebaseUserMutableLiveData();
        loggedStatus = repository.getUserLoggedMutableLiveData();
        userFStore = repository.getUserFStoreMutableLiveData();
        highRatingHotel = repository.getHighRatingHotelMutableLiveData();
        highReviewHotel = repository.getHighReviewHotelMutableLiveData();
    }
    public void signUp(String email , String pass,String name, String phone){
        repository.register(email, pass,name,phone);
    }
    public void signIn(String email , String pass){
        repository.login(email, pass);
    }
    public void signOut(){
        repository.signOut();
    }
    public void getUserDataFromFirestore() { repository.getUserDataFromFirestore();}
    public void getHotelHighRating(){repository.getHighRatingHotel();}
    public void getHotelHighReview(){repository.getHighReview();}


}
