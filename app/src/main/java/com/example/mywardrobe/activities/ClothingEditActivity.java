package com.example.mywardrobe.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import java.io.File;

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

    //Camera
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private File photoFile;
    public String photoFileName = "photo.jpg";

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

        btnCaptureNewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

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
                if(rawInputPrice.isEmpty()){
                    rawInputPrice="0";
                }
                Double clothingPrice = Double.valueOf(rawInputPrice);
                currentClothing.setClothingPrice(clothingPrice);
                currentClothing.setClothingBrand(etNewBrand.getText().toString());
                if(photoFile != null){
                    currentClothing.setClothingImage(new ParseFile(photoFile));
                }
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

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(ClothingEditActivity.this, "com.codepath.fileprovider.MyWardrobe", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        // Start the image capture intent to take photo
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivNewClothing.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    private void goClothingDetails() {
        Intent i = new Intent(this, ClothingDetailsActivity.class);
        i.putExtra("fromEditClothing", Parcels.wrap(currentClothing));
        startActivity(i);
        finish();
    }
}