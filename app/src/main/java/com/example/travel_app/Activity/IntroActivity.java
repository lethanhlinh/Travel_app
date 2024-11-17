package com.example.travel_app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.travel_app.Domain.User;
import com.example.travel_app.databinding.ActivityIntroBinding;

public class IntroActivity extends BaseActivity {
    ActivityIntroBinding binding;
    private User userLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getIntentExtra();

        binding.introBtn.setOnClickListener(v -> {
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            intent.putExtra("user", userLogin);
            startActivity(intent);
        });
    }

    public void getIntentExtra(){
        userLogin = (User) getIntent().getParcelableExtra("user");
    }
}
