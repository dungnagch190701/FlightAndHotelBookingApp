package com.example.travelapp.repository;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travelapp.model.CityModel;
import com.example.travelapp.model.Destination;
import com.example.travelapp.model.FacilitiesModel;
import com.example.travelapp.model.HotelModel;
import com.example.travelapp.model.HotelReservationModel;
import com.example.travelapp.model.ReviewHotelModel;
import com.example.travelapp.model.RoomTypeModel;
import com.example.travelapp.model.SearchRecentModel;
import com.example.travelapp.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class HotelRepository {
    private MutableLiveData<ArrayList<HotelModel>> hotelModelMutableLiveData;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference firebaseStorage;
    private MutableLiveData<List<ReviewHotelModel>> reviewRecentModelMutableLiveData;
    private MutableLiveData<List<FacilitiesModel>> facilitiesModelMutableLiveData;
    private MutableLiveData<List<RoomTypeModel>> roomTypeMutableLiveData;
    private MutableLiveData<List<Destination>> destinationMutableLiveData;
    private MutableLiveData<List<HotelReservationModel>> hotelReservationMutableLiveData;
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<Boolean> isSaved;
    private MutableLiveData<List<HotelModel>> hotelSavedMutableLiveData;


    public HotelRepository() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance().getReference();
        hotelModelMutableLiveData = new MutableLiveData<ArrayList<HotelModel>>();
        reviewRecentModelMutableLiveData = new MutableLiveData<>();
        facilitiesModelMutableLiveData = new MutableLiveData<>();
        roomTypeMutableLiveData = new MutableLiveData<>();
        destinationMutableLiveData = new MutableLiveData<>();
        hotelReservationMutableLiveData=  new MutableLiveData<>();
        firebaseAuth = FirebaseAuth.getInstance();
        isSaved= new MutableLiveData<>();
        hotelSavedMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<HotelModel>> getHotelSavedMutableLiveData() {
        return hotelSavedMutableLiveData;
    }

    public MutableLiveData<List<HotelReservationModel>> getHotelReservationMutableLiveData() {
        return hotelReservationMutableLiveData;
    }

    public MutableLiveData<Boolean> getIsSaved() {
        return isSaved;
    }



    public MutableLiveData<List<Destination>> getDestinationMutableLiveData() {
        return destinationMutableLiveData;
    }

    public MutableLiveData<List<RoomTypeModel>> getRoomTypeMutableLiveData() {
        return roomTypeMutableLiveData;
    }

    public MutableLiveData<List<FacilitiesModel>> getFacilitiesModelMutableLiveData() {
        return facilitiesModelMutableLiveData;
    }

    public MutableLiveData<ArrayList<HotelModel>> getHotelModelMutableLiveData() {
        return hotelModelMutableLiveData;
    }

    public MutableLiveData<List<ReviewHotelModel>> getRecentReviewModelMutableLiveData() {
        return reviewRecentModelMutableLiveData;
    }

    public void addViewedHotel(HotelModel hotelModel){
        CollectionReference collectionReference = firebaseFirestore.collection("RecentSearchAndViewed")
                .document(firebaseAuth.getUid())
                .collection("viewed");
        Query query = collectionReference;
        query = query.whereEqualTo("hotel_id", hotelModel.getHotel_id());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        // Tài liệu đã tồn tại, không lưu nữa
                        Log.d("existing hotel", "Document already exists!");
                    } else {
                        // Tài liệu chưa tồn tại, lưu vào Firestore
                        collectionReference.add(hotelModel)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d("success hotel", "DocumentSnapshot successfully written!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("fail hotel", "Error writing document", e);
                                    }
                                });
                    }
                } else {
                    Log.w("fail recent", "Error getting document", task.getException());
                }
            }
        });
    }
    public void getHotelSaved(){

        firebaseFirestore.collection("User").document(firebaseAuth.getUid())
                .collection("saved")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("REAL FAIL", "Listen failed.", error);
                            return;
                        }
                        List<HotelModel> hotelModels = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot: value){
                            HotelModel hotelModel = queryDocumentSnapshot.toObject(HotelModel.class);
                            Map<String, Object> locationMap = (Map<String, Object>) queryDocumentSnapshot.get("location");
                            Destination destination = new Destination();
                            destination.setCity((String) locationMap.get("city"));
                            destination.setCountry((String) locationMap.get("country"));
                            hotelModel.setLocationModel(destination);
                            hotelModels.add(hotelModel);
                        }
                        hotelSavedMutableLiveData.postValue(hotelModels);
                    }
                });
