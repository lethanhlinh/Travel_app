package com.example.travel_app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import com.example.travel_app.Adapter.ViewPagerAdapter;
import com.example.travel_app.Domain.User;
import com.example.travel_app.Fragment.ProfileFragment;
import com.example.travel_app.R;
import com.example.travel_app.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseActivity {
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewPager = binding.viewPager;
        bottomNavigationView = binding.thanhmenu;

        // Kiểm tra trạng thái đăng nhập
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false); // Mặc định là false nếu không có dữ liệu

        if (!isLoggedIn) {
            // Nếu chưa đăng nhập, mở trang đăng nhập
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Nhận thông tin người dùng từ Intent
        user = (User) getIntent().getSerializableExtra("user");
        //Nhận userKey để truyền vào ProfileFragment
        String userKey = getIntent().getStringExtra("userKey");
        // Thiết lập ViewPager với adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, user);
        viewPager.setAdapter(adapter);

        // Thiết lập sự kiện cho ViewPager
        viewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Không cần xử lý
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.menu_home);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.menu_location);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.menu_favorites);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.menu_profile);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Không cần xử lý
            }
        });

        // Thiết lập sự kiện cho BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.menu_home) {
                    viewPager.setCurrentItem(0);
                } else if (itemId == R.id.menu_location) {
                    viewPager.setCurrentItem(1);
                } else if (itemId == R.id.menu_favorites) {
                    viewPager.setCurrentItem(2);
                } else if (itemId == R.id.menu_profile) {
                    viewPager.setCurrentItem(3);

                    // Tạo ProfileFragment
                    ProfileFragment profileFragment = new ProfileFragment();

                    // Tạo Bundle và truyền userKey
                    Bundle bundle = new Bundle();
                    bundle.putString("userKey", userKey); // Truyền userKey vào Bundle
                    profileFragment.setArguments(bundle);
                }
                return true;
            }
        });
    }

    public User getUser() {
        return user;  // `user` đã được khởi tạo trong `onCreate()`
    }
}
