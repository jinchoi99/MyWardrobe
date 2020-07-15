package com.example.mywardrobe.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mywardrobe.R;
import com.example.mywardrobe.models.Category;
import com.example.mywardrobe.models.Clothing;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class ClothesFragment extends Fragment {
    public static final String TAG = "ClothesFragment";
    Category currentCategory;

    public ClothesFragment() {
        // Required empty public constructor
    }

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
        //currentCategory = Parcels.unwrap(getArguments().getParcelable("currentCategory"));
        queryClothes();
    }

    protected void queryClothes() {
        ParseQuery<Clothing> query = ParseQuery.getQuery(Clothing.class);
        query.setLimit(20);
        //query.whereEqualTo(Clothing.KEY_CLOTHING_OWNER, ParseUser.getCurrentUser());
        //query.whereEqualTo(Clothing.KEY_CLOTHING_CATEGORY, currentCategory.getCategoryName());
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
            }
        });
    }
}