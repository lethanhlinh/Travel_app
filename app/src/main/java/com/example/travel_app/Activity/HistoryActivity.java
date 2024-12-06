package com.example.travel_app.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.travel_app.Adapter.FavoriteAdapter;
import com.example.travel_app.Domain.ItemDomain;
import com.example.travel_app.databinding.ActivityHistoryBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

public class HistoryActivity extends BaseActivity implements FavoriteAdapter.OnFavoriteChangeListener {
    ActivityHistoryBinding binding;
    private ArrayList<ItemDomain> historyItems;
    private FavoriteAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo danh sách
        historyItems = new ArrayList<>();
        // Gọi phương thức load dữ liệu
        loadHistoryItems();

        // Thiết lập RecyclerView
        adapter = new FavoriteAdapter(historyItems, this);
        binding.recycleViewHistory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.recycleViewHistory.setAdapter(adapter);
    }

    @Override
    public void onFavoriteChanged() {
        loadHistoryItems();
        adapter.notifyDataSetChanged();

        binding.btnBack.setOnClickListener(view -> finish());
    }

    private void loadHistoryItems() {
        // Lấy danh sách yêu thích từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("HistoryItems", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String json = (String) entry.getValue();
            ItemDomain item = new Gson().fromJson(json, ItemDomain.class);
            historyItems.add(item);
        }

        // Ẩn progress bar sau khi tải xong dữ liệu
        binding.progressBarHistory.setVisibility(View.GONE);

        // Nếu danh sách trống, hiển thị thông báo
        if (historyItems.isEmpty()) {
            Toast.makeText(this, "Chưa có sản phẩm yêu thích!", Toast.LENGTH_SHORT).show();
        }
    }
}
