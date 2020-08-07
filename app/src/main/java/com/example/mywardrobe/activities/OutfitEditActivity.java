package com.example.mywardrobe.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.example.mywardrobe.R;
import com.example.mywardrobe.adapters.AddOptionClothesCategoriesAdapter;
import com.example.mywardrobe.adapters.ComposeOutfitAdapter;
import com.example.mywardrobe.adapters.OutfitDetailsClothesAdapter;
import com.example.mywardrobe.models.Category;
import com.example.mywardrobe.models.Clothing;
import com.example.mywardrobe.models.Outfit;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class OutfitEditActivity extends AppCompatActivity {
    public static final String TAG = "OutfitEditActivity";
    Outfit currentOutfit;
    EditText etOutfitEditName;

    //Current Clothes
    private RecyclerView rvOutfitEditCurrentClothes;
    private OutfitDetailsClothesAdapter currClothesAdapter;
    public static List<Clothing> allClothesCurrentRelations;

    //Add Option Clothes
    RecyclerView rvOutfitsEditCategories;
    private AddOptionClothesCategoriesAdapter addClothesCategoriesAdapter;
    private List<Category> allAddOptionCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit_edit);

        //Name
        currentOutfit = Parcels.unwrap(getIntent().getParcelableExtra("currentOutfit"));
        etOutfitEditName = findViewById(R.id.etOutfitEditName);
        etOutfitEditName.setText(currentOutfit.getOutfitName());

        //Current Clothes List
        rvOutfitEditCurrentClothes = findViewById(R.id.rvOutfitEditCurrentClothes);
        allClothesCurrentRelations = new ArrayList<>();
        currClothesAdapter = new OutfitDetailsClothesAdapter(this, allClothesCurrentRelations);
        rvOutfitEditCurrentClothes.setAdapter(currClothesAdapter);
        rvOutfitEditCurrentClothes.setLayoutManager(new GridLayoutManager(this, 3));
        ParseQuery queryRelations = currentOutfit.getClothingRelation().getQuery();
        queryRelations.findInBackground(new FindCallback<Clothing>() {
            @Override
            public void done(List<Clothing> clothesRelations, ParseException e) {
                if(e!=null){
                    return;
                }
                allClothesCurrentRelations.addAll(clothesRelations);
                currClothesAdapter.notifyDataSetChanged();
            }
        });

        //Add Option Clothes Categories
        rvOutfitsEditCategories = findViewById(R.id.rvOutfitsEditCategories);
        allAddOptionCategories = new ArrayList<>();
        addClothesCategoriesAdapter = new AddOptionClothesCategoriesAdapter(this, allAddOptionCategories);
        rvOutfitsEditCategories.setAdapter(addClothesCategoriesAdapter);
        rvOutfitsEditCategories.setLayoutManager(new LinearLayoutManager(this));
        queryOutfitsEditCategories();
    }

    private void queryOutfitsEditCategories() {
        ParseQuery<Category> query = ParseQuery.getQuery(Category.class);
        query.whereEqualTo(Category.KEY_CATEGORY_OWNER, ParseUser.getCurrentUser());
        query.addDescendingOrder(Category.KEY_CAT_CREATED_KEY);
        query.findInBackground(new FindCallback<Category>() {
            @Override
            public void done(List<Category> editOutfitsCategories, ParseException e) {
                if(e!=null){
                    Log.e(TAG, "Issue with getting categories",e);
                    return;
                }
                allAddOptionCategories.addAll(editOutfitsCategories);
                addClothesCategoriesAdapter.notifyDataSetChanged();
            }
        });
    }
}