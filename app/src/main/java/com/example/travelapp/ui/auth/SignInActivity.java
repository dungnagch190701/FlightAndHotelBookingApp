package com.example.travelapp.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.travelapp.MainActivity;
import com.example.travelapp.databinding.ActivitySignInBinding;
import com.example.travelapp.viewmodel.AuthViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;
    String email,password;
    private AuthViewModel viewModel;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        viewModel = new ViewModelProvider(this , (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())).get(AuthViewModel.class);
        viewModel.getUserData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser != null){
                    Intent mActivity = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(mActivity);
                    finish();
                }
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.SignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.processBar.setVisibility(View.VISIBLE);
                email = String.valueOf(binding.EmailEdt.getText());
                password = String.valueOf(binding.PasswordEdt.getText());
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(SignInActivity.this, "Enter email!", Toast.LENGTH_SHORT).show();
                    binding.processBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(SignInActivity.this, "Enter password!", Toast.LENGTH_SHORT).show();
                    binding.processBar.setVisibility(View.GONE);
                    return;
                }
                viewModel.signIn(email , password);

//                mAuth.signInWithEmailAndPassword(email, password)
//                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()) {
//                                    binding.processBar.setVisibility(View.GONE);
//                                    Toast.makeText(SignInActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
//                                    Intent mActivity = new Intent(SignInActivity.this,MainActivity.class);
//                                    startActivity(mActivity);
//                                    finish();
//                                } else {
//                                    // If sign in fails, display a message to the user.
//                                    binding.processBar.setVisibility(View.GONE);
//                                    Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
            }
        });
        binding.gotoSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mActivity = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(mActivity);
                finish();
            }
        });
        binding.gotoForgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mActivity = new Intent(SignInActivity.this, ForgotPwdActivity.class);
                startActivity(mActivity);
            }
        });

    }
}