package com.example.mywardrobe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mywardrobe.R;
import com.example.mywardrobe.models.Clothing;
import com.example.mywardrobe.models.Outfit;
import com.parse.ParseFile;

import java.util.List;

public class ClothesRelationsAdapter extends RecyclerView.Adapter<ClothesRelationsAdapter.ViewHolder>{

    private Context context;
    private List<Clothing> clothesRelations;

    public ClothesRelationsAdapter(Context context, List<Clothing> clothesRelations) {
        this.context = context;
        this.clothesRelations = clothesRelations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_clothing_relation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Clothing clothingRelation = clothesRelations.get(position);
        holder.bind(clothingRelation);
    }

    @Override
    public int getItemCount() {
        return clothesRelations.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivClothingRelation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivClothingRelation = itemView.findViewById(R.id.ivClothingRelation);
        }

        public void bind(Clothing clothingRelation) {
            ParseFile image = clothingRelation.getClothingImage();
            if(image!=null){
                (Glide.with(context).load(image.getUrl()).circleCrop()).into(ivClothingRelation);
            }
        }
    }
}
