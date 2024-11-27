package com.example.travel_app.Adapter;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.travel_app.Activity.MainActivity;
import com.example.travel_app.Domain.User;
import com.example.travel_app.Fragment.FavoriteFragment;
import com.example.travel_app.Fragment.HomeFragment;
import com.example.travel_app.Fragment.GiftFragment;
import com.example.travel_app.Fragment.ProfileFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final User user;
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, User user) {
        super(fm, behavior);
        this.user = user;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new GiftFragment();
            case 2:
                return new FavoriteFragment();
            case 3:
//                // Truyền user vào ProfileFragment bằng Bundle
//                ProfileFragment profileFragment = new ProfileFragment();
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("user", user);
//                profileFragment.setArguments(bundle);
//                return profileFragment;
                // Truyền user vào ProfileFragment bằng Bundle
                ProfileFragment profileFragment = new ProfileFragment();
                Bundle bundle = new Bundle();
             //   bundle.putParcelable("user", (Parcelable) user);

                bundle.putSerializable("user", user); // Sử dụng putParcelable
                profileFragment.setArguments(bundle);
                return profileFragment;
            default:
                return new HomeFragment();
        }

    }

    @Override
    public int getCount() {
        return 4;
    }
}
