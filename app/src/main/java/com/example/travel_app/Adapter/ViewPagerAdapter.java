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
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
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
                return new  ProfileFragment();
            default:
                return new HomeFragment();
        }

    }

    @Override
    public int getCount() {
        return 4;
    }
}
