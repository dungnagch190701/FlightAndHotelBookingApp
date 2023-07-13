package com.example.travelapp.repository;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travelapp.model.CityModel;
import com.example.travelapp.model.FlightModel;
import com.example.travelapp.model.FlightReservation;
import com.example.travelapp.model.FlightReservationPassenger;
import com.example.travelapp.model.HotelModel;
import com.example.travelapp.model.SearchRecentModel;
import com.example.travelapp.model.SeatModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FlightsRepository {
    private Application application;
    private FirebaseFirestore firebaseFirestore;
    private MutableLiveData<List<SeatModel>> seatModelMutableLiveData;
    private MutableLiveData<List<FlightReservation>> flightReservationMutableLiveData;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;



    public MutableLiveData<List<SeatModel>> getSeatModelMutableLiveData() {
        return seatModelMutableLiveData;
    }

    public MutableLiveData<List<FlightReservation>> getFlightReservationMutableLiveData() {
        return flightReservationMutableLiveData;
    }

    public FlightsRepository(Application application) {
        this.application = application;
        firebaseFirestore = FirebaseFirestore.getInstance();
        seatModelMutableLiveData = new MutableLiveData<List<SeatModel>>();
        flightReservationMutableLiveData = new MutableLiveData<>();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
    }
    public void getMyFlightBooking(){
        firebaseFirestore.collection("FlightReservation").whereEqualTo("user_id",firebaseAuth.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<FlightReservation> flightReservations = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            FlightReservation flightReservation = documentSnapshot.toObject(FlightReservation.class);
                            flightReservation.setReservation_id(documentSnapshot.getId());
                            String flight_id = documentSnapshot.getString("flight_id");
                            firebaseFirestore.collection("Flights").document(flight_id)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot document = task.getResult();
                                            FlightModel flightModel = document.toObject(FlightModel.class);
                                            String origin_id = document.getString("origin");
                                            String destination_id = document.getString("destination");
                                            firebaseFirestore.collection("Cities")
                                                    .document(origin_id)
                                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            DocumentSnapshot originDocument = task.getResult();
                                                            CityModel origin = originDocument.toObject(CityModel.class);
                                                            firebaseFirestore.collection("Cities")
                                                                    .document(destination_id).get()
                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                            DocumentSnapshot destinationDocument = task.getResult();
                                                                            CityModel destination = destinationDocument.toObject(CityModel.class);
                                                                            flightModel.setOrigin_model(origin);
                                                                            flightModel.setDestination_model(destination);

                                                                            FirebaseStorage.getInstance().getReferenceFromUrl(flightModel.getLogo())
                                                                                    .getDownloadUrl()
                                                                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                        @Override
                                                                                        public void onSuccess(Uri uri) {
                                                                                            flightModel.setLogo(uri.toString());
                                                                                            flightReservation.setFlight(flightModel);
                                                                                            flightReservations.add(flightReservation);
                                                                                            if (flightReservations.size()==queryDocumentSnapshots.size()){
                                                                                                flightReservationMutableLiveData.postValue(flightReservations);
                                                                                            }
                                                                                        }
                                                                                    });

                                                                        }
                                                                    });
                                                        }
                                                    });
                                        }
                                    });
                        }
                    }
                });
    }

