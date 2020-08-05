package com.example.mywardrobe.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    TextView tvClothingDetailsBrandPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothing_details);

        ProgressBar pbLoadingClothingDetails= (ProgressBar) findViewById(R.id.pbLoadingClothingDetails);
        pbLoadingClothingDetails.setVisibility(View.VISIBLE);

        if(getIntent().hasExtra("fromClothes"))
        {
            currentClothing = Parcels.unwrap(getIntent().getParcelableExtra("fromClothes"));
        }
        else if(getIntent().hasExtra("fromEditClothing"))
        {
            currentClothing = Parcels.unwrap(getIntent().getParcelableExtra("fromEditClothing"));
        }

        clothingDetailsToolbar = findViewById(R.id.clothesToolbar);
        setSupportActionBar(clothingDetailsToolbar);
        getSupportActionBar().setTitle(currentClothing.getClothingCategoryName() + " / " + currentClothing.getClothingName());
        clothingDetailsToolbar.setTitleTextColor(Color.WHITE);

        tvClothingDetailsName = findViewById(R.id.tvClothingDetailsName);
        ivClothingDetailsImage = findViewById(R.id.ivClothingDetailsImage);
        tvClothingDetailsDescription = findViewById(R.id.tvClothingDetailsDescription);
        tvClothingDetailsBrandPrice = findViewById(R.id.tvClothingDetailsBrandPrice);

        tvClothingDetailsName.setText(currentClothing.getClothingName());
        ParseFile image = currentClothing.getClothingImage();
        if(image!=null){
            Glide.with(this).load(image.getUrl()).into(ivClothingDetailsImage);
        }
        tvClothingDetailsDescription.setText(currentClothing.getClothingDescription());
        tvClothingDetailsBrandPrice.setText(currentClothing.getClothingBrand() + " | $"+currentClothing.getClothingPrice());
        pbLoadingClothingDetails.setVisibility(View.INVISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_clothingdetails, menu);
        menu.getItem(0).setIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.editClothing){
            Intent intent = new Intent(this, ClothingEditActivity.class);
            intent.putExtra("clothing", Parcels.wrap(currentClothing));
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}