package com.example.mywardrobe.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mywardrobe.R;
import com.example.mywardrobe.models.Clothing;
import com.example.mywardrobe.models.Outfit;

import org.parceler.Parcels;

public class OutfitEditActivity extends AppCompatActivity {
    Outfit currentOutfit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit_edit);

        currentOutfit = Parcels.unwrap(getIntent().getParcelableExtra("currentOutfit"));


    }
}