package com.example.travel_app.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.travel_app.Domain.User;
import com.example.travel_app.databinding.ActivityIntroBinding;

public class IntroActivity extends BaseActivity {
    ActivityIntroBinding binding;
    private User userLogin;
    private String userKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Gọi getIntentExtra() trong onCreate() để đảm bảo userLogin được cập nhật
        getIntentExtra();

        binding.introBtn.setOnClickListener(v -> {
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            // Truyền userLogin sang MainActivity
            intent.putExtra("user", userLogin);
            intent.putExtra("userKey", userKey); // Truyền userKey sang MainActivity)
            startActivity(intent);
        });
    }

    public void getIntentExtra(){
        userLogin = (User) getIntent().getSerializableExtra("user");
        userKey = getIntent().getStringExtra("userKey");
    }
}

