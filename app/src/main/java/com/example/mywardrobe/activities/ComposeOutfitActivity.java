package com.example.mywardrobe.activities;

import androidx.appcompat.app.AppCompatActivity;
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
import com.example.mywardrobe.adapters.ComposeOutfitAdapter;
import com.example.mywardrobe.adapters.OutfitsAdapter;
import com.example.mywardrobe.models.Category;
import com.example.mywardrobe.models.Clothing;
import com.example.mywardrobe.models.Outfit;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ComposeOutfitActivity extends AppCompatActivity {
    public static final String TAG = "ComposeOutfitActivity";
    private RecyclerView rvComposeOutfitsCategories;
    private ComposeOutfitAdapter adapter;
    private List<Category> allComposeOutfitsCategories;
    private Button btnAddOutfit;
    private Button btnDoneComposeOutfit;
    private EditText etNewOutfitName;

    private static List<Clothing> selectedClothings;
    private static List<CheckBox> selectedCheckboxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_outfit);

        etNewOutfitName = findViewById(R.id.etNewOutfitName);

        rvComposeOutfitsCategories = findViewById(R.id.rvComposeOutfitsCategories);
        allComposeOutfitsCategories = new ArrayList<>();
        adapter = new ComposeOutfitAdapter(this, allComposeOutfitsCategories);
        rvComposeOutfitsCategories.setAdapter(adapter);
        rvComposeOutfitsCategories.setLayoutManager(new LinearLayoutManager(this));
        queryComposeOutfitsCategories();

        selectedClothings = new ArrayList<>();
        selectedCheckboxes = new ArrayList<>();

        btnAddOutfit = findViewById(R.id.btnAddOutfit);
        btnAddOutfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newOutfitName = etNewOutfitName.getText().toString();
                if(newOutfitName.isEmpty()){
                    Toast.makeText(ComposeOutfitActivity.this, "New Outfit name can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser outfitOwner = ParseUser.getCurrentUser();
                saveNewOutfit(newOutfitName, outfitOwner);
            }
        });

        btnDoneComposeOutfit = findViewById(R.id.btnDoneComposeOutfit);
        btnDoneComposeOutfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goOutfitsFragment();
            }
        });
    }

    public static void makeSelection(View view, Clothing currentClothing) {
        if(((CheckBox)view).isChecked()){
            selectedClothings.add(currentClothing);
            selectedCheckboxes.add(((CheckBox)view));
        }
        else{
            selectedClothings.remove(currentClothing);
            selectedCheckboxes.remove(((CheckBox)view));
        }
        Log.i(TAG, selectedClothings.toString());
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

    private void saveNewOutfit(String newOutfitName, ParseUser outfitOwner) {
        Outfit newOutfit = new Outfit();
        newOutfit.setOutfitName(newOutfitName);
        newOutfit.setOutfitOwner(outfitOwner);
        newOutfit.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e!=null){
                    Log.e(TAG, "Error while saving new outfit",e);
                    Toast.makeText(ComposeOutfitActivity.this, "Error while saving new outfit!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i(TAG, "New Outfit was saved successfully!");
                etNewOutfitName.setText("");
            }
        });
        for (int i = 0; i < selectedClothings.size(); i++) {
            newOutfit.addClothing(selectedClothings.get(i));
            selectedCheckboxes.get(i).setChecked(false);
        }
        selectedClothings.clear();
        selectedCheckboxes.clear();
        Log.i(TAG, "selectedClothings is empty: " + selectedClothings.toString());
        Toast.makeText(ComposeOutfitActivity.this, "New outfit was saved successfully!", Toast.LENGTH_SHORT).show();
    }

    private void goOutfitsFragment() {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("EXTRA", "open OutfitsFragment");
        startActivity(i);
        finish();
    }
}