package com.example.travel_app.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.travel_app.Domain.ItemDomain;
import com.example.travel_app.Domain.User;
import com.example.travel_app.R;
import com.example.travel_app.databinding.ActivitySignupBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class SignUpActivity extends BaseActivity {
    ActivitySignupBinding binding;
    User user;
    FirebaseDatabase database;
    ArrayList<User> listUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo Firebase
        database = FirebaseDatabase.getInstance();

        // Tải danh sách người dùng từ Firebase khi khởi tạo Activity
        loadUserDataFromFirebase();

        //Thiết lập sự kiện
        setVariable();
    }

    private void setVariable() {
        loadUserDataFromFirebase();
        binding.btnSignUp.setOnClickListener(view -> {
            // Kiểm tra các trường dữ liệu trước khi tiếp tục
            if (isValidInput()) {
                // Lấy tham chiếu tới "User" trong Firebase
                DatabaseReference userRef = database.getReference("User");

                // Đếm số lượng bản ghi hiện có để xác định key tiếp theo
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Số lượng bản ghi hiện có sẽ là key tiếp theo
                        long count = snapshot.getChildrenCount();
                        String key = String.valueOf(count); // Chuyển count thành chuỗi để làm key

                        // Lưu thông tin người dùng với key là số thứ tự
                        userRef.child(key).setValue(user)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(SignUpActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();

                                    // Chuyển sang trang đăng nhập
                                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                    startActivity(intent);
                                })
                                .addOnFailureListener(e -> Toast.makeText(SignUpActivity.this, "Lỗi khi đăng ký", Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SignUpActivity.this, "Lỗi kết nối Firebase", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Nếu đã có tài khoản, quay lại trang đăng nhập
        binding.btnSignIn.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
        });
    }

    private boolean isValidInput() {
        String password = binding.passWordTxt.getText().toString();
        String fullName = binding.hoTenTxt.getText().toString();
        String phone = binding.phoneTxt.getText().toString();
        String email = binding.emailTxt.getText().toString();
        String confirmPw = binding.confirmPwTxt.getText().toString();

        // Kiểm tra các trường dữ liệu còn lại
        if (password.isEmpty() || fullName.isEmpty() || phone.isEmpty() || email.isEmpty() || confirmPw.isEmpty()) {
            if (password.isEmpty()) {
                binding.passWordLayout.setError("Mật khẩu không được để trống");
            }
            if (fullName.isEmpty()) {
                binding.hoTenLayout.setError("Họ tên không được để trống");
            }
            if (phone.isEmpty()) {
                binding.phoneLayout.setError("Số điện thoại không được để trống");
            }
            if (email.isEmpty()) {
                binding.emailLayout.setError("Email không được để trống");
            }
            if (confirmPw.isEmpty()) {
                binding.confirmPwLayout.setError("Xác nhận mật khẩu không được để trống");
            }
            return false;
        }

        // Kiểm tra email hợp lệ
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailLayout.setError("Email không hợp lệ");
            return false;
        } else if (isEmailExist(email)) {
            binding.emailLayout.setError("Email đã tồn tại");
            return false;
        }

        // Kiểm tra số điện thoại hợp lệ
        if (!Patterns.PHONE.matcher(phone).matches() || !phone.startsWith("0")) {
            binding.phoneLayout.setError("Số điện thoại không hợp lệ");
            return false;
        } else if (isPhoneExist(Integer.parseInt(phone))) {
            binding.phoneLayout.setError("Số điện thoại đã tồn tại");
            return false;
        }

        // Kiểm tra mật khẩu và xác nhận mật khẩu
        if (!isValidPassword(password)) {
            binding.passWordLayout.setError("Mật khẩu phải bắt đầu bằng chữ hoa, có ký tự số và ký tự đặc biệt");
            return false;
        }

        if (!password.equals(confirmPw)) {
            binding.confirmPwLayout.setError("Mật khẩu không khớp");
            return false;
        }

        // Xóa lỗi nếu mọi thứ hợp lệ
        binding.passWordLayout.setError(null);
        binding.hoTenLayout.setError(null);
        binding.phoneLayout.setError(null);
        binding.emailLayout.setError(null);
        binding.confirmPwLayout.setError(null);

        // Đưa dữ liệu vào đối tượng user
        user = new User();
        user.setAddress("null");
        user.setEmail(email);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setPhone(Integer.parseInt(phone));
        user.setPoint(200);
        user.setTicket("null");
        user.setPic(encodeImageToBase64(R.drawable.user_image));

        return true;
    }

    public boolean isValidPassword(@NonNull String password) {
        // Kiểm tra mật khẩu bắt đầu bằng chữ hoa, có chữ số và ký tự đặc biệt
        return password.matches("^[A-Z].*[0-9].*[^a-zA-Z0-9].*$");
    }

    private String encodeImageToBase64(int drawableId) {
        // Chuyển drawable thành Bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableId);

        // Chuyển Bitmap thành Base64
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void loadUserDataFromFirebase() {
        DatabaseReference myRef = database.getReference("User");
        listUser = new ArrayList<>();
        myRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listUser.clear();
                for (DataSnapshot issue : task.getResult().getChildren()) {
                    listUser.add(issue.getValue(User.class));
                }
            } else {
                Toast.makeText(SignUpActivity.this,"Loi khi tai du lieu!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isEmailExist(String email) {
        for (User user : listUser) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPhoneExist(long phone) {
        for (User user : listUser) {
            if (user.getPhone() == phone) {
                return true;
            }
        }
        return false;
    }
}
