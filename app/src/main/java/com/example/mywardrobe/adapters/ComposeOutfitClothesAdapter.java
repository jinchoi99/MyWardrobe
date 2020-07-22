package com.example.mywardrobe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywardrobe.R;
import com.example.mywardrobe.models.Clothing;

import java.util.List;

public class ComposeOutfitClothesAdapter extends RecyclerView.Adapter<ComposeOutfitClothesAdapter.ViewHolder>{

    private Context context;
    private List<Clothing> composeOutfitClothes;

    public ComposeOutfitClothesAdapter(Context context, List<Clothing> composeOutfitClothes) {
        this.context = context;
        this.composeOutfitClothes = composeOutfitClothes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_compose_outfit_clothes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Clothing composeOutfitClothing = composeOutfitClothes.get(position);
        holder.bind(composeOutfitClothing);
    }

    @Override
    public int getItemCount() {
        return composeOutfitClothes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvComposeOutfitClothingName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvComposeOutfitClothingName = itemView.findViewById(R.id.tvComposeOutfitClothingName);
        }

        public void bind(Clothing composeOutfitClothing) {
            tvComposeOutfitClothingName.setText(composeOutfitClothing.getClothingName());
        }
    }
}
