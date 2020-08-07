package com.example.mywardrobe.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.mywardrobe.R;
import com.example.mywardrobe.adapters.ClothesRelationsAdapter;
import com.example.mywardrobe.adapters.OutfitDetailsClothesAdapter;
import com.example.mywardrobe.models.Clothing;
import com.example.mywardrobe.models.Outfit;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class OutfitDetailsActivity extends AppCompatActivity {
    Toolbar outfitDetailsToolbar;
    Outfit currentOutfit;
    TextView tvOutfitDetailsName;

    private RecyclerView rvOutfitDetailsClothes;
    private OutfitDetailsClothesAdapter adapter;
    private List<Clothing> allClothesRelations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit_details);

        currentOutfit = Parcels.unwrap(getIntent().getParcelableExtra("fromOutfits"));
//        if(getIntent().hasExtra("fromOutfits"))
//        {
//            currentOutfit = Parcels.unwrap(getIntent().getParcelableExtra("fromOutfits"));
//        }
////        else if(getIntent().hasExtra("fromEditOutfit"))
////        {
////            currentOutfit = Parcels.unwrap(getIntent().getParcelableExtra("fromEditOutfit"));
////        }
//
        outfitDetailsToolbar = findViewById(R.id.outfitDetailsToolbar);
        setSupportActionBar(outfitDetailsToolbar);
        getSupportActionBar().setTitle(currentOutfit.getOutfitName());
        outfitDetailsToolbar.setTitleTextColor(Color.WHITE);
        tvOutfitDetailsName = findViewById(R.id.tvOutfitDetailsName);
        tvOutfitDetailsName.setText(currentOutfit.getOutfitName());

        rvOutfitDetailsClothes = findViewById(R.id.rvOutfitDetailsClothes);
        allClothesRelations = new ArrayList<>();
        adapter = new OutfitDetailsClothesAdapter(this, allClothesRelations);
        rvOutfitDetailsClothes.setAdapter(adapter);
        rvOutfitDetailsClothes.setLayoutManager(new GridLayoutManager(this, 3));
        ParseQuery queryRelations = currentOutfit.getClothingRelation().getQuery();
        queryRelations.findInBackground(new FindCallback<Clothing>() {
            @Override
            public void done(List<Clothing> clothesRelations, ParseException e) {
                if(e!=null){
                    return;
                }
                allClothesRelations.addAll(clothesRelations);
                adapter.notifyDataSetChanged();
            }
        });
    }
}