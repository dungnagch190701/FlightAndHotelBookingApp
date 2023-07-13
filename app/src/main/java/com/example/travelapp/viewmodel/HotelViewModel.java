package com.example.travelapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travelapp.model.Destination;
import com.example.travelapp.model.FacilitiesModel;
import com.example.travelapp.model.HotelModel;
import com.example.travelapp.model.HotelReservationModel;
import com.example.travelapp.model.ReviewHotelModel;
import com.example.travelapp.model.RoomTypeModel;
import com.example.travelapp.repository.HotelRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HotelViewModel extends ViewModel {
    private MutableLiveData<ArrayList<HotelModel>> arrayListMutableLiveData;
    private MutableLiveData<List<ReviewHotelModel>> reviewRecentHotelModelLiveData;
    private MutableLiveData<List<FacilitiesModel>> facilitiesModelLiveData;
    private MutableLiveData<List<RoomTypeModel>> roomTypeModelLiveData;
    private MutableLiveData<List<Destination>> destinationLiveData;
    private MutableLiveData<List<HotelReservationModel>> hotelReservationLiveData;
    private MutableLiveData<List<HotelModel>> savedHotelLiveData;
    private HotelRepository hotelRepository;
    private MutableLiveData<Boolean> isSaved;

    public HotelViewModel() {
        hotelRepository = new HotelRepository();
        arrayListMutableLiveData = hotelRepository.getHotelModelMutableLiveData();
        reviewRecentHotelModelLiveData = hotelRepository.getRecentReviewModelMutableLiveData();
        facilitiesModelLiveData = hotelRepository.getFacilitiesModelMutableLiveData();
        roomTypeModelLiveData = hotelRepository.getRoomTypeMutableLiveData();
        destinationLiveData = hotelRepository.getDestinationMutableLiveData();
        hotelReservationLiveData = hotelRepository.getHotelReservationMutableLiveData();
        isSaved = hotelRepository.getIsSaved();
        savedHotelLiveData = hotelRepository.getHotelSavedMutableLiveData();
    }

    public MutableLiveData<List<HotelModel>> getSavedHotelLiveData() {
        return savedHotelLiveData;
    }

    public MutableLiveData<Boolean> getIsSaved() {
        return isSaved;
    }

    public MutableLiveData<List<HotelReservationModel>> getHotelReservationLiveData() {
        return hotelReservationLiveData;
    }

    public void setHotelReservationLiveData(MutableLiveData<List<HotelReservationModel>> hotelReservationLiveData) {
        this.hotelReservationLiveData = hotelReservationLiveData;
    }

    public MutableLiveData<List<Destination>> getDestinationLiveData() {
        return destinationLiveData;
    }

    public MutableLiveData<List<RoomTypeModel>> getRoomTypeModelLiveData() {
        return roomTypeModelLiveData;
    }

    public MutableLiveData<List<FacilitiesModel>> getFacilitiesModelLiveData() {
        return facilitiesModelLiveData;
    }

    public MutableLiveData<List<ReviewHotelModel>> getReviewRecentHotelModelLiveData() {
        return reviewRecentHotelModelLiveData;
    }

    public MutableLiveData<ArrayList<HotelModel>> getArrayListMutableLiveData() {
        return arrayListMutableLiveData;
    }
    public void getSavedHotel(){hotelRepository.getHotelSaved();}
    public void setIsSaved(String id){hotelRepository.setIsSaved(id);}
    public void unSaveHotel(String id){hotelRepository.unSaveHotel(id);}
    public void saveHotel(HotelModel hotelModel){hotelRepository.saveHotel(hotelModel);}
    public void getBookingHotel(){hotelRepository.getHotelBooking();}
    public void bookHotel(String user_id,String hotel_id,String room_type_id,Date check_in_date,Date check_out_date,int total_passenger,int total_amount,int payment){
        hotelRepository.bookHotel(user_id,hotel_id,room_type_id,check_in_date,check_out_date,total_passenger,total_amount,payment);
    }
    public void getDestination(){hotelRepository.getDestination();}
    public void getRoomType(String hotel_id,Date check_in_date,Date check_out_date,int room,int passenger){
        hotelRepository.getRoomType(hotel_id,check_in_date,check_out_date,room,passenger);
    }

    public void getHotel(String destination, Date check_in_date, int room,Date check_out_date){
        hotelRepository.getHotel(destination,check_in_date,room,check_out_date);
    }
    public void getRecentReview(String hotel_id){
        hotelRepository.getRecentReview(hotel_id);
    }
    public void getFacilities(String hotel_id){ hotelRepository.getFacilities(hotel_id);}
    public void addViewedHotel(HotelModel hotelModel){hotelRepository.addViewedHotel(hotelModel);}
}
