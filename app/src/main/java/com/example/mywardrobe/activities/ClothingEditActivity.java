package com.example.mywardrobe.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mywardrobe.R;
import com.example.mywardrobe.models.Clothing;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class ClothingEditActivity extends AppCompatActivity {
    public static final String TAG = "ClothingEditActivity";
    private EditText etClothingNewName;
    private Button btnCaptureNewImage;
    private ImageView ivNewClothing;
    private EditText etClothingNewDescription;
    private EditText etNewPrice;
    private EditText etNewBrand;
    private Button btnClothingSave;
    private Button btnNewDone;

    Clothing currentClothing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothing_edit);

        etClothingNewName = findViewById(R.id.etClothingNewName);
        btnCaptureNewImage = findViewById(R.id.btnCaptureNewImage);
        ivNewClothing = findViewById(R.id.ivNewClothing);
        etClothingNewDescription = findViewById(R.id.etClothingNewDescription);
        etNewPrice = findViewById(R.id.etNewPrice);
        etNewBrand = findViewById(R.id.etNewBrand);
        btnClothingSave = findViewById(R.id.btnClothingSave);
        btnNewDone = findViewById(R.id.btnNewDone);

        currentClothing = Parcels.unwrap(getIntent().getParcelableExtra("clothing"));

        etClothingNewName.setText(currentClothing.getClothingName());
        ParseFile image = currentClothing.getClothingImage();
        if(image!=null){
            Glide.with(this).load(image.getUrl()).into(ivNewClothing);
        }
        etClothingNewDescription.setText(currentClothing.getClothingDescription());
        etNewPrice.setText("" + currentClothing.getClothingPrice());
        etNewBrand.setText(currentClothing.getClothingBrand());
        btnNewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goClothingDetails();
            }
        });
    }

    private void goClothingDetails() {
        Intent i = new Intent(this, ClothingDetailsActivity.class);
        i.putExtra("fromEditClothing", Parcels.wrap(currentClothing));
        startActivity(i);
        finish();
    }
}