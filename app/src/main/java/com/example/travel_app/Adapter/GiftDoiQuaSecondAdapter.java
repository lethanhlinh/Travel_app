package com.example.travel_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travel_app.Activity.GiftDetailActivity;
import com.example.travel_app.Domain.Gift;
import com.example.travel_app.Domain.User;
import com.example.travel_app.databinding.ViewholderDoiquaBinding;
import com.example.travel_app.databinding.ViewholderDoiquasecondBinding;

import java.util.ArrayList;

public class GiftDoiQuaSecondAdapter extends RecyclerView.Adapter<GiftDoiQuaSecondAdapter.ViewHolder>  {
    ArrayList<Gift> items;
    Context context;
    ViewholderDoiquasecondBinding binding;
    private User user;
    public GiftDoiQuaSecondAdapter(ArrayList<Gift> items, User user) {
        this.items = items;
        this.user = user;
    }

    @NonNull
    @Override
    public GiftDoiQuaSecondAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ViewholderDoiquasecondBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        context = parent.getContext();
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(items.get(position).getPic())
                .into(binding.pic);

        binding.txtDescription.setText(items.get(position).getDescription());


        // Xử lý sự kiện khi item được nhấn
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GiftDetailActivity.class);
                intent.putExtra("object", items.get(position)); // Truyền dữ liệu item sang DetailActivity
                intent.putExtra("user", user); // Truyền dữ liệu user sang DetailActivity")
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(ViewholderDoiquasecondBinding binding) {
            super(binding.getRoot());

        }
    }
}
