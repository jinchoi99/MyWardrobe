package com.example.mywardrobe.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mywardrobe.R;
import com.example.mywardrobe.models.Category;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ComposeCategoryActivity extends AppCompatActivity {

    public static final String TAG = "ComposeCategoryActivity";
    private EditText etCategoryName;
    private Button btnAddCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_category);

        etCategoryName = findViewById(R.id.etCategoryName);
        btnAddCategory = findViewById(R.id.btnAddCategory);

        //Create a new instance of Category entity/model/parseobject
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String categoryName = etCategoryName.getText().toString();
                if(categoryName.isEmpty()){
                    Toast.makeText(ComposeCategoryActivity.this, "Category Name can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser categoryOwner = ParseUser.getCurrentUser();
                saveCategory(categoryName, categoryOwner);
            }
        });
    }

    private void saveCategory(String categoryName, ParseUser categoryOwner) {
        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setCategoryOwner(categoryOwner);
        category.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e!=null){
                    Log.e(TAG, "Error while saving category",e);
                    Toast.makeText(ComposeCategoryActivity.this, "Error while saving category!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i(TAG, "Category was saved successfully!");
                Toast.makeText(ComposeCategoryActivity.this, "New Category has been added!", Toast.LENGTH_SHORT).show();
                etCategoryName.setText("");
            }
        });
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}