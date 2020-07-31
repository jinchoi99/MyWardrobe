package com.example.mywardrobe.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;

import com.example.mywardrobe.R;
import com.example.mywardrobe.models.Clothing;

import org.parceler.Parcels;

public class ClothingDetailsActivity extends AppCompatActivity {
    Toolbar clothingDetailsToolbar;
    Clothing currentClothing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothing_details);

        currentClothing = Parcels.unwrap(getIntent().getParcelableExtra("clothing"));

        clothingDetailsToolbar = findViewById(R.id.clothesToolbar);
        setSupportActionBar(clothingDetailsToolbar);
        getSupportActionBar().setTitle(currentClothing.getClothingName());
        clothingDetailsToolbar.setTitleTextColor(Color.WHITE);
    }
}