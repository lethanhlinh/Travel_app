package com.example.travel_app.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Adapter.RecommendedAdapter;
import com.example.travel_app.Adapter.SeeAllAdapter;
import com.example.travel_app.Domain.ItemDomain;
import com.example.travel_app.R;
import com.example.travel_app.databinding.ActivitySeeAllBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SeeAllActivity extends BaseActivity {
    private ImageView imgBack;
    ActivitySeeAllBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySeeAllBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initItem();
        imgBack = findViewById(R.id.backBtn);

    }
    private void initItem(){
        DatabaseReference myRef = database.getReference("Item");
        binding.progressBarPopular.setVisibility(View.VISIBLE);

        ArrayList<ItemDomain> list = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue:snapshot.getChildren()){
                        list.add(issue.getValue(ItemDomain.class));
                    }
                    if(!list.isEmpty()){
                        binding.recyclerViewPopular.setLayoutManager(new LinearLayoutManager(SeeAllActivity.this, LinearLayoutManager.VERTICAL,false));
                        RecyclerView.Adapter adapter = new SeeAllAdapter(list);
                        binding.recyclerViewPopular.setAdapter(adapter);
                    }
                    binding.progressBarPopular.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}