//                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        for (QueryDocumentSnapshot queryDocumentSnapshot: queryDocumentSnapshots){
//                            HotelModel hotelModel = queryDocumentSnapshot.toObject(HotelModel.class);
//                            Map<String, Object> locationMap = (Map<String, Object>) queryDocumentSnapshot.get("location");
//                            Destination destination = new Destination();
//                            destination.setCity((String) locationMap.get("city"));
//                            destination.setCountry((String) locationMap.get("country"));
//                            hotelModel.setLocationModel(destination);
//                            hotelModels.add(hotelModel);
//                        }
//                        hotelSavedMutableLiveData.postValue(hotelModels);
//                    }
//                });
    }
    public void setIsSaved(String hotel_id){
        firebaseFirestore.collection("User").document(firebaseAuth.getUid())
                .collection("saved")
                .document(hotel_id)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("TAG", "Listen failed.", error);
                            return;
                        }
                        if (value != null && value.exists()){
                            isSaved.postValue(true);
                        }else {
                            isSaved.postValue(false);
                        }

                    }
                });
    }
    public void saveHotel(HotelModel hotelModel){
        firebaseFirestore.collection("User").document(firebaseAuth.getUid())
                .collection("saved").document(hotelModel.getHotel_id())
                .set(hotelModel.toMap());
    }
    public void unSaveHotel(String id){
        firebaseFirestore.collection("User").document(firebaseAuth.getUid())
                .collection("saved").document(id)
                .delete();
    }

    public void getHotelBooking(){

        firebaseFirestore.collection("HotelReservations").whereEqualTo("user_id",firebaseAuth.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<HotelReservationModel> hotelReservationModels = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            HotelReservationModel hotelReservationModel = documentSnapshot.toObject(HotelReservationModel.class);
                            String hotel_id = documentSnapshot.getString("hotel_id");
                            String room_type_id = documentSnapshot.getString("room_type_id");
                            firebaseFirestore.collection("Hotels").document(hotel_id)
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            HotelModel hotelModel = documentSnapshot.toObject(HotelModel.class);
                                            hotelModel.setHotel_id(documentSnapshot.getId());
                                            String tag = documentSnapshot.getString("type");
                                            hotelModel.setTag(tag);
                                            String destination_id = documentSnapshot.getString("location");
                                            StorageReference hotelImagesRef = firebaseStorage.child("hotel-images/"+documentSnapshot.getId());
                                            hotelImagesRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                                                @Override
                                                public void onSuccess(ListResult listResult) {
                                                    ArrayList<String> imageUrls = new ArrayList<>();
                                                    for (StorageReference item : listResult.getItems()){
                                                        item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                String imageUrl = uri.toString();
                                                                imageUrls.add(imageUrl);
                                                                if (imageUrls.size() == listResult.getItems().size()) {
                                                                    hotelModel.setPhotoURL(imageUrls);
                                                                    firebaseFirestore.collection("Destinations").document(destination_id)
                                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                            Destination destination = documentSnapshot.toObject(Destination.class);
                                                            hotelModel.setLocationModel(destination);
                                                            firebaseFirestore.collection("Room_Types").document(room_type_id)
                                                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                            RoomTypeModel roomTypeModel = documentSnapshot.toObject(RoomTypeModel.class);
                                                                            hotelReservationModel.setHotelModel(hotelModel);
                                                                            hotelReservationModel.setRoomTypeModel(roomTypeModel);
                                                                            hotelReservationModels.add(hotelReservationModel);

                                                                            if (hotelReservationModels.size()==queryDocumentSnapshots.size()){
                                                                                hotelReservationMutableLiveData.postValue(hotelReservationModels);
                                                                            }
                                                                        }
                                                                    });
                                                        }
                                                    });

                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });
