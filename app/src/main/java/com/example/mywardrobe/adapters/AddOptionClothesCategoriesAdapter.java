package com.example.mywardrobe.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywardrobe.R;
import com.example.mywardrobe.activities.OutfitEditActivity;
import com.example.mywardrobe.models.Category;
import com.example.mywardrobe.models.Clothing;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class AddOptionClothesCategoriesAdapter extends RecyclerView.Adapter<AddOptionClothesCategoriesAdapter.ViewHolder>{
    private Context context;
    private List<Category> allEditOutfitsCategories;
    public static final String TAG = "AddOptionClothes";

    public AddOptionClothesCategoriesAdapter(Context context, List<Category> allEditOutfitsCategories) {
        this.context = context;
        this.allEditOutfitsCategories = allEditOutfitsCategories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_addoptions_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = allEditOutfitsCategories.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return allEditOutfitsCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvAddOutfitCategoryName;
        private RecyclerView rvAddOutfitClothes;
        private AddOptionClothesAdapter adapter;
        public List<Clothing> allAddOptionClothes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddOutfitCategoryName = itemView.findViewById(R.id.tvAddOutfitCategoryName);
            rvAddOutfitClothes = itemView.findViewById(R.id.rvAddOutfitClothes);
            allAddOptionClothes = new ArrayList<>();
            adapter = new AddOptionClothesAdapter(context, allAddOptionClothes);
            rvAddOutfitClothes.setAdapter(adapter);
            rvAddOutfitClothes.setLayoutManager(new GridLayoutManager(context,2));
        }

        public void bind(Category category) {
            tvAddOutfitCategoryName.setText(category.getCategoryName());

            ParseQuery<Clothing> query = ParseQuery.getQuery(Clothing.class);
            query.whereEqualTo(Clothing.KEY_CLOTHING_OWNER, ParseUser.getCurrentUser());
            query.whereEqualTo(Clothing.KEY_CLOTHING_CATEGORY, category.getCategoryName());
            query.addDescendingOrder(Clothing.KEY_CLOTHING_CREATED_KEY);
            query.findInBackground(new FindCallback<Clothing>() {
                @Override
                public void done(List<Clothing> clothes, ParseException e) {
                    if(e!=null){
                        return;
                    }
                    allAddOptionClothes.addAll(clothes);
                    takeoutClothes(allAddOptionClothes, clothes);
                }
            });
        }

        private void takeoutClothes(List<Clothing> allAddOptionClothes, List<Clothing> clothes) {
            List<Clothing> check = OutfitEditActivity.allClothesCurrentRelations;
            //make sure clothes.size() doesn't change; remove from allAddOptionClothes, not clothes
            for (int k = 0; k < clothes.size(); k++) {
                for (int i = 0; i < check.size(); i++) {
                    String checkID = clothes.get(k).getObjectId() ;
                    String hasID = check.get(i).getObjectId();
                    boolean eq = checkID.equals(hasID);
                    if(eq){
                        allAddOptionClothes.remove(clothes.get(k));
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }
    }
}
