package com.example.mywardrobe.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mywardrobe.R;
import com.example.mywardrobe.models.Clothing;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class ClothingDetailsActivity extends AppCompatActivity {
    Toolbar clothingDetailsToolbar;
    Clothing currentClothing;
    TextView tvClothingDetailsName;
    ImageView ivClothingDetailsImage;
    TextView tvClothingDetailsDescription;
    TextView tvClothingDetailsPrice;
    TextView tvClothingDetailsBrand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothing_details);

        currentClothing = Parcels.unwrap(getIntent().getParcelableExtra("clothing"));

        clothingDetailsToolbar = findViewById(R.id.clothesToolbar);
        setSupportActionBar(clothingDetailsToolbar);
        getSupportActionBar().setTitle(currentClothing.getClothingName());
        clothingDetailsToolbar.setTitleTextColor(Color.WHITE);

        tvClothingDetailsName = findViewById(R.id.tvClothingDetailsName);
        ivClothingDetailsImage = findViewById(R.id.ivClothingDetailsImage);
        tvClothingDetailsDescription = findViewById(R.id.tvClothingDetailsDescription);
        tvClothingDetailsPrice = findViewById(R.id.tvClothingDetailsPrice);
        tvClothingDetailsBrand = findViewById(R.id.tvClothingDetailsBrand);

        tvClothingDetailsName.setText(currentClothing.getClothingName());
        ParseFile image = currentClothing.getClothingImage();
        if(image!=null){
            Glide.with(this).load(image.getUrl()).into(ivClothingDetailsImage);
        }
        tvClothingDetailsDescription.setText(currentClothing.getClothingDescription());
        tvClothingDetailsPrice.setText("$"+currentClothing.getClothingPrice());
        tvClothingDetailsBrand.setText(currentClothing.getClothingBrand());
    }
}