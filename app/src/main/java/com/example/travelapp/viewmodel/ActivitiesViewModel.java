package com.example.travelapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travelapp.model.ActivitiesModel;

import java.util.ArrayList;
import java.util.List;

public class ActivitiesViewModel extends ViewModel {
    private MutableLiveData<List<ActivitiesModel>> activitiesModelMutableLiveData;
    private List<ActivitiesModel> activitiesModelList;

    public MutableLiveData<List<ActivitiesModel>> getActivitiesModelMutableLiveData() {
        return activitiesModelMutableLiveData;
    }

    public List<ActivitiesModel> getActivitiesModelList() {
        return activitiesModelList;
    }

    public ActivitiesViewModel(){
        activitiesModelMutableLiveData = new MutableLiveData<>();
        activitiesModelList = new ArrayList<>();
        activitiesModelList.add(new ActivitiesModel(1, "Hawaii", "Surfing", "Learn to surf on the beautiful beaches of Hawaii", "https://pix10.agoda.net/hotelImages/124/1246280/1246280_16061017110043391702.jpg", 50.0, "USD", 2, 5f));
        activitiesModelList.add(new ActivitiesModel(2, "France", "Wine Tasting", "Experience the finest wines of France with a professional sommelier", "https://majestichotelgroup.com/web/majestic/homepage/mobile/slider/00majestic-hotel-and--spa-fachada.jpg", 100.0, "EUR", 3, 4f));
        activitiesModelList.add(new ActivitiesModel(3, "Thailand", "Elephant Trekking", "Ride majestic elephants through the lush jungles of Thailand", "https://media-cdn.tripadvisor.com/media/photo-s/14/60/4e/ef/image-hotel-resto.jpg", 75.0, "THB", 4, 4.2f));
        activitiesModelList.add(new ActivitiesModel(4, "Australia", "Scuba Diving", "Explore the Great Barrier Reef with certified scuba diving instructors", "https://artishotel.vn/wp-content/uploads/2021/12/artishotel.png", 150.0, "AUD", 5, 4.9f));
        activitiesModelList.add(new ActivitiesModel(5, "Japan", "Sushi Making", "Learn the art of making authentic sushi from master chefs in Japan", "https://example.com/sushi-making.jpg", 80.0, "JPY", 2, 4.6f));
        activitiesModelMutableLiveData.postValue(activitiesModelList);
    }


}