//
//                                            firebaseFirestore.collection("Destinations").document(destination_id)
//                                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                                        @Override
//                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                                            Destination destination = documentSnapshot.toObject(Destination.class);
//                                                            hotelModel.setLocationModel(destination);
//                                                            firebaseFirestore.collection("Room_Types").document(room_type_id)
//                                                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                                                        @Override
//                                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                                                            RoomTypeModel roomTypeModel = documentSnapshot.toObject(RoomTypeModel.class);
//                                                                            hotelReservationModel.setHotelModel(hotelModel);
//                                                                            hotelReservationModel.setRoomTypeModel(roomTypeModel);
//                                                                            hotelReservationModels.add(hotelReservationModel);
//
//                                                                            if (hotelReservationModels.size()==queryDocumentSnapshots.size()){
//                                                                                hotelReservationMutableLiveData.postValue(hotelReservationModels);
//                                                                            }
//                                                                        }
//                                                                    });
//                                                        }
//                                                    });
                                        }
                                    });
                        }
                    }
                });
    }

    public void bookHotel(String user_id,String hotel_id,String room_type_id,Date check_in_date,Date check_out_date,int total_passenger,int total_amount,int payment){
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("user_id", user_id);
        reservation.put("hotel_id", hotel_id);
        reservation.put("room_type_id", room_type_id);
        reservation.put("check_in_date", check_in_date);
        reservation.put("check_out_date", check_out_date);
        reservation.put("total_passenger", total_passenger);
        reservation.put("total_amount", total_amount);
        reservation.put("payment", payment);
        firebaseFirestore.collection("HotelReservations")
                .add(reservation)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("BOOK HOTEL", "DocumentSnapshot added with ID: " + documentReference.getId());
                        firebaseFirestore.collection("RoomAvailability")
                                .whereEqualTo("hotel_id", hotel_id)
                                .whereEqualTo("room_type_id", room_type_id)
                                .whereGreaterThanOrEqualTo("date", check_in_date)
                                .whereLessThanOrEqualTo("date", check_out_date)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                int available_room = document.getLong("available_room").intValue();
                                                if (available_room > 0) {
                                                    String docId = document.getId();
                                                    firebaseFirestore.collection("RoomAvailability").document(docId)
                                                            .update("available_room", available_room - 1)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Log.d("UPDATE ROOM AVAILABILITY", "DocumentSnapshot successfully updated!");
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.w("UPDATE ROOM AVAILABILITY", "Error updating document", e);
                                                                }
                                                            });
                                                }
                                            }
                                        } else {
                                            Log.d("BOOK HOTEL", "Error getting documents: ", task.getException());
                                        }
                                    }
                                });

                    }
                });

    }
    public void getDestination(){
        firebaseFirestore.collection("Destinations")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null) {
                            List<Destination> destinationList = new ArrayList<>();
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Destination destination = documentSnapshot.toObject(Destination.class);
                                destination.setDestination_id(documentSnapshot.getId());
                                destinationList.add(destination);
                            }
                            destinationMutableLiveData.postValue(destinationList);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("DESTINATION FAIL", "Error getting documents.", e);
                    }
                });
    }

    public void getRoomType(String hotel_id,Date check_in_date,Date check_out_date,int room,int passenger){
        List<RoomTypeModel> roomTypeModels = new ArrayList<>();
        CollectionReference roomTypeRef = firebaseFirestore.collection("Room_Types");
        List<String> roomTypeId = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(check_in_date);
        calendar.add(Calendar.HOUR_OF_DAY, -7);
        Date new_check_in_date = calendar.getTime();
        calendar.setTime(check_out_date);
// Giảm 7 tiếng cho check_out_date
        calendar.add(Calendar.HOUR_OF_DAY, -7);
// Lấy giá trị mới cho check_out_date
        Date new_check_out_date = calendar.getTime();
         firebaseFirestore.collection("RoomAvailability")
                .whereGreaterThanOrEqualTo("date", new_check_in_date)
                .whereLessThanOrEqualTo("date", new_check_out_date)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot documentSnapshot : task.getResult()){
                            long avai = documentSnapshot.getLong("available_room");
                            int available_room = (int) avai;
                            if (available_room>=room){
                                String roomTypeIdToAdd = documentSnapshot.getString("room_type_id");
                                if (!roomTypeId.contains(roomTypeIdToAdd)) {
                                    roomTypeId.add(roomTypeIdToAdd);
                                }
                            }
                        }
                        roomTypeRef.whereEqualTo("hotel_id",hotel_id)
                                .whereIn(FieldPath.documentId(),roomTypeId)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){
                                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                                long max_per = documentSnapshot.getLong("guest_per_room");
                                                int max_per_int = (int) max_per;
                                                int maxPassenger = max_per_int*room;
                                                if (passenger <= maxPassenger){
                                                    RoomTypeModel roomTypeModel = documentSnapshot.toObject(RoomTypeModel.class);
                                                    roomTypeModel.setRoom_type_id(documentSnapshot.getId());
                                                    List<String> downloadUrls = new ArrayList<>();
                                                    List<String> photoUrls = roomTypeModel.getPhoto_url();
                                                    for (String url : photoUrls){
                                                        FirebaseStorage.getInstance().getReferenceFromUrl(url)
                                                                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                    @Override
                                                                    public void onSuccess(Uri uri) {
                                                                        String downloadUrl = uri.toString();
                                                                        downloadUrls.add(downloadUrl);
                                                                        if (downloadUrls.size() == (photoUrls.size())) {
                                                                            roomTypeModel.setPhoto_url(downloadUrls);
                                                                            roomTypeModels.add(roomTypeModel);
                                                                            if (roomTypeModels.size() == task.getResult().size()) {
                                                                                roomTypeMutableLiveData.postValue(roomTypeModels);
                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                    }

                                                }
                                            }

                                        }
                                    }
                                });

                    }
                });



    }
    public void getFacilities(String hotel_id){
        List<FacilitiesModel> facilitiesModels = new ArrayList<>();
        CollectionReference facilitiesRef = firebaseFirestore.collection("HotelFacilities");
        CollectionReference facRef = firebaseFirestore.collection("Facilities");
        facilitiesRef.whereEqualTo("hotel_id",hotel_id)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                String facId = documentSnapshot.getString("facilities_id");
                                facRef.document(facId)
                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                FacilitiesModel facilitiesModel = documentSnapshot.toObject(FacilitiesModel.class);
                                                facilitiesModels.add(facilitiesModel);
                                                if (facilitiesModels.size() == task.getResult().size()) {
                                                    facilitiesModelMutableLiveData.postValue(facilitiesModels);
                                                }
                                            }
                                        });
                            }

                        }
                    }
                });
    }
    public void getRecentReview(String hotel_id){
        CollectionReference reviewRef = firebaseFirestore.collection("HotelReview");
        Query query = reviewRef
                .whereEqualTo("hotel_id",hotel_id)
                .orderBy("date", Query.Direction.DESCENDING);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<ReviewHotelModel> reviewList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ReviewHotelModel review = new ReviewHotelModel();
                        String comment = document.getString("comment");
                        long rating = document.getLong("rating");
                        int rating_int = (int) rating;
                        Timestamp timestamp = document.getTimestamp("date");
                        Date date = timestamp.toDate();
                        review.setComment(comment);
                        review.setRating(rating_int);
                        review.setDate(date);
                        String user_id = document.getString("user_id");
                        firebaseFirestore.collection("User").document(user_id)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        UserModel userModel = documentSnapshot.toObject(UserModel.class);
                                        firebaseStorage.child("users/"+documentSnapshot.getId()+"/profile.jpg").getDownloadUrl()
                                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String profileImage = uri.toString();
                                                        userModel.setProfileImage(profileImage);
                                                        review.setUser_id(userModel);
                                                        reviewList.add(review);
                                                        if (reviewList.size() == task.getResult().size()) {
                                                            reviewRecentModelMutableLiveData.postValue(reviewList);
                                                        }
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        review.setUser_id(userModel);
                                                        reviewList.add(review);
                                                        if (reviewList.size() == task.getResult().size()) {
                                                            reviewRecentModelMutableLiveData.postValue(reviewList);
                                                        }
                                                    }
                                                });
                                    }
                                });
                    }


                }
            }
        });

    }

    public void getHotel(String destination, Date check_in_date, int room, Date check_out_date){
        ArrayList<String> hotelId = new ArrayList<String>();
        ArrayList<HotelModel> hotelModels = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(check_in_date);
        calendar.add(Calendar.HOUR_OF_DAY, -7);
        Date new_check_in_date = calendar.getTime();
        calendar.setTime(check_out_date);
// Giảm 7 tiếng cho check_out_date
        calendar.add(Calendar.HOUR_OF_DAY, -7);
// Lấy giá trị mới cho check_out_date
        Date new_check_out_date = calendar.getTime();




        CollectionReference hotelsRef = firebaseFirestore.collection("RoomAvailability");
        Query query = hotelsRef.whereGreaterThanOrEqualTo("date", new_check_in_date)
                .whereLessThanOrEqualTo("date", new_check_out_date);

                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty() ){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                long available_room = documentSnapshot.getLong("available_room");

                                int int_room = (int) available_room;
                                if (int_room>=room){
                                    String idHotel = documentSnapshot.getString("hotel_id");
                                    if (!hotelId.contains(idHotel)) {
                                        hotelId.add(idHotel);
                                    }
                                }
                            }
                            firebaseFirestore.collection("Hotels").whereIn(FieldPath.documentId(),hotelId).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> taskOne) {
                                            if (taskOne.isSuccessful()){
                                                for (QueryDocumentSnapshot documentSnapshot : taskOne.getResult()){
                                                    String location = documentSnapshot.getString("location");
                                                    if (location.equals(destination)){
                                                        String address  = documentSnapshot.getString("address");
                                                        String name = documentSnapshot.getString("name");
                                                        String description = documentSnapshot.getString("description");
                                                        long rating = documentSnapshot.getLong("rating");
                                                        int rating_int = (int) rating;
                                                        double review_avg = documentSnapshot.getDouble("review_avg");
                                                        float review_avg_float = (float) review_avg;
                                                        long total_review = documentSnapshot.getLong("total_review");
                                                        int total_review_int = (int) total_review;
                                                        long price_start_from = documentSnapshot.getLong("start_from");
                                                        int price_start_from_int = (int) price_start_from;
                                                        String tag = documentSnapshot.getString("type");
                                                        HotelModel hotelObj = new HotelModel(documentSnapshot.getId(),name,address,null,null,rating_int,description,review_avg_float,total_review_int,price_start_from_int,tag);
                                                        firebaseFirestore.collection("Destinations").document(documentSnapshot.getString("location"))
                                                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                        DocumentSnapshot descDocument = task.getResult();
                                                                        Destination destination1 = descDocument.toObject(Destination.class);
                                                                        hotelObj.setLocationModel(destination1);
                                                                        StorageReference hotelImagesRef = firebaseStorage.child("hotel-images/"+documentSnapshot.getId());
                                                                        hotelImagesRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                                                                            @Override
                                                                            public void onSuccess(ListResult listResult) {
                                                                                ArrayList<String> imageUrls = new ArrayList<>();
                                                                                for (StorageReference item : listResult.getItems()){
                                                                                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                        @Override
                                                                                        public void onSuccess(Uri uri) {
                                                                                            String imageUrl = uri.toString();
                                                                                            imageUrls.add(imageUrl);
//
                                                                                            if (imageUrls.size() == listResult.getItems().size()) {
                                                                                                hotelObj.setPhotoURL(imageUrls);
                                                                                                hotelModels.add(hotelObj);
                                                                                            }
                                                                                            if (hotelModels.size() == hotelId.size()){
                                                                                                System.out.println(hotelModels);
                                                                                                hotelModelMutableLiveData.postValue(hotelModels);
                                                                                            }

                                                                                        }
                                                                                    });
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                    }
                                                }

                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}
