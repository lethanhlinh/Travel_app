package com.example.travel_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.travel_app.Domain.User;
import com.example.travel_app.R;
import com.example.travel_app.databinding.ActivityEditProfileBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditProfileActivity extends BaseActivity {
    ActivityEditProfileBinding binding;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getUserFromIntent();
        setVariable();
        UpdateProfile();
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());

        Glide.with(EditProfileActivity.this)
                .load(user.getPic())
                .into(binding.pic);
        binding.hoTenTxt.setText(user.getFullName());
        binding.tenDangNhapTxt.setText(user.getEmail());
        binding.phoneTxt.setText(user.getPhone());
        binding.nsinhTxt.setText("20/12/2024");

    }

    public void getUserFromIntent() {
        user = (User) getIntent().getSerializableExtra("user");
    }

    public void UpdateProfile(){
        binding.btnSavechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IsvalidInput()){
                    String hoTen = binding.hoTenTxt.getText().toString();
                    String email = binding.tenDangNhapTxt.getText().toString();
                    String phone = binding.phoneTxt.getText().toString();
                    String newPassWord = binding.newPassTxt.getText().toString();

                    user.setFullName(hoTen);
                    user.setEmail(email);
                    user.setPhone(phone);
                    user.setPassword(newPassWord);

                    // Lấy userKey bằng Callback
                    getUserKey(user, new UserCallback() {
                        @Override
                        public void onUserKeyFound(String userKey) {
                            if (userKey != null) {
                                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("User");
                                myRef.child(userKey)
                                        .setValue(user)
                                        .addOnSuccessListener(aVoid -> Toast.makeText(EditProfileActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e -> Toast.makeText(EditProfileActivity.this, "Lỗi khi cập nhật", Toast.LENGTH_SHORT).show());
                            } else {
                                Toast.makeText(EditProfileActivity.this, "Không tìm thấy người dùng.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(EditProfileActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    public boolean IsvalidInput(){
        String newPassWord = binding.newPassTxt.getText().toString();
        if(binding.oldPassTxt.getText().toString().isEmpty()){
            binding.oldPassLayout.setError("Vui lòng nhập mật khẩu cũ");
            return false;
        } else if (!binding.oldPassTxt.getText().toString().equals(user.getPassword())) {
            binding.oldPassLayout.setError("Mật khẩu cũ không đúng!!!");
            return false;
        }else {
            binding.oldPassLayout.setError(null);
            if(newPassWord.isEmpty()){
                binding.newPassLayout.setError("Vui lòng nhập mật khẩu mới");
                return false;
            } else if (!isValidPassword(newPassWord)) {
                    binding.newPassLayout.setError("Mật khẩu phải bắt đầu bằng chữ hoa, có ký tự số và ký tự đặc biệt");
                    return false;
            }
        }

        binding.oldPassLayout.setError(null);
        binding.newPassLayout.setError(null);
        return true;
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

    public boolean isValidPassword(@NonNull String password) {
        // Kiểm tra mật khẩu bắt đầu bằng chữ hoa, có chữ số và ký tự đặc biệt
        return password.matches("^[A-Z].*[0-9].*[^a-zA-Z0-9].*$");
    }
}