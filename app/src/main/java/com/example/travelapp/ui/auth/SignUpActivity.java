package com.example.travelapp.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.travelapp.MainActivity;
import com.example.travelapp.databinding.ActivitySignUpBinding;
import com.example.travelapp.model.UserModel;
import com.example.travelapp.viewmodel.AuthViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    String name,email,phone,password,confirmPassword;
    private AuthViewModel viewModel;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        viewModel = new ViewModelProvider(this , (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())).get(AuthViewModel.class);
        viewModel.getLoggedStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    Intent mActivity = new Intent(SignUpActivity.this, SignInActivity.class);
                    startActivity(mActivity);
                    finish();
                }
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        binding.SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = String.valueOf(binding.NameEdt.getText());
                email = String.valueOf(binding.EmailEdt.getText());
                phone = String.valueOf(binding.PhoneEdt.getText());
                password = String.valueOf(binding.PasswordEdt.getText());
                confirmPassword = String.valueOf(binding.ConfirmPasswordEdt.getText());

                if (password.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Password Empty", Toast.LENGTH_SHORT).show();
                    return;
                }  if (password.length()<6) {
                    Toast.makeText(SignUpActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this, "Password Would Not be matched", Toast.LENGTH_SHORT).show();
                    return;
                }
                viewModel.signUp(email,password,name,phone);

            }
        });
        binding.gotoSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mActivity = new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(mActivity);
                finish();
            }
        });


    }

}