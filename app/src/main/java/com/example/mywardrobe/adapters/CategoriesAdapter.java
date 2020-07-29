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
import org.parceler.Parcels;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    public interface OnCheckDeleteClickListener{
        void onCheckDeleteClicked(int position, CheckBox cb);
    }

    public static final String TAG = "CategoriesAdapter";

    private Context context;
    private List<Category> categories;
    OnCheckDeleteClickListener checkDeleteClickListener;

    public CategoriesAdapter(Context context, List<Category> categories, OnCheckDeleteClickListener checkDeleteClickListener) {
        this.context = context;
        this.categories = categories;
        this.checkDeleteClickListener = checkDeleteClickListener;
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
                        //call the onCheckDeleteClicked method, which is one of the methods of checkDeleteClickListener,
                        // which is an instance of the OnCheckDeleteClickListener interface created in the CatFragment
                        //and passed to the adapter when calling adapter constructor, and
                        //"this.checkDeleteClickListener = checkDeleteClickListener;"
                        //so this checkDeleteClickListener is an instance of OnCheckDeleteClickListener interface,
                        // where its method onCheckDeleteClicked is defined (/overwritten) when instance is created
                        //so when checkDeleteClickListener.onCheckDeleteClicked(getAdapterPosition(), cbDeleteCategory); is called,
                        //the onCheckDeleteClicked function defined in CatFragment is what is processed
                        checkDeleteClickListener.onCheckDeleteClicked(getAdapterPosition(), cbDeleteCategory);
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
    }
}
