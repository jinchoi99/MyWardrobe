package com.example.mywardrobe.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mywardrobe.R;
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

public class ClothesActivity extends AppCompatActivity {
    public static final String TAG = "ClothesActivity";
    Category currentCategory;

    private RecyclerView rvClothesList;
    private ClothesAdapter adapter;
    private List<Clothing> allClothes;

    Toolbar clothesToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes);

        clothesToolbar = findViewById(R.id.clothesToolbar);
        setSupportActionBar(clothesToolbar);

        rvClothesList = findViewById(R.id.rvClothesList);
        allClothes = new ArrayList<>();
        adapter = new ClothesAdapter(this, allClothes);
        rvClothesList.setAdapter(adapter);
        rvClothesList.setLayoutManager(new GridLayoutManager(this, 3));

        if(getIntent().hasExtra("fromCategories"))
        {
            currentCategory = Parcels.unwrap(getIntent().getParcelableExtra("fromCategories"));
        }
        else if(getIntent().hasExtra("fromCompose"))
        {
            currentCategory = Parcels.unwrap(getIntent().getParcelableExtra("fromCompose"));
        }

        getSupportActionBar().setTitle(currentCategory.getCategoryName());
        clothesToolbar.setTitleTextColor(Color.WHITE);

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_clothing, menu);
        menu.getItem(0).setIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.composeClothing){
            Toast.makeText(this, "compose new clothing", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ComposeClothingActivity.class);
            intent.putExtra("categoryName", Parcels.wrap(currentCategory));
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}