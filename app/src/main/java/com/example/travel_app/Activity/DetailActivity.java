package com.example.travel_app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;
import com.example.travel_app.Domain.ItemDomain;
import com.example.travel_app.Domain.User;
import com.example.travel_app.R;
import com.example.travel_app.databinding.ActivityDetailBinding;
import com.example.travel_app.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private ItemDomain object;
    private User user;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        setVariable();
    }
    //Set du lieu tu intent
    private void setVariable() {
        binding.titleTxt.setText(object.getTitle());
        binding.priceTxt.setText("$"+object.getPrice());
        binding.backBtn.setOnClickListener(v -> finish());
        binding.bedTxt.setText("" + object.getBed());
        binding.durationTxt.setText(object.getDuration());
        binding.distanceTxt.setText(object.getDistance());
        binding.descriptionTxt.setText(object.getDescription());
        binding.addressTxt.setText(object.getAddress());
        binding.ratingTxt.setText(object.getScore()+"Rating");
        binding.ratingBar.setRating((float)object.getScore());

        Glide.with(DetailActivity.this)
                .load(object.getPic())
                .into(binding.pic);
        binding.addToCartBtn.setOnClickListener(view -> {
            // Thêm vào danh sách lịch sử đặt vé và mở TicketActivity để tải vé về
            addHistory(object);
            Toast.makeText(DetailActivity.this, "Đã đặt vé thành công!!! Mời bạn tải vé về ngay.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DetailActivity.this, TicketActivity.class);
            intent.putExtra("object", object);
            intent.putExtra("user", user);

            startActivity(intent);
        });

        // Kiểm tra trạng thái yêu thích từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("FavoriteItems", MODE_PRIVATE);
        String json = sharedPreferences.getString(object.getTitle(), null);
        if (json != null) {
            isFavorite = true;
            binding.likeBtn.getDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            object.setFavorite(isFavorite);
        }

        binding.likeBtn.setOnClickListener(view -> {
            if (!isFavorite) {
                isFavorite = true;
                binding.likeBtn.getDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                object.setFavorite(isFavorite);
                addFavorite(object);
            } else {
                isFavorite = false;
                binding.likeBtn.getDrawable().clearColorFilter();
                object.setFavorite(isFavorite);
                removeFavorite(object);
            }
        });
    }

    //Xoa khoi danh sach yeu thich
    private void removeFavorite(ItemDomain object) {
        SharedPreferences sharedPreferences = getSharedPreferences("FavoriteItems", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Xóa mục yêu thích bằng ID
        editor.remove(object.getTitle());
        editor.apply();
    }

    //Them vao danh sach yeu thich
    private void addFavorite(ItemDomain object) {
        SharedPreferences sharedPreferences = getSharedPreferences("FavoriteItems", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Chuyển đối tượng ItemDomain thành JSON
        String json = new Gson().toJson(object);

        // Lưu vào SharedPreferences với key là ID của item
        editor.putString(object.getTitle(), json);
        editor.apply();
    }

    //Thêm vao danh sach lich su dat ve
    private void addHistory(ItemDomain object) {
        SharedPreferences sharedPreferences = getSharedPreferences("HistoryItems", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Lấy danh sách hiện tại từ SharedPreferences
        String existingHistory = sharedPreferences.getString("history_list", "[]"); // Mặc định là danh sách rỗng
        List<ItemDomain> historyList = new Gson().fromJson(existingHistory, new TypeToken<List<ItemDomain>>(){}.getType());

        // Thêm item mới vào danh sách
        historyList.add(object);

        // Chuyển danh sách thành JSON và lưu lại
        String updatedHistory = new Gson().toJson(historyList);
        editor.putString("history_list", updatedHistory);
        editor.apply();
    }
    //Lay du lieu tu intent
    private void getIntentExtra() {
        object = (ItemDomain) getIntent().getSerializableExtra("object");
        user = (User) getIntent().getSerializableExtra("user");
    }
}