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
import com.example.mywardrobe.models.Clothing;
import com.example.mywardrobe.models.Outfit;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class OutfitsAdapter extends RecyclerView.Adapter<OutfitsAdapter.ViewHolder> {
    private Context context;
    private List<Outfit> outfits;
    public static final String TAG = "OutfitsAdapter";

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
        private RecyclerView rvClothesRelationList;
        private ClothesRelationsAdapter adapter;
        private List<Clothing> allClothesRelations;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOutfitName = itemView.findViewById(R.id.tvOutfitName);
            rvClothesRelationList = itemView.findViewById(R.id.rvClothesRelationList);
            allClothesRelations = new ArrayList<>();
            adapter = new ClothesRelationsAdapter(context, allClothesRelations);
            rvClothesRelationList.setAdapter(adapter);
            rvClothesRelationList.setLayoutManager(new GridLayoutManager(context, 3));
        }

        public void bind(Outfit outfit) {
            tvOutfitName.setText(outfit.getOutfitName());
            ParseQuery queryRelations = outfit.getClothingRelation().getQuery();
            queryRelations.findInBackground(new FindCallback<Clothing>() {
                @Override
                public void done(List<Clothing> clothesRelations, ParseException e) {
                    if(e!=null){
                        Log.e(TAG, "Issue with getting clothesRelations",e);
                        return;
                    }
                    allClothesRelations.addAll(clothesRelations);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }
}
