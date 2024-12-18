package com.example.travel_app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;
import com.example.travel_app.Domain.Gift;
import com.example.travel_app.Domain.ItemDomain;
import com.example.travel_app.Domain.User;
import com.example.travel_app.R;
import com.example.travel_app.databinding.ActivityDetailBinding;
import com.example.travel_app.databinding.ActivityGiftDetailBinding;
import com.example.travel_app.databinding.ActivityMainBinding;
import com.google.gson.Gson;

public class GiftDetailActivity extends BaseActivity {
    ActivityGiftDetailBinding binding;
    private Gift object;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGiftDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        setVariable();
    }

    private void setVariable() {
        binding.titleTxt.setText(object.getGiftCode());
        binding.priceTxt.setText(String.valueOf(object.getSubPoint()));
        binding.backBtn.setOnClickListener(v -> finish());

        binding.durationTxt.setText(object.getExpireDate());

        binding.descriptionTxt.setText(object.getDescription());




        Glide.with(GiftDetailActivity.this)
                .load(object.getPic())
                .into(binding.pic);
        binding.addToCartBtn.setOnClickListener(view -> {
            Intent intent = new Intent(GiftDetailActivity.this, GiftTicketActivity.class);
            intent.putExtra("object", object);
            intent.putExtra("user", user);
            startActivity(intent);
        });


    }



    private void getIntentExtra() {
        object = (Gift) getIntent().getSerializableExtra("object");
        user = (User) getIntent().getSerializableExtra("user");
    }
}