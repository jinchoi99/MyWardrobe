package com.example.mywardrobe.fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mywardrobe.R;
import com.example.mywardrobe.activities.ComposeCategoryActivity;
import com.example.mywardrobe.adapters.CategoriesAdapter;
import com.example.mywardrobe.models.Category;
import com.example.mywardrobe.models.Clothing;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class CategoriesFragment extends Fragment {
    public static final String TAG = "CategoriesFragment";
    public static boolean deleteCategoryMode = false;
    public static boolean editCategoryMode = false;

    //Delete Category
    private RelativeLayout popUpDeleteDialog;
    private LinearLayout categoriesOverbox;
    private Button btnDeleteCatYes;
    private Button btnDeleteCatNo;
    Animation fromsmall;

    //RV
    private RecyclerView rvCategories;
    private CategoriesAdapter adapter;
    private List<Category> allCategories;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Check Click Listeners
        popUpDeleteDialog = view.findViewById(R.id.popUpDeleteDialog);
        categoriesOverbox = view.findViewById(R.id.categoriesOverbox);
        categoriesOverbox.setVisibility(View.GONE);

        btnDeleteCatYes = view.findViewById(R.id.btnDeleteCatYes);
        btnDeleteCatNo = view.findViewById(R.id.btnDeleteCatNo);

        fromsmall = AnimationUtils.loadAnimation(getContext(), R.anim.fromsmall);
        popUpDeleteDialog.setAlpha(0);

        CategoriesAdapter.OnCheckDeleteClickListener onCheckDeleteClickListener = new CategoriesAdapter.OnCheckDeleteClickListener() {
            @Override
            public void onCheckDeleteClicked(final int position, final CheckBox cb) {
                popUpDeleteDialog.setAlpha(1);
                popUpDeleteDialog.startAnimation(fromsmall);
                categoriesOverbox.setVisibility(View.VISIBLE);

                final Category currentCategory = allCategories.get(position);

                btnDeleteCatYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Remove Category
                        // Remove clothes with that category from Parse
                        removeClothesOfCategory(currentCategory.getCategoryName());

                        // Remove category from Parse
                        try {
                            currentCategory.delete();
                            Toast.makeText(view.getContext(), "successfully deleted category", Toast.LENGTH_SHORT).show();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        // Remove from categories list
                        allCategories.remove(position);

                        categoriesOverbox.setVisibility(View.GONE);
                        popUpDeleteDialog.setAlpha(0);
                        CategoriesFragment.deleteCategoryMode=false;
                        cb.setChecked(false);
                        adapter.notifyDataSetChanged();
                    }
                });

                //Cancel deletion
                btnDeleteCatNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popUpDeleteDialog.setAlpha(0);
                        categoriesOverbox.setVisibility(View.GONE);
                        CategoriesFragment.deleteCategoryMode=false;
                        cb.setChecked(false);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };

        //RV, Query
        rvCategories = view.findViewById(R.id.rvCategories);
        allCategories = new ArrayList<>();
        adapter = new CategoriesAdapter(getContext(), allCategories, onCheckDeleteClickListener);
        rvCategories.setAdapter(adapter);
        rvCategories.setLayoutManager(new GridLayoutManager(getContext(), 2));
        queryCategories();
    }

    private void removeClothesOfCategory(String categoryName) {
        ParseQuery<Clothing> query = ParseQuery.getQuery(Clothing.class);
        query.whereEqualTo(Clothing.KEY_CLOTHING_OWNER, ParseUser.getCurrentUser());
        query.whereEqualTo(Clothing.KEY_CLOTHING_CATEGORY, categoryName);
        query.findInBackground(new FindCallback<Clothing>() {
            @Override
            public void done(List<Clothing> clothesOfCategory, ParseException e) {
                if(e!=null){
                    Log.e(TAG, "Issue with getting clothes of the category",e);
                    return;
                }
                for(Clothing clothing : clothesOfCategory){
                    try {
                        clothing.delete();
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    protected void queryCategories() {
        ParseQuery<Category> query = ParseQuery.getQuery(Category.class);
        query.setLimit(20);
        query.whereEqualTo(Category.KEY_CATEGORY_OWNER, ParseUser.getCurrentUser());
        query.addDescendingOrder(Category.KEY_CAT_CREATED_KEY);
        query.findInBackground(new FindCallback<Category>() {
            @Override
            public void done(List<Category> categories, ParseException e) {
                if(e!=null){
                    Log.e(TAG, "Issue with getting categories",e);
                    return;
                }
                allCategories.addAll(categories);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_category, menu);

        menu.getItem(0).setIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        menu.getItem(1).setIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.composeCategory:
                Intent intent = new Intent(getActivity(), ComposeCategoryActivity.class);
                startActivity(intent);
                break;
            case R.id.deleteCategory:
                deleteCategoryMode = true;
                adapter.notifyDataSetChanged();
                break;
            case R.id.editCategory:
                Toast.makeText(getContext(), "edit category", Toast.LENGTH_SHORT).show();
                editCategoryMode = true;
                adapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}