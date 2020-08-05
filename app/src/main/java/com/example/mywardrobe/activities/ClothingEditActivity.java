package com.example.mywardrobe.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mywardrobe.R;
import com.example.mywardrobe.models.Clothing;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

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

    private ProgressBar pbLoadingClothingEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothing_edit);

        pbLoadingClothingEdit = findViewById(R.id.pbLoadingClothingEdit);

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

        btnClothingSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pbLoadingClothingEdit.setVisibility(View.VISIBLE);
                currentClothing.setClothingName(etClothingNewName.getText().toString());
                currentClothing.setClothingDescription(etClothingNewDescription.getText().toString());
                String rawInputPrice = etNewPrice.getText().toString();
                Double clothingPrice = Double.valueOf(rawInputPrice);
                if(rawInputPrice.isEmpty()){
                    clothingPrice = 0.0;
                }
                currentClothing.setClothingPrice(clothingPrice);
                currentClothing.setClothingBrand(etNewBrand.getText().toString());
                currentClothing.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e!=null){
                            Log.e(TAG, "Error while saving clothing",e);
                            Toast.makeText(ClothingEditActivity.this, "Error while saving clothing!", Toast.LENGTH_SHORT).show();
                            pbLoadingClothingEdit.setVisibility(View.INVISIBLE);
                            return;
                        }
                        Toast.makeText(ClothingEditActivity.this, "Clothing was edited successfully!", Toast.LENGTH_SHORT).show();
                        pbLoadingClothingEdit.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
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