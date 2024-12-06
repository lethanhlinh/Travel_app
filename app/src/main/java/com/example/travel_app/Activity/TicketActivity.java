package com.example.travel_app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.travel_app.Domain.ItemDomain;
import com.example.travel_app.databinding.ActivityTicketBinding;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TicketActivity extends BaseActivity {
    ActivityTicketBinding binding;
    private ItemDomain object;
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
            }
        });
    }

    private void getIntentExtra() {
        object = (ItemDomain) getIntent().getSerializableExtra("object");
    }

    // Phương thức chụp ảnh một vùng cụ thể trong layout
    private void captureLayoutAsImage() {
        // Lấy layout root
        View view = binding.main;

        // Đảm bảo rằng layout đã được render
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        // Xác định vùng cắt (rect) bạn muốn chụp (ví dụ: vùng phía trên)
        Rect rect = new Rect(0, 0, view.getWidth(), view.getHeight() / 2); // Cắt nửa trên của layout

        // Cắt vùng ảnh
        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height());

        // Lưu ảnh vào bộ nhớ trong
        saveImageToStorage(croppedBitmap);
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

        // Chuyển đối tượng ItemDomain thành JSON
        String json = new Gson().toJson(object);

        // Lưu vào SharedPreferences với key là ID của item
        editor.putString(object.getTitle(), json);
        editor.apply();
    }
}