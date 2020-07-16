package com.example.mywardrobe.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mywardrobe.R;
import com.example.mywardrobe.fragments.ClothesFragment;
import com.example.mywardrobe.models.Category;
import com.example.mywardrobe.models.Clothing;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

public class ComposeClothingActivity extends AppCompatActivity {

    public static final String TAG = "ComposeClothingActivity";
    private EditText etClothingName;
    private Button btnCaptureImage;
    private ImageView ivClothing;
    private EditText etClothingDescription;
    private EditText etPrice;
    private EditText etBrand;
    private Button btnClothingSubmit;
    Category currentCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_clothing);

        etClothingName = findViewById(R.id.etClothingName);
        btnCaptureImage = findViewById(R.id.btnCaptureImage);
        ivClothing = findViewById(R.id.ivClothing);
        etClothingDescription = findViewById(R.id.etClothingDescription);
        etPrice = findViewById(R.id.etPrice);
        etBrand = findViewById(R.id.etBrand);
        btnClothingSubmit = findViewById(R.id.btnClothingSubmit);

        //Create a new instance of Clothing entity/model/parseobject
        btnClothingSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clothingName = etClothingName.getText().toString();
                if(clothingName.isEmpty()){
                    Toast.makeText(ComposeClothingActivity.this, "Clothing Name can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String clothingDescription = etClothingDescription.getText().toString();
                String price = etPrice.getText().toString();
                if(price.isEmpty() || price.charAt(0)!='$'){
                    Toast.makeText(ComposeClothingActivity.this, "Price needs to start with '$'", Toast.LENGTH_SHORT).show();
                    return;
                }
                Number clothingPrice;
                if(price.length()==1){
                    clothingPrice=0;
                }
                else {
                    clothingPrice=Integer.valueOf(price.substring(1));
                }
                String clothingBrand = etBrand.getText().toString();
                ParseUser clothingOwner = ParseUser.getCurrentUser();
                currentCategory = Parcels.unwrap(getIntent().getParcelableExtra("categoryName"));
                final String categoryName = currentCategory.getCategoryName();
                saveClothing(clothingName, clothingDescription, clothingPrice, clothingBrand, clothingOwner, categoryName);
            }
        });
    }

    private void saveClothing(String clothingName, String clothingDescription, Number clothingPrice, String clothingBrand, ParseUser clothingOwner, String categoryName) {
        Clothing clothing = new Clothing();
        clothing.setClothingName(clothingName);
        clothing.setClothingDescription(clothingDescription);
        clothing.setClothingPrice(clothingPrice);
        clothing.setClothingBrand(clothingBrand);
        clothing.setClothingOwner(clothingOwner);
        clothing.setClothingCategory(categoryName);
        clothing.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e!=null){
                    Log.e(TAG, "Error while saving clothing",e);
                    Toast.makeText(ComposeClothingActivity.this, "Error while saving clothing!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i(TAG, "Clothing was saved successfully!");
                Toast.makeText(ComposeClothingActivity.this, "Clothing was saved successfully!", Toast.LENGTH_SHORT).show();
                etClothingName.setText("");
                etClothingDescription.setText("");
                etPrice.setText("$");
                etBrand.setText("");
            }
        });
    }
}