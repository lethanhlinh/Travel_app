package com.example.travel_app.Activity;

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
import com.example.travel_app.Domain.Gift;
import com.example.travel_app.Domain.ItemDomain;
import com.example.travel_app.Domain.User;
import com.example.travel_app.R;
import com.example.travel_app.databinding.ActivityGiftTicketBinding;
import com.example.travel_app.databinding.ActivityTicketBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GiftTicketActivity extends BaseActivity {
    ActivityGiftTicketBinding binding;
    private Gift object;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGiftTicketBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //lay du lieu tu intent
        getIntentExtra();
        setVariable();
    }

    private void setVariable() {
        Glide.with(GiftTicketActivity.this)
                .load(object.getPic())
                .into(binding.pic);
        Glide.with(GiftTicketActivity.this)
                .load(object.getPic())
                .into(binding.pic);

        binding.backBtn.setOnClickListener(v -> finish());
        binding.titleTxt.setText(object.getGiftCode());
        binding.durationTxt.setText(object.getExpireDate());
        binding.tourGuideTxt.setText(object.getCoupon());




        binding.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureLayoutAsImage();
                subtractPointUser(object.getSubPoint());
            }
        });
    }
    //tru diem
    private void subtractPointUser(double point) {
        if (user == null) {
            Toast.makeText(GiftTicketActivity.this, "User không tồn tại", Toast.LENGTH_SHORT).show();
            return; // Dừng hàm nếu user null
        }
        // Lấy userKey bằng Callback
        user.setPoint(user.getPoint() - point);
        getUserKey(user, new UserCallback(){
            @Override
            public void onUserKeyFound(String userKey) {
                if (userKey != null) {
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("User");
                    myRef.child(userKey)
                            .setValue(user)
                            .addOnSuccessListener(aVoid -> Toast.makeText(GiftTicketActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(GiftTicketActivity.this, "Lỗi khi cập nhật", Toast.LENGTH_SHORT).show());
                } else {
                    Toast.makeText(GiftTicketActivity.this, "Không tìm thấy người dùng.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(GiftTicketActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
            }
        });
        if (getApplicationContext() instanceof MainActivity) {
            ((MainActivity) getApplicationContext()).setUser(user);  // Lấy User từ MainActivity
        }
    }

    private void getUserKey(User user, UserCallback callback) {
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


    private void getIntentExtra() {
        object = (Gift) getIntent().getSerializableExtra("object");
        user = (User) getIntent().getSerializableExtra("user");
    }

    // Phương thức chụp ảnh một vùng cụ thể trong layout
    private void captureLayoutAsImage() {
        LinearLayout ticketLayout = findViewById(R.id.linearLayout);
        ticketLayout.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(ticketLayout.getDrawingCache());
        ticketLayout.setDrawingCacheEnabled(false);

        // Bạn có thể không cần cắt nếu toàn bộ LinearLayout là những gì bạn muốn
        // Rect rect = new Rect(0, 0, ticketLayout.getWidth(), ticketLayout.getHeight());
        // Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height());

        saveImageToStorage(bitmap); // Hoặc croppedBitmap nếu bạn đã cắt


        // Lấy layout root
//        View view = binding.main;
//
//        // Đảm bảo rằng layout đã được render
//        view.setDrawingCacheEnabled(true);
//        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
//        view.setDrawingCacheEnabled(false);
//
//        // Xác định vùng cắt (rect) bạn muốn chụp (ví dụ: vùng phía trên)
//        Rect rect = new Rect(0, 0, view.getWidth(), view.getHeight() ); // Cắt nửa trên của layout
//
//        // Cắt vùng ảnh
//        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height());
//
//        // Lưu ảnh vào bộ nhớ trong
//        saveImageToStorage(croppedBitmap);


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

            Toast.makeText(GiftTicketActivity.this, "Ảnh đã được lưu", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(GiftTicketActivity.this, "Lỗi khi lưu ảnh", Toast.LENGTH_SHORT).show();
        }
    }

    public interface UserCallback {
        void onUserKeyFound(String userKey);
        void onError(String error);
    }

}