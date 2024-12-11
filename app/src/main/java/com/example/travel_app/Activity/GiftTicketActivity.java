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

import com.bumptech.glide.Glide;
import com.example.travel_app.Domain.Gift;
import com.example.travel_app.Domain.ItemDomain;
import com.example.travel_app.R;
import com.example.travel_app.databinding.ActivityGiftTicketBinding;
import com.example.travel_app.databinding.ActivityTicketBinding;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GiftTicketActivity extends BaseActivity {
    ActivityGiftTicketBinding binding;
    private Gift object;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGiftTicketBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

            }
        });
    }

    private void getIntentExtra() {
        object = (Gift) getIntent().getSerializableExtra("object");
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


}