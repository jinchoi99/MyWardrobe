package com.example.mywardrobe.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mywardrobe.R;
import com.example.mywardrobe.adapters.CategoriesAdapter;
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
    private TextView tvNoClothingMessage;

    Toolbar clothesToolbar;

    //pull-to-refresh
    private SwipeRefreshLayout swipeContainer;
    //Progress Bar
    ProgressBar pbLoadingClothes;

    //Delete Clothing
    public static boolean deleteClothingMode = false;
    private RelativeLayout rlPopUpDeleteClothingDialog;
    private LinearLayout clothesOverbox;
    private Button btnDeleteClothingYes;
    private Button btnDeleteClothingNo;
    private TextView tvDeleteClothingMessage;
    Animation fromsmall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes);

        deleteClothingMode = false;
        pbLoadingClothes= (ProgressBar) findViewById(R.id.pbLoadingClothes);

        // Delete Clothing
        fromsmall = AnimationUtils.loadAnimation(this, R.anim.fromsmall);
        clothesOverbox = findViewById(R.id.clothesOverbox);
        clothesOverbox.setAlpha(0);
        rlPopUpDeleteClothingDialog = findViewById(R.id.rlPopUpDeleteClothingDialog);
        btnDeleteClothingYes = findViewById(R.id.btnDeleteClothingYes);
        btnDeleteClothingNo = findViewById(R.id.btnDeleteClothingNo);
        tvDeleteClothingMessage = findViewById(R.id.tvDeleteClothingMessage);
        rlPopUpDeleteClothingDialog.setVisibility(View.GONE);

        ClothesAdapter.OnCheckDeleteClickListener onCheckDeleteClickListener = new ClothesAdapter.OnCheckDeleteClickListener() {
            @Override
            public void onCheckDeleteClicked(final int position, final CheckBox cb) {
                rlPopUpDeleteClothingDialog.setAlpha(1);
                rlPopUpDeleteClothingDialog.setVisibility(View.VISIBLE);
                rlPopUpDeleteClothingDialog.startAnimation(fromsmall);
                clothesOverbox.animate().alpha(1.0f).setDuration(800);

                final Clothing currentClothing = allClothes.get(position);
                tvDeleteClothingMessage.setText("Are you sure you want to delete \"" + currentClothing.getClothingName() + "\" ?");

                //Remove Clothing
                btnDeleteClothingYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Remove clothing from Parse
                        try {
                            currentClothing.delete();
                            Toast.makeText(view.getContext(), "successfully deleted clothing", Toast.LENGTH_SHORT).show();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        // Remove from clothes list
                        allClothes.remove(position);

                        cb.setChecked(false);
                        closeDeleteDialog();
                    }
                });

                //Cancel deletion
                btnDeleteClothingNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cb.setChecked(false);
                        closeDeleteDialog();
                    }
                });
            }
        };

        clothesToolbar = findViewById(R.id.clothesToolbar);
        setSupportActionBar(clothesToolbar);

        tvNoClothingMessage = findViewById(R.id.tvNoClothingMessage);

        rvClothesList = findViewById(R.id.rvClothesList);
        allClothes = new ArrayList<>();
        adapter = new ClothesAdapter(this, allClothes, onCheckDeleteClickListener);
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

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                deleteClothingMode = false;
                adapter.clear();
                queryClothes();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void closeDeleteDialog() {
        clothesOverbox.animate().alpha(0.0f).setDuration(500);
        rlPopUpDeleteClothingDialog.animate().alpha(0.0f).setDuration(500);
        rlPopUpDeleteClothingDialog.setVisibility(View.GONE);
        deleteClothingMode=false;
        adapter.notifyDataSetChanged();
    }

    protected void queryClothes() {
        pbLoadingClothes.setVisibility(ProgressBar.VISIBLE);
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
                    pbLoadingClothes.setVisibility(ProgressBar.INVISIBLE);
                    return;
                }
                allClothes.addAll(clothes);
                if(allClothes.isEmpty()){
                    tvNoClothingMessage.setVisibility(View.VISIBLE);
                }
                else {
                    tvNoClothingMessage.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
                pbLoadingClothes.setVisibility(ProgressBar.INVISIBLE);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_clothing, menu);
        menu.getItem(0).setIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        menu.getItem(1).setIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.deleteClothing:
                if(allClothes.size()!=0)
                {
                    if(deleteClothingMode){
                        deleteClothingMode = false;
                    }
                    else{
                        deleteClothingMode = true;
                    }
                    adapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(this, "There is no clothing yet!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.composeClothing:
                Toast.makeText(this, "compose new clothing", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ComposeClothingActivity.class);
                intent.putExtra("categoryName", Parcels.wrap(currentCategory));
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}