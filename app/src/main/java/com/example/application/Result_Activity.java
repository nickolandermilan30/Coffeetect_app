package com.example.application;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class Result_Activity extends AppCompatActivity {

    Button backr, saveButton;
    private boolean isSaving = false;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        TextView resultTextView = findViewById(R.id.resultTextView);
        backr = findViewById(R.id.capagain);
        saveButton = findViewById(R.id.saveButton);
        ImageView imageView = findViewById(R.id.resultImageView); // Add ImageView

        // Retrieve the result from the Intent
        String result = getIntent().getStringExtra("result");
        // Set the result in the TextView
        resultTextView.setText("Result: " + result);

        // Retrieve the image byte array from the Intent
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        if (byteArray != null) {
            // Convert the byte array to a Bitmap and display it in the ImageView
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imageView.setImageBitmap(imageBitmap);
        }

        backr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Result_Activity.this, Camera_page.class);
                intent.putExtra("diseaseName", "SampleDiseaseName");
                startActivity(intent);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSaving) {
                    isSaving = true;

                    // Retrieve the disease name from the Intent
                    String result = getIntent().getStringExtra("result");

                    // Retrieve the byte array from the intent
                    byte[] byteArray = getIntent().getByteArrayExtra("image");

                    // Convert the byte array back to a Bitmap
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 1;
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;

                    Bitmap imageBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);

                    // Resize the Bitmap
                    int targetWidth = 800;
                    int targetHeight = 600;
                    imageBitmap = Bitmap.createScaledBitmap(imageBitmap, targetWidth, targetHeight, false);

                    // Set the text for disease and severity
                    resultTextView.setText(result);

                    // Set the image in the ImageView
                    imageView.setImageBitmap(imageBitmap);

                    // Create a SavedResult object
                    SavedResult savedResult = new SavedResult(result, imageBitmap);

                    // Redirect to another activity (Folders)
                    Intent intent = new Intent(Result_Activity.this, Folders.class);
                    startActivity(intent);

                    // Check if `History` is full and add to the appropriate history list
                    if (SavedResultsManager.getSavedResultsCount() < 10) {
                        SavedResultsManager.addSavedResult(savedResult);
                    } else if (SavedResultsManager.getSavedResultsCountHistory2() < 10) {
                        SavedResultsManager.addSavedResultToHistory2(savedResult);
                    } else if (SavedResultsManager.getSavedResultsCountHistory3() < 10) {
                        SavedResultsManager.addSavedResultToHistory3(savedResult);
                    } else if (SavedResultsManager.getSavedResultsCountHistory4() < 10) {
                        SavedResultsManager.addSavedResultToHistory4(savedResult);
                    } else if (SavedResultsManager.getSavedResultsCountHistory5() < 10) {
                        SavedResultsManager.addSavedResultToHistory5(savedResult);
                    } else if (SavedResultsManager.getSavedResultsCountHistory6() < 10) {
                        SavedResultsManager.addSavedResultToHistory6(savedResult);
                    } else if (SavedResultsManager.getSavedResultsCountHistory7() < 10) {
                        SavedResultsManager.addSavedResultToHistory7(savedResult);
                    } else if (SavedResultsManager.getSavedResultsCountHistory8() < 10) {
                        SavedResultsManager.addSavedResultToHistory8(savedResult);
                    } else if (SavedResultsManager.getSavedResultsCountHistory9() < 10) {
                        SavedResultsManager.addSavedResultToHistory9(savedResult);
                    } else if (SavedResultsManager.getSavedResultsCountHistory10() < 10) {
                        SavedResultsManager.addSavedResultToHistory10(savedResult);
                    }

                    // Save the image to Firebase Storage
                    saveImageToFirebaseStorage(savedResult, imageBitmap);
                }
            }
        });

        // Add back button functionality
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void saveImageToFirebaseStorage(SavedResult savedResult, Bitmap imageBitmap) {
        // Convert the Bitmap to a byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        // Generate a unique filename (e.g., using a timestamp)
        String filename = System.currentTimeMillis() + ".png";

        // Get a reference to the Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a reference to the folder where you want to save the image
        StorageReference folderReference = storage.getReference().child("images");

        // Create a reference to the specific image file
        StorageReference imageReference = folderReference.child(filename);

        // Upload the image to Firebase Storage
        UploadTask uploadTask = imageReference.putBytes(byteArray);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Get the download URL for the uploaded image
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUrl) {
                        String imageUrl = downloadUrl.toString();

                        // Set the image URL in the saved result
                        savedResult.setImageUrl(imageUrl);

                        // You may want to save the savedResult object to a database or a list here
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle the failure to upload the image
                Toast.makeText(Result_Activity.this, "Failed to save the image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
