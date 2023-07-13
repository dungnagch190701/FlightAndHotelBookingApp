package com.example.travelapp.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.travelapp.MainActivity;
import com.example.travelapp.R;
import com.example.travelapp.databinding.BotUpdateEmailBinding;
import com.example.travelapp.databinding.FragmentEditProfileBinding;
import com.example.travelapp.databinding.UserRenameBinding;
import com.example.travelapp.model.UserModel;
import com.example.travelapp.viewmodel.AuthViewModel;
import com.example.travelapp.viewmodel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class EditProfileFragment extends Fragment {

    StorageReference storageReference;
    FragmentEditProfileBinding binding;
    UserViewModel userViewModel;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(getLayoutInflater(),container, false);
        View view =  binding.getRoot();
        storageReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        Task usertask = auth.getCurrentUser().reload();
        usertask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                if (auth.getCurrentUser().isEmailVerified()){
                    binding.confirmBtn.setVisibility(View.GONE);
                    binding.confirmWarning.setVisibility(View.GONE);
                }
                else{
                    binding.confirmBtn.setVisibility(View.VISIBLE);
                    binding.confirmWarning.setVisibility(View.VISIBLE);
                }
            }


        });

        binding.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(requireContext(), "Email verification sent! Please check your email.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        userViewModel.getUserModelMutableLiveData().observe(getViewLifecycleOwner(), new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                Glide.with(requireContext()).load(userModel.getProfileImage()).error(R.drawable.no_avatar).into(binding.avatarImg);
                binding.name.setText(userModel.getName());
                binding.email.setText(userModel.getEmail());

            }
        });
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(),MainActivity.class);
                startActivity(intent);

            }
        });
        binding.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1000);
            }
        });
        binding.cardViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRenameBottom();
            }
        });
        binding.emailCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUpdateEmailBottom();
            }
        });


    }

    private void openUpdateEmailBottom() {
        BotUpdateEmailBinding emailBinding;
        emailBinding = BotUpdateEmailBinding.inflate(getLayoutInflater());
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(emailBinding.getRoot());
        bottomSheetDialog.show();
        emailBinding.emailEdt.setText(binding.email.getText().toString());
        emailBinding.saveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailBinding.emailEdt.getText().toString();
                if (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    userViewModel.updateEmail(email);
                    bottomSheetDialog.dismiss();;
                }else{
                    Toast.makeText(requireContext(), "Please enter valid email!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openRenameBottom() {
        UserRenameBinding bindingRename;
        bindingRename = UserRenameBinding.inflate(getLayoutInflater());
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(bindingRename.getRoot());
        bottomSheetDialog.show();
        bindingRename.nameEdt.setText(binding.name.getText().toString());
        bindingRename.saveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = bindingRename.nameEdt.getText().toString();
                if (!TextUtils.isEmpty(name)){
                    userViewModel.updateName(name);
                    bottomSheetDialog.dismiss();
                }
                else {
                    Toast.makeText(requireContext(), "Please enter the name!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000){
            if (resultCode == Activity.RESULT_OK){
                Uri imageURI = data.getData();
                userViewModel.updateImageProfile(imageURI);
            }
        }
    }
}