package com.example.travel_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travel_app.Activity.DetailActivity;
import com.example.travel_app.Activity.HistoryActivity;
import com.example.travel_app.Domain.ItemDomain;
import com.example.travel_app.Fragment.FavoriteFragment;
import com.example.travel_app.databinding.ViewholderFavoriteBinding;
import com.google.gson.Gson;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder>{
    ArrayList<ItemDomain> items;
    Context context;
    ViewholderFavoriteBinding binding;
    OnFavoriteChangeListener favoriteChangeListener;

    public FavoriteAdapter(ArrayList<ItemDomain> items, FavoriteFragment favoriteFragment) {
        this.items = items;
    }
    public FavoriteAdapter(ArrayList<ItemDomain> items, HistoryActivity historyActivity) {
        this.items = items;
    }

    public interface OnFavoriteChangeListener {
        void onFavoriteChanged();
    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ViewholderFavoriteBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        context = parent.getContext();
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, int position) {
        ItemDomain currentItem = items.get(position);

        // Thiết lập thông tin cho View
        binding.titleTxt.setText(currentItem.getTitle());
        binding.priceTxt.setText("$" + currentItem.getPrice());
        binding.addressTxt.setText(currentItem.getAddress());
        binding.scoreTxt.setText("" + currentItem.getScore());

        // Thiết lập trạng thái nút like
        if (currentItem.isFavorite()) {
            binding.likeBtn.getDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        } else {
            binding.likeBtn.getDrawable().clearColorFilter();
        }

        // Khi nhấn nút like/unlike
        binding.likeBtn.setOnClickListener(v -> {
            currentItem.setFavorite(!currentItem.isFavorite()); // Đảo ngược trạng thái

            SharedPreferences sharedPreferences = context.getSharedPreferences("FavoriteItems", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            if (currentItem.isFavorite()) {
                editor.putString(currentItem.getTitle(), new Gson().toJson(currentItem)); // Lưu item vào SharedPreferences
                binding.likeBtn.getDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            } else {
                editor.remove(currentItem.getTitle()); // Xóa item khỏi SharedPreferences
                binding.likeBtn.getDrawable().clearColorFilter();
                items.remove(position); // Xóa item khỏi danh sách
                notifyItemRemoved(position); // Cập nhật RecyclerView
                notifyItemRangeChanged(position, items.size()); // Cập nhật các item còn lại
            }

            editor.apply();

            if (favoriteChangeListener != null) {
                favoriteChangeListener.onFavoriteChanged();
            }
        });

        Glide.with(context)
                .load(items.get(position).getPic())
                .into(binding.pic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("object", items.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(ViewholderFavoriteBinding binding) {
            super(binding.getRoot());
        }
    }
}
