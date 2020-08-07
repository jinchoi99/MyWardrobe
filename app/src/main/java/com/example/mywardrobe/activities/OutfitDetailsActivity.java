package com.example.mywardrobe.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;

import com.example.mywardrobe.R;
import com.example.mywardrobe.models.Outfit;

import org.parceler.Parcels;

public class OutfitDetailsActivity extends AppCompatActivity {
    Toolbar outfitDetailsToolbar;
    Outfit currentOutfit;

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
    }
}