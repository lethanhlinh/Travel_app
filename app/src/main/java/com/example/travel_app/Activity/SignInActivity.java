package com.example.travel_app.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.travel_app.Domain.User;
import com.example.travel_app.databinding.ActivitySigninBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class SignInActivity extends BaseActivity {
    private ArrayList<User> listUsers;
    private ActivitySigninBinding binding;
    private User userLogin;

    //Khai báo user o day
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Set sự kiện cho trang Login
        addOnClickSignIn();
    }

    private void addOnClickSignIn() {
        binding.btnSignIn.setOnClickListener(view -> {
            if (isValidInput()) {
                String tenDangNhap = binding.tenDangNhapTxt.getText().toString();
                String password = binding.passWordTxt.getText().toString();
                loadUserLoginFromFirebase(tenDangNhap, password);
            }
        });

        //Neu chua co tai khoan thi chuyen sang trang dang ky
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidInput()) {
                    String tenDangNhap = binding.tenDangNhapTxt.getText().toString();
                    String password = binding.passWordTxt.getText().toString();
                    loadUserLoginFromFirebase(tenDangNhap, password);
                }
            }
        });
        //Nếu quên mật khẩu thì chuyển sang trang đổi password
        binding.TxtforgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
//                startActivity(intent);
            }
        });
    }



    public boolean isValidInput() {
        String tenDangNhap = binding.tenDangNhapTxt.getText().toString();
        String password = binding.passWordTxt.getText().toString();
        if (password.isEmpty() || tenDangNhap.isEmpty()) {
            if (tenDangNhap.isEmpty()) {
                binding.tenDangNhapLayout.setError("Tên đăng nhập không được để trống");
                return false;
            }

            if (password.isEmpty()) {
                binding.passWordLayout.setError("Mật khẩu không được để trống");
                return false;
            }
        }

        //Xóa Error cho cac truong du lieu
        binding.tenDangNhapLayout.setError(null);
        binding.passWordLayout.setError(null);

        return true;
    }


    private void loadUserLoginFromFirebase(String tenDangNhap, String password) {
        DatabaseReference myRef = database.getReference("User");
        listUsers = new ArrayList<>();
        myRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listUsers.clear();
                for (DataSnapshot issue : task.getResult().getChildren()) {
                    listUsers.add(issue.getValue(User.class));
                }

                for (User user : listUsers) {
                    // Kiểm tra email
                    if (user.getEmail().equals(tenDangNhap)) {
                        if (user.getPassword().equals(password)) {
                            userLogin = user;
                            // Lưu trạng thái đăng nhập vào SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isLoggedIn", true); // Đặt trạng thái đăng nhập thành true
                            editor.apply(); // Áp dụng thay đổi
                            Toast.makeText(SignInActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignInActivity.this, IntroActivity.class);
                            intent.putExtra("user", userLogin);
                            startActivity(intent);
                            finish();
                            return;
                        } else {
                            binding.passWordLayout.setError("Mật khẩu không đúng");
                            return; // Mật khẩu không đúng
                        }
                    }

                    // Kiểm tra số điện thoại
                    if (String.valueOf(user.getPhone()).equals(tenDangNhap)) {
                        if (user.getPassword().equals(password)) {
                            userLogin = user;
                            // Lưu trạng thái đăng nhập vào SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isLoggedIn", true); // Đặt trạng thái đăng nhập thành true
                            editor.apply(); // Áp dụng thay đổi
                            Toast.makeText(SignInActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignInActivity.this, IntroActivity.class);
                            intent.putExtra("user", userLogin);
                            startActivity(intent);
                            finish();
                            return;
                        } else {
                            binding.passWordLayout.setError("Mật khẩu không đúng");
                            return; // Mật khẩu không đúng
                        }
                    }
                }
                Toast.makeText(SignInActivity.this, "Tài khoản không tồn tại, vui lòng đăng ky", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SignInActivity.this, "Loi khi tai du lieu!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
