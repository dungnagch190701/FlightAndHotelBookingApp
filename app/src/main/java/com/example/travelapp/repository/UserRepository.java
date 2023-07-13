package com.example.travelapp.repository;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travelapp.model.FlightReservationPassenger;
import com.example.travelapp.model.HotelModel;
import com.example.travelapp.model.SearchRecentModel;
import com.example.travelapp.model.SeatModel;
import com.example.travelapp.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepository {

    private MutableLiveData<UserModel> userModelMutableLiveData;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference firebaseStorage;
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<List<SearchRecentModel>> searchRecentMutableLiveData;
    private MutableLiveData<List<FlightReservationPassenger>> flightReservationPassengerMutableLiveData;
    private MutableLiveData<Boolean> isReviewMutableLiveData;
    private MutableLiveData<List<HotelModel>> hotelModelMutableLiveData;


    public UserRepository() {
        userModelMutableLiveData = new MutableLiveData<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        searchRecentMutableLiveData = new MutableLiveData<>();
        flightReservationPassengerMutableLiveData = new MutableLiveData<>();
        isReviewMutableLiveData = new MutableLiveData<>();
        hotelModelMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<HotelModel>> getHotelModelMutableLiveData() {
        return hotelModelMutableLiveData;
    }

    public MutableLiveData<Boolean> getIsReviewMutableLiveData() {
        return isReviewMutableLiveData;
    }

    public MutableLiveData<List<FlightReservationPassenger>> getFlightReservationPassenger() {
        return flightReservationPassengerMutableLiveData;
    }

    public MutableLiveData<List<SearchRecentModel>> getSearchRecentMutableLiveData() {
        return searchRecentMutableLiveData;
    }
//    public void reviewHotel(String hotel_id, float rating, Date date,String comment){
//        Map<String, Object> review = new HashMap<>();
//        review.put("hotel_id",hotel_id);
//        review.put("rating",rating);
//        review.put("user_id",firebaseAuth.getUid());
//        review.put("date",date);
//        review.put("comment",comment);
//        firebaseFirestore.collection("HotelReview")
//                .add(review);
//    }
public void reviewHotel(String hotel_id, float rating, Date date, String comment) {
    // Lấy khách sạn hiện tại từ bảng "Hotels"
    DocumentReference hotelRef = firebaseFirestore.collection("Hotels").document(hotel_id);
    hotelRef.get().addOnSuccessListener(documentSnapshot -> {
        if (documentSnapshot.exists()) {
            // Lấy dữ liệu hiện tại của khách sạn
            int totalReview = documentSnapshot.getLong("total_review").intValue();
            float reviewAvg = documentSnapshot.getDouble("review_avg").floatValue();

            // Tính toán các giá trị mới
            int newTotalReview = totalReview + 1;
            float newReviewAvg = (reviewAvg * totalReview + rating) / newTotalReview;

            // Cập nhật lại dữ liệu của khách sạn
            Map<String, Object> hotelData = new HashMap<>();
            hotelData.put("total_review", newTotalReview);
            hotelData.put("review_avg", newReviewAvg);
            hotelRef.update(hotelData)
                    .addOnSuccessListener(aVoid -> {
                        // Thêm review vào bảng "HotelReview"
                        Map<String, Object> review = new HashMap<>();
                        review.put("hotel_id", hotel_id);
                        review.put("rating", rating);
                        review.put("user_id", firebaseAuth.getUid());
                        review.put("date", date);
                        review.put("comment", comment);
                        firebaseFirestore.collection("HotelReview")
                                .add(review);
                    })
                    .addOnFailureListener(e -> {
                        // Xử lý khi cập nhật dữ liệu khách sạn thất bại
                    });
        } else {
            // Xử lý khi không tìm thấy khách sạn với hotel_id tương ứng
        }
    }).addOnFailureListener(e -> {
        // Xử lý khi không thể truy cập dữ liệu khách sạn
    });
}

    public void isReview(String id){
        firebaseFirestore.collection("HotelReview")
                .whereEqualTo("hotel_id",id)
                .whereEqualTo("user_id",firebaseAuth.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("REAL FAIL", "Listen failed.", error);
                            return;
                        }
                        for (QueryDocumentSnapshot queryDocumentSnapshot: value){
                            if (queryDocumentSnapshot != null && queryDocumentSnapshot.exists()){
                                isReviewMutableLiveData.postValue(true);
                            }else {
                                isReviewMutableLiveData.postValue(false);
                            }
                        }
                    }
                });

    }
    public void clearViewedHotel(){
        CollectionReference searchRef = firebaseFirestore.collection("RecentSearchAndViewed").document(firebaseAuth.getUid()).collection("viewed");

        searchRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                WriteBatch batch = firebaseFirestore.batch();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    batch.delete(document.getReference());
                }
                batch.commit().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Log.d("TAG", "All documents in the collection have been deleted.");
                    } else {
                        Log.w("TAG", "Error deleting documents.", task1.getException());
                    }
                });
            } else {
                Log.w("TAG", "Error getting documents.", task.getException());
            }
        });
    }
    public void getViewedHotel(){
        CollectionReference searchRef = firebaseFirestore.collection("RecentSearchAndViewed").document(firebaseAuth.getUid()).collection("viewed");
        searchRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.w("REAL FAIL", "Listen failed.", error);
                return;
            }

            // Lấy danh sách các document từ query snapshot
            List<HotelModel> viewList = new ArrayList<>();
            for (QueryDocumentSnapshot document : value) {
                // Đọc thông tin từ document
                HotelModel hotelModel = document.toObject(HotelModel.class);
                viewList.add(hotelModel);
            }

            // Cập nhật dữ liệu lên MutableLiveData
            hotelModelMutableLiveData.postValue(viewList);
        });
    }

    public void getSearchRecentFlight(){
        CollectionReference searchRef = firebaseFirestore.collection("RecentSearchAndViewed").document(firebaseAuth.getUid()).collection("search");

        searchRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.w("REAL FAIL", "Listen failed.", error);
                return;
            }

            // Lấy danh sách các document từ query snapshot
            List<SearchRecentModel> recentList = new ArrayList<>();
            for (QueryDocumentSnapshot document : value) {
                // Đọc thông tin từ document
                SearchRecentModel search = document.toObject(SearchRecentModel.class);
                recentList.add(search);
            }

            // Cập nhật dữ liệu lên MutableLiveData
            searchRecentMutableLiveData.postValue(recentList);
        });
    }
    public void getFlightPassenger(String id){
        List<FlightReservationPassenger> flightReservationPassengers = new ArrayList<>();
        firebaseFirestore.collection("FlightReservationPassenger").whereEqualTo("flightReservation_id",id)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            FlightReservationPassenger flightReservationPassenger = documentSnapshot.toObject(FlightReservationPassenger.class);

                            firebaseFirestore.collection("Seats").document(flightReservationPassenger.getSeat_id())
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            SeatModel seatModel = documentSnapshot.toObject(SeatModel.class);
                                            flightReservationPassenger.setSeatModel(seatModel);
                                            flightReservationPassengers.add(flightReservationPassenger);
                                            if (flightReservationPassengers.size()==queryDocumentSnapshots.size()){
                                                flightReservationPassengerMutableLiveData.postValue(flightReservationPassengers);
                                            }
                                        }
                                    });

                        }
                    }
                });

    }
    public void clearAllRecent(){
        CollectionReference searchRef = firebaseFirestore.collection("RecentSearchAndViewed").document(firebaseAuth.getUid()).collection("search");

        searchRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                WriteBatch batch = firebaseFirestore.batch();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    batch.delete(document.getReference());
                }
                batch.commit().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Log.d("TAG", "All documents in the collection have been deleted.");
                    } else {
                        Log.w("TAG", "Error deleting documents.", task1.getException());
                    }
                });
            } else {
                Log.w("TAG", "Error getting documents.", task.getException());
            }
        });
    }

    public MutableLiveData<UserModel> getUserModelMutableLiveData() {
        return userModelMutableLiveData;
    }

    public void getUserDataFromFirestore() {
        firebaseFirestore.collection("User").document(firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    UserModel userModel = documentSnapshot.toObject(UserModel.class);

                    if (userModel != null) {
                        firebaseStorage.child("users/"+documentSnapshot.getId()+"/profile.jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful() && task.getResult() != null){
                                    Uri downloadUri = task.getResult();
                                    if (downloadUri!= null){
                                        String profileImage = downloadUri.toString();
                                        userModel.setProfileImage(profileImage);
                                        userModelMutableLiveData.postValue(userModel);
                                    }
                                }
                                else{
                                    userModelMutableLiveData.postValue(userModel);
                                }



                            }
                        });

                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi có lỗi xảy ra
                });
    }

    public void updateImageProfile(Uri uri){
        firebaseStorage.child("users/"+firebaseAuth.getCurrentUser().getUid()+"/profile.jpg")
                .putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        getUserDataFromFirestore();
                    }
                });
    }
    public void updateName(String name){
        firebaseFirestore.collection("User").document(firebaseAuth.getUid())
                .update("name",name)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        getUserDataFromFirestore();
                    }
                });
    }
    public void updateEmail(String email){
        firebaseFirestore.collection("User").document(firebaseAuth.getUid())
                .update("email",email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        firebaseAuth.getCurrentUser().updateEmail(email)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        getUserDataFromFirestore();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("LOI:",e.getMessage());
                                    }
                                });
                    }
                });



    }



}

