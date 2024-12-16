package com.example.travel_app.Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.travel_app.Domain.User;
import com.example.travel_app.Fragment.FavoriteFragment;
import com.example.travel_app.Fragment.GiftFragment;
import com.example.travel_app.Fragment.HomeFragment;
import com.example.travel_app.Fragment.ProfileFragment;
import com.example.travel_app.GiftFragment.DoiQuaFragment;
import com.example.travel_app.GiftFragment.TichXuFragment;

public class GiftViewPagerAdapter extends FragmentStatePagerAdapter {
    private final User user;
    public GiftViewPagerAdapter(@NonNull FragmentManager fm, int behavior, User user) {
        super(fm, behavior);
        this.user = user;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            TichXuFragment tichXuFragment = new TichXuFragment();
            Bundle tichXubundle = new Bundle();
            tichXubundle.putSerializable("user", user);
            tichXuFragment.setArguments(tichXubundle);
            return tichXuFragment;
        } else if (position == 1) {
            DoiQuaFragment doiQuaFragment = new DoiQuaFragment();
            Bundle doiQuaBundle = new Bundle();
            doiQuaBundle.putSerializable("user", user);
            doiQuaFragment.setArguments(doiQuaBundle);
            return doiQuaFragment;
        } else {
            throw new IllegalArgumentException("Invalid position: " + position);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Tích Xu";
            case 1:
                return "Đổi quà";
            default:
                return "Tích Xu";
        }
    }
}
