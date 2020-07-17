package com.example.mywardrobe.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywardrobe.R;
import com.example.mywardrobe.activities.ClothesActivity;
import com.example.mywardrobe.models.Category;

import org.parceler.Parcels;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

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
        private Button btnCategroy;
        private Category currentCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnCategroy = itemView.findViewById(R.id.btnCategory);
            btnCategroy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ClothesActivity.class);
                    intent.putExtra("fromCategories", Parcels.wrap(currentCategory));
                    context.startActivity(intent);
                }
            });
        }

        public void bind(Category category) {
            currentCategory = category;
            btnCategroy.setText(category.getCategoryName());
        }
    }
}
