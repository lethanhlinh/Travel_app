package com.example.travel_app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Adapter.FavoriteAdapter;
import com.example.travel_app.Domain.ItemDomain;
import com.example.travel_app.R;
import com.example.travel_app.databinding.FragmentFavoriteBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteFragment extends Fragment implements FavoriteAdapter.OnFavoriteChangeListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentFavoriteBinding binding;
    private ArrayList<ItemDomain> favoriteItems;
    private FavoriteAdapter adapter;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Khởi tạo binding và inflate layout
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);

        // Khởi tạo danh sách
        favoriteItems = new ArrayList<>();
        // Gọi phương thức load dữ liệu
        loadFavoriteItems();

        // Thiết lập RecyclerView
        adapter = new FavoriteAdapter(favoriteItems, this);
        binding.recyclerViewFavorite.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerViewFavorite.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        favoriteItems.clear();
        loadFavoriteItems();
        adapter.notifyDataSetChanged();
    }

    private void loadFavoriteItems() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("FavoriteItems", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String json = (String) entry.getValue();
            ItemDomain item = new Gson().fromJson(json, ItemDomain.class);
            favoriteItems.add(item);
            binding.progressBarFavorite.setVisibility(getView().GONE);

        }

        // Nếu danh sách trống, hiển thị thông báo
        if (favoriteItems.isEmpty()) {
            Toast.makeText(getContext(), "Chưa có sản phẩm yêu thích!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFavoriteChanged() {
        loadFavoriteItems();
        adapter.notifyDataSetChanged();
    }
}