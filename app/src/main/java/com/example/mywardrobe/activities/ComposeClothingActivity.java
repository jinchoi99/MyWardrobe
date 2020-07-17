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
import android.widget.Toast;

import com.example.mywardrobe.R;
import com.example.mywardrobe.models.Category;
import com.example.mywardrobe.models.Clothing;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.io.File;

public class ComposeClothingActivity extends AppCompatActivity {

    public static final String TAG = "ComposeClothingActivity";
    private EditText etClothingName;
    private Button btnCaptureImage;
    private ImageView ivClothing;
    private EditText etClothingDescription;
    private EditText etPrice;
    private EditText etBrand;
    private Button btnClothingSubmit;
    private Button btnDone;

    Category currentCategory;

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private File photoFile;
    public String photoFileName = "photo.jpg";

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
        btnDone = findViewById(R.id.btnDone);

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });
        currentCategory = Parcels.unwrap(getIntent().getParcelableExtra("categoryName"));

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goClothes();
            }
        });

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
                String rawInputPrice = etPrice.getText().toString();
                if(rawInputPrice.isEmpty() || rawInputPrice.charAt(0)!='$'){
                    Toast.makeText(ComposeClothingActivity.this, "Price needs to start with '$'", Toast.LENGTH_SHORT).show();
                    return;
                }
                Number clothingPrice;
                if(rawInputPrice.length()==1){
                    clothingPrice=0;
                }
                else {
                    clothingPrice=Double.valueOf(rawInputPrice.substring(1));
                }
                String clothingBrand = etBrand.getText().toString();
                ParseUser clothingOwner = ParseUser.getCurrentUser();
                final String categoryName = currentCategory.getCategoryName();
                if(photoFile == null || ivClothing.getDrawable() == null){
                    Toast.makeText(ComposeClothingActivity.this, "There is no image!", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveClothing(clothingName, clothingDescription, clothingPrice, clothingBrand, clothingOwner, categoryName, photoFile);
            }
        });
    }

    private void goClothes() {
        Intent i = new Intent(this, ClothesActivity.class);
        i.putExtra("fromCompose", Parcels.wrap(currentCategory));
        startActivity(i);
        finish();
    }


    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(ComposeClothingActivity.this, "com.codepath.fileprovider.MyWardrobe", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        //if (intent.resolveActivity(getPackageManager()) == null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        //}
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
                ivClothing.setImageBitmap(takenImage);
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

    private void saveClothing(String clothingName, String clothingDescription, Number clothingPrice, String clothingBrand, ParseUser clothingOwner, String categoryName, final File photoFile) {
        Clothing clothing = new Clothing();
        clothing.setClothingName(clothingName);
        clothing.setClothingDescription(clothingDescription);
        clothing.setClothingPrice(clothingPrice);
        clothing.setClothingBrand(clothingBrand);
        clothing.setClothingOwner(clothingOwner);
        clothing.setClothingCategory(categoryName);
        clothing.setClothingImage(new ParseFile(photoFile));
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
                ivClothing.setImageResource(0);
            }
        });
    }
}