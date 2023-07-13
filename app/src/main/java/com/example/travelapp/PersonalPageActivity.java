package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.travelapp.databinding.ActivityPersonalPageBinding;
import com.example.travelapp.model.UserModel;
import com.example.travelapp.ui.fragment.EditProfileFragment;
import com.example.travelapp.ui.fragment.HomeFragment;
import com.example.travelapp.viewmodel.AuthViewModel;
import com.example.travelapp.viewmodel.UserViewModel;
import com.google.firebase.firestore.auth.User;

public class PersonalPageActivity extends AppCompatActivity {
    ActivityPersonalPageBinding binding;
    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPersonalPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        EditProfileFragment editProfileFragment = new EditProfileFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.personal_page,editProfileFragment);
        fragmentTransaction.commit();





    }

}