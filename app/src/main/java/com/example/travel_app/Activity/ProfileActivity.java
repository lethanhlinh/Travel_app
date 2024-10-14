package com.example.travel_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travel_app.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class ProfileActivity extends AppCompatActivity {
    private ChipNavigationBar chipNavigationBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        chipNavigationBar = findViewById(R.id.thanhmenu);
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {

                if (id == R.id.explorer) {
                    startActivity(new Intent(ProfileActivity.this, MainActivity.class ));
                } else if (id == R.id.favorites) {

                } else if (id == R.id.cart) {

                } else if (id == R.id.profile) {

                }

            }
        });
    }
}
