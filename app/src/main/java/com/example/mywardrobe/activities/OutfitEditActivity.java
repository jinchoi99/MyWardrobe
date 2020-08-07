package com.example.mywardrobe.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

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
import com.parse.SaveCallback;

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

    private static List<Clothing> selectedClothings;
    private static List<CheckBox> selectedCheckboxes;
    private Button btnAddEditOutfit;
    private Button btnDoneEditOutfit;

    public static void makeSelection(View view, Clothing currentClothing) {
        if(((CheckBox)view).isChecked()){
            selectedClothings.add(currentClothing);
            selectedCheckboxes.add(((CheckBox)view));
        }
        else{
            selectedClothings.remove(currentClothing);
            selectedCheckboxes.remove(((CheckBox)view));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit_edit);

        selectedClothings = new ArrayList<>();
        selectedCheckboxes = new ArrayList<>();

        btnAddEditOutfit = findViewById(R.id.btnAddEditOutfit);
        btnAddEditOutfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnDoneEditOutfit = findViewById(R.id.btnDoneEditOutfit);
        btnDoneEditOutfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newOutfitName = etOutfitEditName.getText().toString();
                if(newOutfitName.isEmpty()){
                    Toast.makeText(OutfitEditActivity.this, "Outfit name can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentOutfit.setOutfitName(newOutfitName);
                currentOutfit.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e!=null){
                            Log.e(TAG, "Error while saving clothing",e);
                            Toast.makeText(OutfitEditActivity.this, "Error while saving clothing!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(OutfitEditActivity.this, "Outfit was edited successfully!", Toast.LENGTH_SHORT).show();
                    }
                });
                goOutfitDetails();
            }
        });

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

    private void goOutfitDetails() {
        Intent intent = new Intent(this, OutfitDetailsActivity.class);
        intent.putExtra("fromEditOutfit", Parcels.wrap(currentOutfit));
        startActivity(intent);
        finish();
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