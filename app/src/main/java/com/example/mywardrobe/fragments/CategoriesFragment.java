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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mywardrobe.R;
import com.example.mywardrobe.activities.ComposeCategoryActivity;
import com.example.mywardrobe.activities.ComposeClothingActivity;
import com.example.mywardrobe.adapters.CategoriesAdapter;
import com.example.mywardrobe.models.Category;
import com.example.mywardrobe.models.Clothing;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class CategoriesFragment extends Fragment {
    public static final String TAG = "CategoriesFragment";
    public static boolean deleteCategoryMode = false;
    public static boolean editCategoryMode = false;

    //Delete Category
    private RelativeLayout rlPopUpDeleteCategoryDialog;
    private LinearLayout categoriesOverbox;
    private Button btnDeleteCategoryYes;
    private Button btnDeleteCategoryNo;
    private TextView tvDeleteCategoryMessage;
    //Edit Category
    private RelativeLayout rlPopUpEditCategoryDialog;
    private Button btnEditCategorySave;
    private Button btnEditCategoryCancel;
    private EditText etNewCategoryName;
    Animation fromsmall;

    //RV
    private RecyclerView rvCategories;
    private CategoriesAdapter adapter;
    private List<Category> allCategories;
    private TextView tvNoCategoryMessage;

    //pull-to-refresh
    private SwipeRefreshLayout swipeContainer;
    //Progress Bar
    ProgressBar pbLoadingCategories;

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

        editCategoryMode = false;
        deleteCategoryMode = false;
        pbLoadingCategories= (ProgressBar) view.findViewById(R.id.pbLoadingCategories);

        categoriesOverbox = view.findViewById(R.id.categoriesOverbox);
        categoriesOverbox.setAlpha(0);
        fromsmall = AnimationUtils.loadAnimation(getContext(), R.anim.fromsmall);

        // Delete Category
        rlPopUpDeleteCategoryDialog = view.findViewById(R.id.rlPopUpDeleteCategoryDialog);
        btnDeleteCategoryYes = view.findViewById(R.id.btnDeleteCategoryYes);
        btnDeleteCategoryNo = view.findViewById(R.id.btnDeleteCategoryNo);
        tvDeleteCategoryMessage = view.findViewById(R.id.tvDeleteCategoryMessage);
        rlPopUpDeleteCategoryDialog.setVisibility(View.GONE);

        CategoriesAdapter.OnCheckDeleteClickListener onCheckDeleteClickListener = new CategoriesAdapter.OnCheckDeleteClickListener() {
            @Override
            public void onCheckDeleteClicked(final int position, final CheckBox cb) {
                rlPopUpDeleteCategoryDialog.setAlpha(1);
                rlPopUpDeleteCategoryDialog.setVisibility(View.VISIBLE);
                rlPopUpDeleteCategoryDialog.startAnimation(fromsmall);
                categoriesOverbox.animate().alpha(1.0f).setDuration(800);

                final Category currentCategory = allCategories.get(position);
                tvDeleteCategoryMessage.setText("Are you sure you want to delete \"" + currentCategory.getCategoryName() + "\" ?");

                //Remove Category
                btnDeleteCategoryYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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

                        cb.setChecked(false);
                        closeDeleteDialog();
                    }
                });

                //Cancel deletion
                btnDeleteCategoryNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cb.setChecked(false);
                        closeDeleteDialog();
                    }
                });
            }
        };

        // Edit Category
        rlPopUpEditCategoryDialog = view.findViewById(R.id.rlPopUpEditCategoryDialog);
        btnEditCategorySave = view.findViewById(R.id.btnEditCategorySave);
        btnEditCategoryCancel = view.findViewById(R.id.btnEditCategoryCancel);
        etNewCategoryName = view.findViewById(R.id.etNewCategoryName);
        rlPopUpEditCategoryDialog.setVisibility(View.GONE);

        CategoriesAdapter.OnCheckEditClickListener onCheckEditClickListener = new CategoriesAdapter.OnCheckEditClickListener() {
            @Override
            public void onCheckEditClicked(final int position, final CheckBox cb) {
                rlPopUpEditCategoryDialog.setAlpha(1);
                rlPopUpEditCategoryDialog.setVisibility(View.VISIBLE);
                rlPopUpEditCategoryDialog.startAnimation(fromsmall);
                categoriesOverbox.animate().alpha(1.0f).setDuration(800);

                final Category currentCategory = allCategories.get(position);
                etNewCategoryName.setText(currentCategory.getCategoryName());

                //Save New CategoryName
                btnEditCategorySave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String newCategoryName = etNewCategoryName.getText().toString();

                        //save new category name for all clothes with that category name in Parse
                        changeClothesOfCategory(currentCategory.getCategoryName(), newCategoryName);

                        //save new category name in Parse
                        currentCategory.setCategoryName(newCategoryName);
                        currentCategory.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e!=null){
                                    Log.e(TAG, "Error while saving category",e);
                                    Toast.makeText(getContext(), "Error while saving category!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Log.i(TAG, "Category was saved successfully!");
                                Toast.makeText(getContext(), "Category was saved successfully!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        //set new category name in categories list
                        allCategories.set(position, currentCategory);

                        cb.setChecked(false);
                        closeEditDialog();
                    }
                });

                //Cancel Edit
                btnEditCategoryCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cb.setChecked(false);
                        closeEditDialog();
                    }
                });
            }
        };

        //RV, Query
        tvNoCategoryMessage = view.findViewById(R.id.tvNoCategoryMessage);
        rvCategories = view.findViewById(R.id.rvCategories);
        allCategories = new ArrayList<>();
        adapter = new CategoriesAdapter(getContext(), allCategories, onCheckDeleteClickListener, onCheckEditClickListener);
        rvCategories.setAdapter(adapter);
        rvCategories.setLayoutManager(new GridLayoutManager(getContext(), 2));
        queryCategories();

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainerCategories);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                editCategoryMode = false;
                deleteCategoryMode = false;
                adapter.clear();
                queryCategories();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.brown,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void changeClothesOfCategory(String originalCategoryName, final String newCategoryName) {
        ParseQuery<Clothing> query = ParseQuery.getQuery(Clothing.class);
        query.whereEqualTo(Clothing.KEY_CLOTHING_OWNER, ParseUser.getCurrentUser());
        query.whereEqualTo(Clothing.KEY_CLOTHING_CATEGORY, originalCategoryName);
        query.findInBackground(new FindCallback<Clothing>() {
            @Override
            public void done(List<Clothing> clothesOfCategory, ParseException e) {
                if(e!=null){
                    Log.e(TAG, "Issue with getting clothes of the category",e);
                    return;
                }
                for(Clothing clothing : clothesOfCategory){
                    clothing.setClothingCategoryName(newCategoryName);
                    clothing.saveInBackground();
                }
            }
        });
    }

    private void closeDeleteDialog() {
        //categoriesOverbox is not actually GONE, just INVISIBLE transparent
        categoriesOverbox.animate().alpha(0.0f).setDuration(500);
        //animate transparency, fade out
        rlPopUpDeleteCategoryDialog.animate().alpha(0.0f).setDuration(500);
        // get the view out of the fragment, diff from INVISIBLE
        // GONE is actually considered as gone, doesn't exist even as a transparent view
        // this prevents having the delete and edit dialogs overlap, cause buttons to overlap
        rlPopUpDeleteCategoryDialog.setVisibility(View.GONE);
        deleteCategoryMode=false;
        adapter.notifyDataSetChanged();
    }

    private void closeEditDialog() {
        categoriesOverbox.animate().alpha(0.0f).setDuration(500);
        rlPopUpEditCategoryDialog.animate().alpha(0.0f).setDuration(500);
        rlPopUpEditCategoryDialog.setVisibility(View.GONE);
        editCategoryMode=false;
        adapter.notifyDataSetChanged();
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
        pbLoadingCategories.setVisibility(ProgressBar.VISIBLE);
        ParseQuery<Category> query = ParseQuery.getQuery(Category.class);
        query.setLimit(20);
        query.whereEqualTo(Category.KEY_CATEGORY_OWNER, ParseUser.getCurrentUser());
        query.addDescendingOrder(Category.KEY_CAT_CREATED_KEY);
        query.findInBackground(new FindCallback<Category>() {
            @Override
            public void done(List<Category> categories, ParseException e) {
                if(e!=null){
                    Log.e(TAG, "Issue with getting categories",e);
                    pbLoadingCategories.setVisibility(ProgressBar.INVISIBLE);
                    return;
                }
                allCategories.addAll(categories);
                if(allCategories.isEmpty()){
                    tvNoCategoryMessage.setVisibility(View.VISIBLE);
                }
                else {
                    tvNoCategoryMessage.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
                pbLoadingCategories.setVisibility(ProgressBar.INVISIBLE);
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
                // getActivity().finish();
                break;
            case R.id.deleteCategory:
                if(deleteCategoryMode){
                    deleteCategoryMode = false;
                    editCategoryMode = false;
                }
                else {
                    deleteCategoryMode = true;
                    editCategoryMode = false;
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.editCategory:
                if(editCategoryMode){
                    editCategoryMode = false;
                    deleteCategoryMode = false;
                }
                else {
                    editCategoryMode = true;
                    deleteCategoryMode = false;
                }
                adapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}