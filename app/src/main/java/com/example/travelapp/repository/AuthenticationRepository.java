package com.example.travelapp.repository;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.travelapp.model.Destination;
import com.example.travelapp.model.HotelModel;
import com.example.travelapp.model.UserModel;
import com.example.travelapp.ui.auth.SignInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationRepository {
    private Application application;
    private MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    private MutableLiveData<Boolean> userLoggedMutableLiveData;
    private MutableLiveData<UserModel> userFStoreMutableLiveData;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference firebaseStorage;
    private MutableLiveData<List<HotelModel>> highRatingHotelMutableLiveData;
    private MutableLiveData<List<HotelModel>> highReviewHotelMutableLiveData;

    public MutableLiveData<List<HotelModel>> getHighReviewHotelMutableLiveData() {
        return highReviewHotelMutableLiveData;
    }

    public MutableLiveData<List<HotelModel>> getHighRatingHotelMutableLiveData() {
        return highRatingHotelMutableLiveData;
    }

    public MutableLiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        return firebaseUserMutableLiveData;
    }

    public MutableLiveData<Boolean> getUserLoggedMutableLiveData() {
        return userLoggedMutableLiveData;
    }

    public MutableLiveData<UserModel> getUserFStoreMutableLiveData() {
        return userFStoreMutableLiveData;
    }

    public AuthenticationRepository(Application application){
        this.application = application;
        firebaseUserMutableLiveData = new MutableLiveData<>();
        userLoggedMutableLiveData = new MutableLiveData<>();
        userFStoreMutableLiveData = new MutableLiveData<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance().getReference();

        if (auth.getCurrentUser() != null){
            firebaseUserMutableLiveData.postValue(auth.getCurrentUser());
        }
        highRatingHotelMutableLiveData = new MutableLiveData<>();
        highReviewHotelMutableLiveData = new MutableLiveData<>();
    }
    public void getHighRatingHotel(){
        Query query = firebaseFirestore.collection("Hotels")
                .orderBy("rating", Query.Direction.DESCENDING)
                .limit(5);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> taskOne) {
                if (taskOne.isSuccessful()) {
                    List<HotelModel> hotelModels = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : taskOne.getResult()) {
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
                                                            if (hotelModels.size() == taskOne.getResult().size()){
                                                                highRatingHotelMutableLiveData.postValue(hotelModels);
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
        });
    }
    public void getHighReview(){
        Query query = firebaseFirestore.collection("Hotels")
                .orderBy("review_avg", Query.Direction.DESCENDING)
                .limit(5);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> taskOne) {
                if (taskOne.isSuccessful()) {
                    List<HotelModel> hotelModels = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : taskOne.getResult()) {
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
                                                            if (hotelModels.size() == taskOne.getResult().size()){
                                                                highReviewHotelMutableLiveData.postValue(hotelModels);
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
        });
    }
    public void register(String email , String pass,String name,String phone){
        UserModel user = new UserModel(name,phone,email);
        auth.createUserWithEmailAndPassword(email , pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull  Task<AuthResult> task) {
                if (task.isSuccessful()){

                    firebaseFirestore.collection("User")
                            .document(auth.getUid())
                            .set(user.toMap())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(application, "OK", Toast.LENGTH_SHORT).show();
                                            signOut();
                                        }
                                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(application, "Failed ne", Toast.LENGTH_SHORT).show();
                                }
                            });




                }else{
                    Toast.makeText(application, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void login(String email , String pass){
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    firebaseUserMutableLiveData.postValue(auth.getCurrentUser());
                    Toast.makeText(application, "Login Success", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(application, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void signOut(){
        auth.signOut();
        userLoggedMutableLiveData.postValue(true);
    }

    public void getUserDataFromFirestore() {
        firebaseFirestore.collection("User").document(auth.getCurrentUser().getUid())
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
                                        userFStoreMutableLiveData.postValue(userModel);
                                    }
                                    else{
                                        userFStoreMutableLiveData.postValue(userModel);
                                    }
                                }
                                else{
                                    userFStoreMutableLiveData.postValue(userModel);
                                }


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                userFStoreMutableLiveData.postValue(userModel);
                            }
                        });

                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi có lỗi xảy ra
                });
    }


}
