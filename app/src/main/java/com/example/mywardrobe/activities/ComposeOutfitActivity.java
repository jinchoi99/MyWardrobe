package com.example.mywardrobe.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mywardrobe.R;

public class ComposeOutfitActivity extends AppCompatActivity {

    public static final String TAG = "ComposeOutfitActivity";
    private Button btnAddOutfit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_outfit);

        btnAddOutfit = findViewById(R.id.btnAddOutfit);
        btnAddOutfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goOutfitsFragment();
            }
        });
    }

    private void goOutfitsFragment() {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("EXTRA", "open OutfitsFragment");
        startActivity(i);
        finish();
    }
}