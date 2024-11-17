package com.example.travel_app.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.travel_app.Domain.User;
import com.example.travel_app.R;
import com.example.travel_app.databinding.ActivitySignupBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class SignUpActivity extends BaseActivity {
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 100;
    ActivitySignupBinding binding;
    User user;
    ArrayList<User> listUser = new ArrayList<>();
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listUser = getListUser();
        database = FirebaseDatabase.getInstance();

        setOnClickSignUp();
        //setupGoogleSignIn();
    }

    private void setOnClickSignUp() {
        //Khi nhan vao nut dang ky thi se thuc hien ham onSignUp
        binding.btnSignUp.setOnClickListener(view -> {
            // Kiểm tra các trường dữ liệu trước khi tiếp tục
            if (isValidInput()) {
                onSignUp();
            }
        });
        // Nếu đã có tài khoản, quay lại trang đăng nhập
        binding.btnSignIn.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
        });
    }

    private void onSignUp() {
        // Lấy tham chiếu tới "User" trong Firebase
        DatabaseReference userRef = database.getReference("User");
        // Đếm số lượng bản ghi hiện có để xác định key tiếp theo
        userRef.addValueEventListener(new ValueEventListener() {
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

    private boolean isValidInput() {
        String Password = binding.passWordTxt.getText().toString();
        String FullName = binding.hoTenTxt.getText().toString();
        String Phone = binding.phoneTxt.getText().toString();
        String Email = binding.emailTxt.getText().toString();
        String ConfirmPw = binding.confirmPwTxt.getText().toString();

        // Kiểm tra các trường dữ liệu còn lại
        if (Password.isEmpty() || FullName.isEmpty() || Phone.isEmpty() || Email.isEmpty() || ConfirmPw.isEmpty()) {
            if (Password.isEmpty()) {
                binding.passWordLayout.setError("Mật khẩu không được để trống");
            }
            if (FullName.isEmpty()) {
                binding.hoTenLayout.setError("Họ tên không được để trống");
            }
            if (Phone.isEmpty()) {
                binding.phoneLayout.setError("Số điện thoại không được để trống");
            }
            if (Email.isEmpty()) {
                binding.emailLayout.setError("Email không được để trống");
            }
            if (ConfirmPw.isEmpty()) {
                binding.confirmPwLayout.setError("Xác nhận mật khẩu không được để trống");
            }
            return false;
        }

        // Kiểm tra email hợp lệ
        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            binding.emailLayout.setError("Email không hợp lệ");
            return false;
        }else if(isEarsEmail(Email)){
            binding.emailLayout.setError("Email đã tồn tại");
            return false;
        }

        // Kiểm tra số điện thoại hợp lệ
        if (!Patterns.PHONE.matcher(Phone).matches() || !Phone.startsWith("0")) {
            binding.phoneLayout.setError("Số điện thoại không hợp lệ");
            return false;
        } else if (isEarsPhone(Phone)) {
            binding.phoneLayout.setError("Số điện thoại đã tồn tại");
            return false;
        }

        // Kiểm tra mật khẩu và xác nhận mật khẩu
        if (!isValidPassword(Password)) {
            binding.passWordLayout.setError("Mật khẩu phải bắt đầu bằng chữ hoa, có ký tự số và ký tự đặc biệt");
            return false;
        }

        if (!Password.equals(ConfirmPw)) {
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
        user.setAddress(null);
        user.setEmail(Email);
        user.setFullName(FullName);
        user.setPassword(Password);
        user.setPhone(Phone);
        user.setPoint(200);
        user.setTicket(null);
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

    public boolean isEarsEmail (String email){
        ArrayList<User> listUser = getListUser();
        for (User user : listUser) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEarsPhone (String phone){
        ArrayList<User> listUser = getListUser();
        for (User user : listUser) {
            if (user.getPhone().equals(phone)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<User> getListUser() {
        DatabaseReference myRef = database.getReference("User");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        User userSignup = issue.getValue(User.class);
                        if (userSignup != null) {
                            listUser.add(userSignup);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return listUser;
    }

//    private void setupGoogleSignIn() {
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//        googleSignInClient = GoogleSignIn.getClient(this, gso);
//
//        binding.btnGoogle.setOnClickListener(view -> {
//            Intent signInIntent = googleSignInClient.getSignInIntent();
//            startActivityForResult(signInIntent, RC_SIGN_IN);
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
//        }
//    }
//
//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        try {
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//
//            if (account != null) {
//                // Lấy thông tin người dùng từ tài khoản Google
//                String email = account.getEmail();
//                String fullName = account.getDisplayName();
//                Uri photoUrl = account.getPhotoUrl();
//
//                // Điền thông tin vào các trường đăng ký
//                binding.emailTxt.setText(email);
//                binding.hoTenTxt.setText(fullName);
//                if (photoUrl != null) {
//                    user.setPic(photoUrl.toString());
//                }
//            }
//        } catch (ApiException e) {
//            e.printStackTrace();
//        }
//    }
}