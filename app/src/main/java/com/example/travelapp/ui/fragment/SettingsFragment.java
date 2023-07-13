package com.example.travelapp.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.travelapp.MainActivity;
import com.example.travelapp.PersonalPageActivity;
import com.example.travelapp.R;
import com.example.travelapp.databinding.FragmentSettingsBinding;
import com.example.travelapp.model.UserModel;
import com.example.travelapp.ui.auth.SignInActivity;
import com.example.travelapp.viewmodel.AuthViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;


public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;
    private AuthViewModel viewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this , (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(AuthViewModel.class);
        viewModel.getLoggedStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    Intent mActivity = new Intent(getActivity(), SignInActivity.class);
                    startActivity(mActivity);
                    getActivity().finish();
                }
            }
        });



    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.processBar.setVisibility(View.VISIBLE);
        binding.avatar.setVisibility(View.GONE);
        binding.email.setVisibility(View.GONE);
        binding.phone.setVisibility(View.GONE);
        viewModel.getUserDataFromFirestore();
        viewModel.getUserFStore().observe(getViewLifecycleOwner(), new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                if (userModel != null) {
                    // Cập nhật giao diện người dùng với dữ liệu mới
                    binding.name.setText(userModel.getName());
                    binding.email.setText(userModel.getEmail());
                    binding.phone.setText(userModel.getPhone());
                    Glide.with(requireContext()).load(userModel.getProfileImage()).error(R.drawable.no_avatar).into(binding.avatar);
                    binding.processBar.setVisibility(View.GONE);
                    binding.avatar.setVisibility(View.VISIBLE);
                    binding.email.setVisibility(View.VISIBLE);
                    binding.phone.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.accountInf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PersonalPageActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.signOut();
            }
        });
    }
}