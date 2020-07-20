package com.example.mywardrobe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywardrobe.R;
import com.example.mywardrobe.models.Outfit;

import java.util.List;

public class OutfitsAdapter extends RecyclerView.Adapter<OutfitsAdapter.ViewHolder> {
    private Context context;
    private List<Outfit> outfits;

    public OutfitsAdapter(Context context, List<Outfit> outfits) {
        this.context = context;
        this.outfits = outfits;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_outfit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Outfit outfit = outfits.get(position);
        holder.bind(outfit);
    }

    @Override
    public int getItemCount() {
        return outfits.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvOutfitName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOutfitName = itemView.findViewById(R.id.tvOutfitName);
        }

        public void bind(Outfit outfit) {
            tvOutfitName.setText(outfit.getOutfitName());
        }
    }
}
