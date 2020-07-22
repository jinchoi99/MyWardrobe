package com.example.mywardrobe.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mywardrobe.R;
import com.example.mywardrobe.adapters.ComposeOutfitAdapter;
import com.example.mywardrobe.adapters.OutfitsAdapter;
import com.example.mywardrobe.models.Category;
import com.example.mywardrobe.models.Outfit;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ComposeOutfitActivity extends AppCompatActivity {
    public static final String TAG = "ComposeOutfitActivity";
    private RecyclerView rvComposeOutfitsCategories;
    private ComposeOutfitAdapter adapter;
    private List<Category> allComposeOutfitsCategories;
    private Button btnAddOutfit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_outfit);

        btnAddOutfit = findViewById(R.id.btnAddOutfit);
        btnAddOutfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goOutfitsFragment();
            }
        });

        rvComposeOutfitsCategories = findViewById(R.id.rvComposeOutfitsCategories);
        allComposeOutfitsCategories = new ArrayList<>();
        adapter = new ComposeOutfitAdapter(this, allComposeOutfitsCategories);
        rvComposeOutfitsCategories.setAdapter(adapter);
        rvComposeOutfitsCategories.setLayoutManager(new LinearLayoutManager(this));
        queryComposeOutfitsCategories();
    }

    private void queryComposeOutfitsCategories() {
        ParseQuery<Category> query = ParseQuery.getQuery(Category.class);
        query.setLimit(20);
        query.whereEqualTo(Category.KEY_CATEGORY_OWNER, ParseUser.getCurrentUser());
        query.addDescendingOrder(Category.KEY_CAT_CREATED_KEY);
        query.findInBackground(new FindCallback<Category>() {
            @Override
            public void done(List<Category> composeOutfitsCategories, ParseException e) {
                if(e!=null){
                    Log.e(TAG, "Issue with getting categories",e);
                    return;
                }
                for(Category category : composeOutfitsCategories){
                    Log.i(TAG, "Category Name: " + category.getCategoryName());
                }
                allComposeOutfitsCategories.addAll(composeOutfitsCategories);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void goOutfitsFragment() {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("EXTRA", "open OutfitsFragment");
        startActivity(i);
        finish();
    }
}