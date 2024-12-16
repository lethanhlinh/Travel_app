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
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
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
        binding.btnBack.setOnClickListener(v -> finish());
        binding.txtClear.setOnClickListener(v -> {
            clearHistory();
        });
        // Thiết lập RecyclerView
        adapter = new FavoriteAdapter(historyItems, this);
        binding.recycleViewHistory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.recycleViewHistory.setAdapter(adapter);
    }

    @Override
    public void onFavoriteChanged() {
        loadHistoryItems();
        adapter.notifyDataSetChanged();
    }

    private void loadHistoryItems() {
        SharedPreferences sharedPreferences = getSharedPreferences("HistoryItems", Context.MODE_PRIVATE);
        String historyJson = sharedPreferences.getString("history_list", "[]"); // Mặc định là danh sách rỗng

        // Chuyển JSON thành danh sách đối tượng
        List<ItemDomain> items = new Gson().fromJson(historyJson, new TypeToken<List<ItemDomain>>(){}.getType());
        historyItems.addAll(items);

        binding.progressBarHistory.setVisibility(View.GONE);

        if (historyItems.isEmpty()) {
            Toast.makeText(this, "Bạn chưa đặt tour nào!", Toast.LENGTH_SHORT).show();
        }
    }
    private void clearHistory() {
        SharedPreferences sharedPreferences = getSharedPreferences("HistoryItems", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("history_list"); // Xóa key "history_list"
        editor.apply();
    }
}
