package com.example.travel_app.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.travel_app.Fragment.FavoriteFragment;
import com.example.travel_app.Fragment.GiftFragment;
import com.example.travel_app.Fragment.HomeFragment;
import com.example.travel_app.Fragment.ProfileFragment;
import com.example.travel_app.GiftFragment.DoiQuaFragment;
import com.example.travel_app.GiftFragment.TichXuFragment;

public class GiftViewPagerAdapter extends FragmentStatePagerAdapter {
    public GiftViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new TichXuFragment();
            case 1:
                return new DoiQuaFragment();
            default:
                return new TichXuFragment();
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
