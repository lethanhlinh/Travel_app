package com.example.travel_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travel_app.Activity.DetailActivity;
import com.example.travel_app.Domain.ItemDomain;
import com.example.travel_app.databinding.ViewholderRecommendedBinding;

import java.util.ArrayList;

public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.ViewHolder> {
     ArrayList<ItemDomain> items;
     Context context;
    ViewholderRecommendedBinding binding;
    public RecommendedAdapter(ArrayList<ItemDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecommendedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       binding = ViewholderRecommendedBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        context = parent.getContext();
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       // ItemDomain item = items.get(position); // Lấy item tại vị trí hiện tại

        binding.titleTxt.setText(items.get(position).getTitle());
        binding.priceTxt.setText("$" + items.get(position).getPrice());
        binding.addressTxt.setText(items.get(position).getAddress());
        binding.scoreTxt.setText("" + items.get(position).getScore());

//        holder.binding.titleTxt.setText(item.getTitle());
//        holder.binding.priceTxt.setText("$" + item.getPrice());
//        holder.binding.addressTxt.setText(item.getAddress());
//        holder.binding.scoreTxt.setText("" + item.getScore());

        Glide.with(context)
                .load(items.get(position).getPic())
                .into(binding.pic);

        // Xử lý sự kiện khi item được nhấn
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int currentPosition = holder.getAdapterPosition(); // Lấy vị trí chính xác tại thời điểm bấm
//                if (currentPosition != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("object", items.get(position)); // Truyền dữ liệu item sang DetailActivity
                    context.startActivity(intent);
         //       }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(ViewholderRecommendedBinding binding) {
            super(binding.getRoot());

        }
    }
}
