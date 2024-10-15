package com.example.travel_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travel_app.R;
import com.example.travel_app.databinding.ActivitySignupBinding;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    //Khai bao lop user de luu du lieu vao firebase

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        setVariable();
    }

    private void setVariable() {
        binding.btnSignUp.setOnClickListener(view -> {
            //Xu ly du lieu dua vao firebase va chuyen sang trang login

            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
        });

        //Neu da co tk thi quay lai trang dang nhap
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getIntentExtra() {
        //user.hoTen = binding.hoTenTxt.getText();
    }
}
