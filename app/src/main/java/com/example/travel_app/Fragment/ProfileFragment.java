package com.example.travel_app.Fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.travel_app.Activity.EditProfileActivity;
import com.example.travel_app.Activity.HistoryActivity;
import com.example.travel_app.Activity.SignInActivity;
import com.example.travel_app.Activity.XemTVNhomActivity;
import com.example.travel_app.Chat.ChatActivity;
import com.example.travel_app.Domain.User;
import com.example.travel_app.R;
import com.example.travel_app.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private User user;
    FragmentProfileBinding binding;
    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseDatabase database;
    private DatabaseReference userRef;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(User user) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
    //    args.putParcelable("user", (Parcelable) user);
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
          //  user = getArguments().getParcelable("user");
            user = (User) getArguments().getSerializable("user");
        }

        // Initialize Firebase components
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("User");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        binding = FragmentProfileBinding.bind(view);
        setVariable();
        return view;
    }

    private void setVariable() {
        binding.profileName.setText(user.getFullName());
        binding.profileEmail.setText(user.getEmail());

        // Load image using Glide
        if (user.getPic() != null && !user.getPic().isEmpty()) {
            Glide.with(ProfileFragment.this)
                    .load(user.getPic())
                    .into(binding.profileImage);
        }

        //Set su kien cho nut Upload image
        binding.btnUploadImage.setOnClickListener(view -> {
            openImagePicker();
        });

        //Set su kien cho nut Dang Xuat
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xóa thông tin đăng nhập từ SharedPreferences
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();  // Xóa tất cả dữ liệu
                editor.apply();

                // Chuyển hướng người dùng về màn hình đăng nhập
                Intent intent = new Intent(getContext(), SignInActivity.class);
                startActivity(intent);
                getActivity().finish(); // Đảm bảo đóng Fragment hiện tại để không quay lại sau khi đăng xuất
            }
        });
        //Set su kien cho nut edit
        binding.editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        //Set su kien cho nut Book tour
        binding.booktour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HistoryActivity.class);
                startActivity(intent);
            }
        });

        //Set su kien cho nut Xem thanh vien nhom
        binding.xemThanhVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), XemTVNhomActivity.class);
                startActivity(intent);
            }
        });

        binding.privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                startActivity(intent);
            }
        });

    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            // Display the selected image
            Glide.with(ProfileFragment.this)
                    .load(imageUri)
                    .into(binding.profileImage);

            // Upload the image to Firebase Storage
            uploadImageToFirebase(imageUri);
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        if (imageUri != null) {
            // Generate a unique file name for the image
            String imageName = UUID.randomUUID().toString();

            // Create a reference to the image in Firebase Storage
            StorageReference imageRef = storageReference.child("profile_images/" + imageName);

            // Upload the image to Firebase Storage
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get the download URL of the uploaded image
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Update the user's profile picture in the Realtime Database
                                    user.setPic(uri.toString());
                                    userRef.child(user.getEmail()).setValue(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getContext(), "Ảnh đã được cập nhật", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getContext(), "Lỗi khi cập nhật ảnh", Toast.LENGTH_SHORT).show();
                                                    Log.e("ProfileFragment", "Error updating profile picture: " + e.getMessage());
                                                }
                                            });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Lỗi khi tải ảnh lên", Toast.LENGTH_SHORT).show();
                            Log.e("ProfileFragment", "Error uploading image: " + e.getMessage());
                        }
                    });
        }
    }
}
