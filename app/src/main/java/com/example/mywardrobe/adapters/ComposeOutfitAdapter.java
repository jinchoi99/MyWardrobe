package com.example.mywardrobe.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywardrobe.R;
import com.example.mywardrobe.models.Category;
import com.example.mywardrobe.models.Clothing;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ComposeOutfitAdapter extends RecyclerView.Adapter<ComposeOutfitAdapter.ViewHolder>{
    private Context context;
    private List<Category> composeOutfitsCategories;
    public static final String TAG = "ComposeOutfitAdapter";

    public ComposeOutfitAdapter(Context context, List<Category> composeOutfitsCategories) {
        this.context = context;
        this.composeOutfitsCategories = composeOutfitsCategories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_compose_outfit_categories, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = composeOutfitsCategories.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return composeOutfitsCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvComposeOutfitCategoryName;
        private RecyclerView rvComposeOutfitClothes;
        private ComposeOutfitClothesAdapter adapter;
        public List<Clothing> allComposeOutfitClothes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvComposeOutfitCategoryName = itemView.findViewById(R.id.tvComposeOutfitCategoryName);
            rvComposeOutfitClothes = itemView.findViewById(R.id.rvComposeOutfitClothes);
            allComposeOutfitClothes = new ArrayList<>();
            adapter = new ComposeOutfitClothesAdapter(context, allComposeOutfitClothes);
            rvComposeOutfitClothes.setAdapter(adapter);
            rvComposeOutfitClothes.setLayoutManager(new GridLayoutManager(context,2));
        }

        public void bind(Category category) {
            tvComposeOutfitCategoryName.setText(category.getCategoryName());
            ParseQuery<Clothing> query = ParseQuery.getQuery(Clothing.class);
            query.setLimit(20);
            query.whereEqualTo(Clothing.KEY_CLOTHING_OWNER, ParseUser.getCurrentUser());
            query.whereEqualTo(Clothing.KEY_CLOTHING_CATEGORY, category.getCategoryName());
            query.addDescendingOrder(Clothing.KEY_CLOTHING_CREATED_KEY);
            query.findInBackground(new FindCallback<Clothing>() {
                @Override
                public void done(List<Clothing> clothes, ParseException e) {
                if(e!=null){
                    Log.e(TAG, "Issue with getting clothes",e);
                    return;
                }
                for(Clothing clothing : clothes){
                    Log.i(TAG, "Clothing Name: " + clothing.getClothingName());
                }
                allComposeOutfitClothes.addAll(clothes);
                adapter.notifyDataSetChanged();
                }
            });
        }
    }
}
