package com.example.mywardrobe.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywardrobe.R;
import com.example.mywardrobe.activities.ClothesActivity;
import com.example.mywardrobe.fragments.CategoriesFragment;
import com.example.mywardrobe.models.Category;
import com.example.mywardrobe.models.Clothing;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    public static final String TAG = "CategoriesAdapter";

    private Context context;
    private List<Category> categories;

    public CategoriesAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private Button btnCategory;
        private Category currentCategory;
        private CheckBox cbDeleteCategory;
        private CheckBox cbEditCategory;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnCategory = itemView.findViewById(R.id.btnCategory);
            btnCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ClothesActivity.class);
                    intent.putExtra("fromCategories", Parcels.wrap(currentCategory));
                    context.startActivity(intent);
                }
            });

            cbDeleteCategory = itemView.findViewById(R.id.cbDeleteCategory);
            cbEditCategory = itemView.findViewById(R.id.cbEditCategory);
        }

        public void bind(final Category category) {
            currentCategory = category;
            btnCategory.setText(category.getCategoryName());

            if(CategoriesFragment.deleteCategoryMode){
                cbDeleteCategory.setVisibility(View.VISIBLE);
                cbDeleteCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Remove clothes with that category from Parse
                        removeClothesOfCategory(category.getCategoryName());

                        // Remove category from Parse
                        try {
                            category.delete();
                            Toast.makeText(view.getContext(), "successfully deleted category", Toast.LENGTH_SHORT).show();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        // Remove from categories list
                        categories.remove(getAdapterPosition());

                        CategoriesFragment.deleteCategoryMode=false;
                        cbDeleteCategory.setChecked(false);
                        notifyDataSetChanged();
                    }
                });
            }
            else {
                cbDeleteCategory.setVisibility(View.GONE);
            }

            if(CategoriesFragment.editCategoryMode){
                cbEditCategory.setVisibility(View.VISIBLE);
                cbEditCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO:
                        // Input new name for category
                        // btnCategory.setText(new name)
                        // Save changes for the category in Parse
                        CategoriesFragment.editCategoryMode=false;
                        cbEditCategory.setChecked(false);
                        notifyDataSetChanged();
                    }
                });
            }
            else {
                cbEditCategory.setVisibility(View.GONE);
            }
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
                        Log.i(TAG, "Clothing Name: " + clothing.getClothingName());
                    }
                }
            });
        }
    }
}
