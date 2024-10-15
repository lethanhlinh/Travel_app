package com.example.travel_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travel_app.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class ProfileActivity extends BaseActivity {
    private ChipNavigationBar chipNavigationBar;
    private TextView textView7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        textView7 = findViewById(R.id.textView7);
        chipNavigationBar = findViewById(R.id.thanhmenu);
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {

                if (id == R.id.explorer) {
                    startActivity(new Intent(ProfileActivity.this, MainActivity.class ));
                } else if (id == R.id.favorites) {

                } else if (id == R.id.cart) {
                    startActivity(new Intent(ProfileActivity.this, FavoriteActivity.class ));
                } else if (id == R.id.profile) {

                }

            }
        });
        textView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class ));
            }
        });
    }
}
