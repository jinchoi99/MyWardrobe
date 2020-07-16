package com.example.mywardrobe.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mywardrobe.R;
import com.example.mywardrobe.activities.ComposeCategoryActivity;
import com.example.mywardrobe.activities.ComposeClothingActivity;
import com.example.mywardrobe.adapters.CategoriesAdapter;
import com.example.mywardrobe.adapters.ClothesAdapter;
import com.example.mywardrobe.models.Category;
import com.example.mywardrobe.models.Clothing;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ClothesFragment extends Fragment {
    public static final String TAG = "ClothesFragment";
    Category currentCategory;

    private RecyclerView rvClothes;
    private ClothesAdapter adapter;
    private List<Clothing> allClothes;

    public ClothesFragment() {
        // Required empty public constructor
    }

    //Fragment with argument
    public static ClothesFragment newInstance(Category currentCategory) {
        ClothesFragment fragmentClothes = new ClothesFragment();
        Bundle args = new Bundle();
        args.putParcelable("currentCategory", Parcels.wrap(currentCategory));
        fragmentClothes.setArguments(args);
        return fragmentClothes;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clothes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvClothes = view.findViewById(R.id.rvClothes);
        allClothes = new ArrayList<>();
        adapter = new ClothesAdapter(getContext(), allClothes);
        rvClothes.setAdapter(adapter);
        rvClothes.setLayoutManager(new GridLayoutManager(getContext(), 3));
        currentCategory = Parcels.unwrap(getArguments().getParcelable("currentCategory"));
        queryClothes();
    }

    protected void queryClothes() {
        ParseQuery<Clothing> query = ParseQuery.getQuery(Clothing.class);
        query.setLimit(20);
        query.whereEqualTo(Clothing.KEY_CLOTHING_OWNER, ParseUser.getCurrentUser());
        query.whereEqualTo(Clothing.KEY_CLOTHING_CATEGORY, currentCategory.getCategoryName());
        query.addDescendingOrder(Clothing.KEY_CLOTHING_CREATED_KEY);
        query.findInBackground(new FindCallback<Clothing>() {
            @Override
            public void done(List<Clothing> clothes, ParseException e) {
                if(e!=null){
                    Log.e(TAG, "Issue with getting clothes",e);
                    return;
                }
                for(Clothing clothing : clothes){
                    Log.i(TAG, "Clothing Name: " + clothing.getClothingName());
                }
                allClothes.addAll(clothes);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_clothing, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.composeClothing){
            Toast.makeText(getContext(), "compose new clothing", Toast.LENGTH_SHORT).show();
            //getActivity = MainActivity since that's where this fragment lies upon
            Intent intent = new Intent(getActivity(), ComposeClothingActivity.class);
            intent.putExtra("categoryName", Parcels.wrap(currentCategory));
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}