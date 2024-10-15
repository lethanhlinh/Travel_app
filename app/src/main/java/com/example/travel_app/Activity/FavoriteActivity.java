package com.example.travel_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import com.example.travel_app.databinding.ActivityFavoriteBinding;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travel_app.R;
import com.example.travel_app.databinding.ActivitySignupBinding;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class FavoriteActivity extends BaseActivity {
   ActivityFavoriteBinding binding;
    private ChipNavigationBar chipNavigationBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        chipNavigationBar = findViewById(R.id.thanhmenu);
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {

                if (id == R.id.explorer) {
                    startActivity(new Intent(FavoriteActivity.this, MainActivity.class ));
                } else if (id == R.id.favorites) {

                } else if (id == R.id.cart) {

                } else if (id == R.id.profile) {
                    startActivity(new Intent(FavoriteActivity.this, ProfileActivity.class ));
                }

            }
        });

    }

}