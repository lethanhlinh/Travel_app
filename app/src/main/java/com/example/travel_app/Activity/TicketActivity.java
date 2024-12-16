package com.example.travel_app.Activity;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.travel_app.Domain.ItemDomain;
import com.example.travel_app.Domain.User;
import com.example.travel_app.R;
import com.example.travel_app.databinding.ActivityTicketBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class TicketActivity extends BaseActivity {
    ActivityTicketBinding binding;
    private ItemDomain object;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTicketBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        setVariable();
    }

    private void setVariable() {
        Glide.with(TicketActivity.this)
                .load(object.getPic())
                .into(binding.pic);
        Glide.with(TicketActivity.this)
                .load(object.getTourGuidePic())
                .into(binding.profile);

        binding.backBtn.setOnClickListener(v -> finish());
        binding.titleTxt.setText(object.getTitle());
        binding.durationTxt.setText(object.getDuration());
        binding.tourGuideTxt.setText(object.getDateTour());
        binding.tourGuideNameTxt.setText(object.getTourGuideName());
        binding.timeTxt.setText(object.getTimeTour());

        binding.callBtn.setOnClickListener(view -> {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse("sms: " + object.getTourGuidePhone()));
            sendIntent.putExtra("sms_body", "type your message");
            startActivity(sendIntent);
        });
        binding.messageBtn.setOnClickListener(view -> {
            String phone = object.getTourGuidePhone();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",phone,null));
            startActivity(intent);
        });

        binding.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureLayoutAsImage();
                addHistory(object);
                addPointsUser(5);
            }
        });
    }

    private void addPointsUser(double point) {
        // Lấy userKey bằng Callback
        user.setPoint(user.getPoint() + point);
        getUserKey(user, new UserCallback() {
            @Override
            public void onUserKeyFound(String userKey) {
                if (userKey != null) {
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("User");
                    myRef.child(userKey)
                            .setValue(user)
                            .addOnSuccessListener(aVoid -> Toast.makeText(TicketActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(TicketActivity.this, "Lỗi khi cập nhật", Toast.LENGTH_SHORT).show());
                } else {
                    Toast.makeText(TicketActivity.this, "Không tìm thấy người dùng.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(TicketActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getIntentExtra() {
        object = (ItemDomain) getIntent().getSerializableExtra("object");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user = (User) bundle.getSerializable("user");
        }
    }

    // Phương thức chụp ảnh một vùng cụ thể trong layout
    private void captureLayoutAsImage() {
        LinearLayout ticketLayout = findViewById(R.id.linearLayout);
        ticketLayout.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(ticketLayout.getDrawingCache());
        ticketLayout.setDrawingCacheEnabled(false);

        saveImageToStorage(bitmap); // Hoặc croppedBitmap nếu bạn đã cắt
    }

    // Lưu ảnh vào bộ nhớ trong
    private void saveImageToStorage(Bitmap bitmap) {
        try {
            // Lưu vào bộ nhớ ngoài trong thư mục Downloads
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "ticket_image_cropped.png");

            // Lưu file vào
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

            Toast.makeText(TicketActivity.this, "Ảnh đã được lưu", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(TicketActivity.this, "Lỗi khi lưu ảnh", Toast.LENGTH_SHORT).show();
        }
    }

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

    public void getUserKey(User user, UserCallback callback) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("User");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    User currentUser = child.getValue(User.class);
                    if (currentUser != null && currentUser.equals(user)) {
                        callback.onUserKeyFound(child.getKey()); // Trả về userKey
                        return;
                    }
                }
                callback.onUserKeyFound(null); // Không tìm thấy
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    public interface UserCallback {
        void onUserKeyFound(String userKey);
        void onError(String error);
    }
}