//    public void addSearchRecentFlight(SearchRecentModel searchRecentModel){
//        firebaseFirestore.collection("SearchRecentFlight").document(firebaseAuth.getUid()).collection("search")
//                .add(searchRecentModel)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d("success recent", "DocumentSnapshot successfully written!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("fail recent", "Error writing document", e);
//                    }
//                });
//
//    }
public void addSearchRecentFlight(SearchRecentModel searchRecentModel) {
    CollectionReference collectionReference = firebaseFirestore.collection("RecentSearchAndViewed")
            .document(firebaseAuth.getUid())
            .collection("search");

    // Tạo truy vấn để kiểm tra sự tồn tại của tài liệu
    Query query = collectionReference;

    // Thêm điều kiện truy vấn cho từng trường
    query = query.whereEqualTo("from_id", searchRecentModel.getFrom_id());
    query = query.whereEqualTo("to_id", searchRecentModel.getTo_id());
    query = query.whereEqualTo("city_from", searchRecentModel.getCity_from());
    query = query.whereEqualTo("city_to", searchRecentModel.getCity_to());
    query = query.whereEqualTo("passenger_number", searchRecentModel.getPassenger_number());
    query = query.whereEqualTo("seat_class", searchRecentModel.getSeat_class());
    query = query.whereEqualTo("date", searchRecentModel.getDate());
    query = query.whereEqualTo("end_date", searchRecentModel.getEnd_date());

    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    // Tài liệu đã tồn tại, không lưu nữa
                    Log.d("existing document", "Document already exists!");
                } else {
                    // Tài liệu chưa tồn tại, lưu vào Firestore
                    collectionReference.add(searchRecentModel)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("success recent", "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("fail recent", "Error writing document", e);
                                }
                            });
                }
            } else {
                Log.w("fail recent", "Error getting document", task.getException());
            }
        }
    });
}

    public void getSeat(String flight_id){
        firebaseFirestore.collection("Seats")
                .whereEqualTo("flight_id",flight_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot!=null){
                                List<SeatModel> list = new ArrayList<>();
                                for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                                    SeatModel seatModel = documentSnapshot.toObject(SeatModel.class);
                                    seatModel.setSeat_id(documentSnapshot.getId());
                                    list.add(seatModel);


                                }
                                Log.d("LIST", String.valueOf(list));
                                seatModelMutableLiveData.postValue(list);
                            }
                        }
                    }
                });
    }

    public void bookFlight(FlightReservation flightReservation, ArrayList<FlightReservationPassenger> flightReservationPassengers,String seat_class,int payment){
        firebaseFirestore.collection("FlightReservation").add(flightReservation.toMap(payment))
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){
                            String reservation_id = task.getResult().getId();
                            for (FlightReservationPassenger passenger : flightReservationPassengers){
                                passenger.setReservation_id(reservation_id);
                                firebaseFirestore.collection("FlightReservationPassenger").add(passenger.toMap())
                                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                Log.d("ADDPASSENGER", task.getResult().getId());
                                                String seat_id = passenger.getSeat_id();
                                                DocumentReference seatRef = firebaseFirestore.collection("Seats").document(seat_id);
                                                seatRef.update("status", "sold")
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Log.d("UPDATESUCCESS", "Seat status updated to sold");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.e("UPDATEFAIL", "Error updating seat status", e);
                                                            }
                                                        });
                                            }
                                        });
                            }
                        }
                    }
                });
        String flightId = flightReservation.getFlight().getFlight_id();
        int numSeatsToUpdate = flightReservation.getTotal_passenger();
        DocumentReference flightDocRef = firebaseFirestore.collection("Flights").document(flightId);

        if (seat_class.equals("Economy")) {
            flightDocRef.update("available_economy_seat", FieldValue.increment(-numSeatsToUpdate));
        } else if (seat_class.equals("Premium Economy")) {
            flightDocRef.update("available_premium_economy_seat", FieldValue.increment(-numSeatsToUpdate));
        } else if (seat_class.equals("Business")) {
            flightDocRef.update("available_business_seat", FieldValue.increment(-numSeatsToUpdate));
        } else if (seat_class.equals("First Class")) {
            flightDocRef.update("available_first_class_seat", FieldValue.increment(-numSeatsToUpdate));
        }




    }





    public LiveData<List<CityModel>> getCities() {
        MutableLiveData<List<CityModel>> citiesLiveData = new MutableLiveData<>();
        firebaseFirestore.collection("Cities")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<CityModel> cities = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String documentId = document.getId();
                            CityModel city = document.toObject(CityModel.class);
                            city.setCity_id(documentId);
                            cities.add(city);
                        }
                        citiesLiveData.postValue(cities);
                    } else {
                        Log.e("ERROR:", "Error getting cities: ", task.getException());
                    }
                });
        return citiesLiveData;
    }

    public LiveData<List<FlightModel>> searchFlight(String origin,String destination,int passenger,String seatClass,long departure_date,long end_date){
        MutableLiveData<List<FlightModel>> searchFlightLiveData = new MutableLiveData<>();
        Date date = new Date(departure_date);
        Date date_end = new Date(end_date);
        Timestamp timestamp = new Timestamp(date);
        Timestamp timestampEnd = new Timestamp(date_end);
        CollectionReference flightsRef = firebaseFirestore.collection("Flights");
        Query query = flightsRef
                .whereEqualTo("origin", origin)
                .whereEqualTo("destination", destination)
                .whereGreaterThanOrEqualTo("departure_time", timestamp)
                .whereLessThanOrEqualTo("departure_time", timestampEnd);


        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("TASK:","SUCCESS");
                    List<FlightModel> flightModels = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Lấy dữ liệu từ Firestore document
                        String flightId = document.getId();
                        String airline = document.getString("airline");
                        String flightNumber = document.getString("flight_number");
                        String originId = document.getString("origin");
                        String destinationId = document.getString("destination");
                        Timestamp departureTime = document.getTimestamp("departure_time");
                        Date departureDate = departureTime != null ? departureTime.toDate() : null; // Đổi Timestamp về dạng Date
                        Timestamp arrivalTime = document.getTimestamp("arrival_time");
                        Date arrivalDate = arrivalTime != null ? arrivalTime.toDate() : null;
                        String logo = document.getString("logo");
                        float economyPrice = document.getDouble("economy_price").floatValue();
                        float premiumEconomyPrice = document.getDouble("premium_economy_price").floatValue();
                        float businessPrice = document.getDouble("business_price").floatValue();
                        float firstClassPrice = document.getDouble("first_class_price").floatValue();
                        int availableEconomySeat = document.getLong("available_economy_seat").intValue();
                        int availablePremiumEconomySeat = document.getLong("available_premium_economy_seat").intValue();
                        int availableBusinessSeat = document.getLong("available_business_seat").intValue();
                        int availableFirstClassSeat = document.getLong("available_first_class_seat").intValue();
                        if ("Economy".equals(seatClass) && availableEconomySeat >= passenger) {

                        } else if ("Premium Economy".equals(seatClass) && availablePremiumEconomySeat >= passenger) {

                        } else if ("Business".equals(seatClass) && availableBusinessSeat >= passenger) {

                        } else if ("First Class".equals(seatClass) && availableFirstClassSeat >= passenger) {

                        } else {

                            continue;
                        }

                        DocumentReference originRef = firebaseFirestore.collection("Cities").document(originId);
                        originRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> originTask) {
                                if (originTask.isSuccessful()) {
                                    Log.d("TASK2:","ORIGIN SUCCESS");
                                    DocumentSnapshot originDocument = originTask.getResult();
                                    CityModel origin = originDocument.toObject(CityModel.class);
                                    DocumentReference destinationRef = firebaseFirestore.collection("Cities").document(destinationId);
                                    destinationRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> destinationTask) {
                                            DocumentSnapshot destinationDocument = destinationTask.getResult();
                                            CityModel destination = destinationDocument.toObject(CityModel.class);
                                            FlightModel flightModel = new FlightModel(flightId,airline, flightNumber, departureDate, arrivalDate, origin, destination, economyPrice, premiumEconomyPrice, businessPrice,
                                                    firstClassPrice,availableEconomySeat,availablePremiumEconomySeat,availableBusinessSeat,availableFirstClassSeat
                                                    );

                                            firebaseStorage.getReferenceFromUrl(logo).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    String img = uri.toString();
                                                    flightModel.setLogo(img);
                                                    flightModels.add(flightModel);
                                                    if (flightModels.size() == task.getResult().size()) {
                                                        searchFlightLiveData.postValue(flightModels);
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
            }
        });
        return searchFlightLiveData;
    }



}
