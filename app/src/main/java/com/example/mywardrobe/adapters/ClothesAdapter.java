package com.example.mywardrobe.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mywardrobe.R;
import com.example.mywardrobe.activities.ClothesActivity;
import com.example.mywardrobe.activities.ClothingDetailsActivity;
import com.example.mywardrobe.models.Clothing;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.List;

public class ClothesAdapter extends RecyclerView.Adapter<ClothesAdapter.ViewHolder> {
    private Context context;
    private List<Clothing> clothes;

    public ClothesAdapter(Context context, List<Clothing> clothes) {
        this.context = context;
        this.clothes = clothes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_clothing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Clothing clothing = clothes.get(position);
        holder.bind(clothing);
    }

    @Override
    public int getItemCount() {
        return clothes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivClothing;
        private TextView tvClothingName;
        private Clothing currentClothing;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivClothing = itemView.findViewById(R.id.ivClothing);
            tvClothingName = itemView.findViewById(R.id.tvClothingName);

            ivClothing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ClothingDetailsActivity.class);
                    intent.putExtra("fromClothes", Parcels.wrap(currentClothing));
                    context.startActivity(intent);
                }
            });
        }

        public void bind(Clothing clothing) {
            currentClothing=clothing;
            tvClothingName.setText(clothing.getClothingName());
            ParseFile image = clothing.getClothingImage();
            if(image!=null){
                Glide.with(context).load(image.getUrl()).into(ivClothing);
            }
        }
    }
}
