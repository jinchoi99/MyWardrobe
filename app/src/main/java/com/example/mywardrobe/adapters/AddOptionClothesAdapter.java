package com.example.mywardrobe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mywardrobe.R;
import com.example.mywardrobe.activities.ComposeOutfitActivity;
import com.example.mywardrobe.activities.OutfitEditActivity;
import com.example.mywardrobe.models.Clothing;
import com.parse.ParseFile;

import java.util.List;

public class AddOptionClothesAdapter extends RecyclerView.Adapter<AddOptionClothesAdapter.ViewHolder>{

    private Context context;
    private List<Clothing> addOptionClothes;

    public AddOptionClothesAdapter(Context context, List<Clothing> addOptionClothes) {
        this.context = context;
        this.addOptionClothes = addOptionClothes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_addoptions_clothing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Clothing addOptionClothing = addOptionClothes.get(position);
        holder.bind(addOptionClothing);
    }

    @Override
    public int getItemCount() {
        return addOptionClothes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvAddOutfitClothingName;
        private CheckBox cbAddOutfitClothing;
        private ImageView ivAddOutfitClothingImage;
        Clothing currentClothing;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddOutfitClothingName = itemView.findViewById(R.id.tvAddOutfitClothingName);
            cbAddOutfitClothing = itemView.findViewById(R.id.cbAddOutfitClothing);
            ivAddOutfitClothingImage = itemView.findViewById(R.id.ivAddOutfitClothingImage);

            cbAddOutfitClothing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OutfitEditActivity.makeSelection(view, currentClothing);
                }
            });
        }

        public void bind(Clothing addOptionClothing) {
            currentClothing = addOptionClothing;
            tvAddOutfitClothingName.setText(addOptionClothing.getClothingName());
            ParseFile image = addOptionClothing.getClothingImage();
            if(image!=null){
                (Glide.with(context).load(image.getUrl()).circleCrop()).into(ivAddOutfitClothingImage);
            }
        }
    }
}
