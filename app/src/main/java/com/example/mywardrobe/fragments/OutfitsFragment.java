package com.example.mywardrobe.fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.mywardrobe.activities.ComposeOutfitActivity;
import com.example.mywardrobe.adapters.OutfitsAdapter;
import com.example.mywardrobe.models.Category;
import com.example.mywardrobe.models.Clothing;
import com.example.mywardrobe.models.Outfit;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OutfitsFragment extends Fragment {
    public static final String TAG = "OutfitsFragment";
    private RecyclerView rvOutfits;
    private OutfitsAdapter adapter;
    private List<Outfit> allOutfits;

    public OutfitsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_outfits, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        rvOutfits = view.findViewById(R.id.rvOutfits);
        allOutfits = new ArrayList<>();
        adapter = new OutfitsAdapter(getContext(), allOutfits);
        rvOutfits.setAdapter(adapter);
        rvOutfits.setLayoutManager(new LinearLayoutManager(getContext()));
        queryOutfits();
    }

    private void queryOutfits() {
        ParseQuery<Outfit> query = ParseQuery.getQuery(Outfit.class);
        query.include(Outfit.KEY_OUTFIT_CLOTHES);
        query.setLimit(20);
        query.whereEqualTo(Outfit.KEY_OUTFIT_OWNER, ParseUser.getCurrentUser());
        query.addDescendingOrder(Outfit.KEY_OUTFIT_CREATED_KEY);
        query.findInBackground(new FindCallback<Outfit>() {
            @Override
            public void done(List<Outfit> outfits, ParseException e) {
                if(e!=null){
                    Log.e(TAG, "Issue with getting outfits",e);
                    return;
                }
                allOutfits.addAll(outfits);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_outfit, menu);
        menu.getItem(0).setIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.composeOutfit){
            Toast.makeText(getContext(), "compose new outfit", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), ComposeOutfitActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}