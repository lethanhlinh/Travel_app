package com.example.travel_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travel_app.R;
import com.example.travel_app.databinding.ActivityDetailBinding;
import com.example.travel_app.databinding.ActivityIntroBinding;
import com.example.travel_app.databinding.ActivitySigninBinding;
import com.google.android.material.tabs.TabLayout;

public class SignInActivity extends AppCompatActivity {

    private ActivitySigninBinding binding;
    //Khai báo user o day
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Dang nhap thanh cong chuyen huong den trang Intro
        binding.btnSignIn.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, IntroActivity.class);
            //intent.putExtra("user", user);
            startActivity(intent);
        });

        //Neu chua co tai khoan thi chuyen sang trang dang ky
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
