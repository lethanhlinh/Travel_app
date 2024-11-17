package com.example.travel_app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.travel_app.Domain.User;
import com.example.travel_app.databinding.ActivitySigninBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SignInActivity extends BaseActivity {
    private User user;
    FirebaseDatabase database;
    private ActivitySigninBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        setLogin();
    }

    // Xử lý logic đăng nhập
    public void setLogin() {
        // Đăng nhập thành công -> chuyển hướng đến IntroActivity
        binding.btnSignIn.setOnClickListener(view -> {
            if (isValidInput()) {
                String tenDangNhap = binding.tenDangNhapTxt.getText().toString();
                String password = binding.passWordTxt.getText().toString();

                // Lấy danh sách user từ Firebase và kiểm tra đăng nhập
                getListUser(listUser -> {
                    if (checkLogin(listUser, tenDangNhap, password)) {
                        // Lưu trạng thái đăng nhập vào SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.apply();

                        // Lấy thông tin user đã đăng nhập
                        user = getUser(listUser, tenDangNhap, password);

                        // Chuyển hướng đến IntroActivity
                        Intent intent = new Intent(SignInActivity.this, IntroActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        finish();
                    } else {
                        // Đăng nhập thất bại
                        binding.tenDangNhapLayout.setError("Tên đăng nhập hoặc mật khẩu không đúng");
                        binding.passWordLayout.setError("Tên đăng nhập hoặc mật khẩu không đúng");
                    }
                });
            }
        });

        // Chuyển đến màn hình đăng ký
        binding.btnSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        // Xử lý khi quên mật khẩu (nếu có triển khai sau này)
        binding.TxtforgotPassword.setOnClickListener(v -> {
            // Thêm logic quên mật khẩu tại đây
        });
    }

    // Kiểm tra đầu vào
    public boolean isValidInput() {
        String tenDangNhap = binding.tenDangNhapTxt.getText().toString();
        String password = binding.passWordTxt.getText().toString();

        // Kiểm tra rỗng
        if (tenDangNhap.isEmpty()) {
            binding.tenDangNhapLayout.setError("Tên đăng nhập không được để trống");
            return false;
        }
        if (password.isEmpty()) {
            binding.passWordLayout.setError("Mật khẩu không được để trống");
            return false;
        }

        // Nếu hợp lệ
        binding.tenDangNhapLayout.setError(null);
        binding.passWordLayout.setError(null);
        return true;
    }

    // Kiểm tra đăng nhập
    public boolean checkLogin(ArrayList<User> listUser, String tenDangNhap, String password) {
        for (User user : listUser) {
            // Kiểm tra email + mật khẩu
            if (user.getEmail().equals(tenDangNhap) || user.getPhone().equals(tenDangNhap) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    // Lấy thông tin user đang đăng nhập
    public User getUser(ArrayList<User> listUser, String tenDangNhap, String password) {
        User userLogin = new User();
        for (User user : listUser) {
            if ((user.getEmail().equals(tenDangNhap) || user.getPhone().equals(tenDangNhap)) &&
                    user.getPassword().equals(password)) {
                userLogin = user;
            }
        }
        return userLogin;
    }

    // Lấy danh sách user từ Firebase
    public void getListUser(UserCallback callback) {
        DatabaseReference myRef = database.getReference("User");
        ArrayList<User> listUser = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        User userLogin = issue.getValue(User.class);
                        if (userLogin != null) {
                            listUser.add(userLogin);
                        }
                    }
                }
                // Gọi callback sau khi dữ liệu được tải xong
                callback.onCallback(listUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Lỗi khi đọc dữ liệu: " + error.getMessage());
                callback.onCallback(new ArrayList<>()); // Trả về danh sách rỗng nếu có lỗi
            }
        });
    }

    // Interface callback để xử lý bất đồng bộ
    public interface UserCallback {
        void onCallback(ArrayList<User> listUser);
    }
}