package com.example.application;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

                    // Find the TextViews and ImageView in the layout
                    TextView diseaseTextView = findViewById(R.id.resultTextView);
                    ImageView resultImageView = findViewById(R.id.resultImageView);

                    // Set the text for disease and severity
                    diseaseTextView.setText(result);

                    // Set the image in the ImageView
                    resultImageView.setImageBitmap(imageBitmap);

                    // Create a SavedResult object
                    SavedResult savedResult = new SavedResult(result, imageBitmap);






                    // Redirect to another activity (Folders)
                    Intent intent = null;
                    if (SavedResultsManager.getSavedResultsCount() < 10) {
                        intent = new Intent(Result_Activity.this, Folders.class);
                    } else if (SavedResultsManager.getSavedResultsCountHistory2() < 10) {
                        intent = new Intent(Result_Activity.this, Folders.class);
                    } else if (SavedResultsManager.getSavedResultsCountHistory3() < 10) {
                        intent = new Intent(Result_Activity.this, Folders.class);
                    } else if (SavedResultsManager.getSavedResultsCountHistory4() < 10) {
                        intent = new Intent(Result_Activity.this, Folders.class);
                    } else if (SavedResultsManager.getSavedResultsCountHistory5() < 10) {
                        intent = new Intent(Result_Activity.this, Folders.class);
                    } else if (SavedResultsManager.getSavedResultsCountHistory6() < 10) {
                        intent = new Intent(Result_Activity.this, Folders.class);
                    } else if (SavedResultsManager.getSavedResultsCountHistory7() < 10) {
                        intent = new Intent(Result_Activity.this, Folders.class);
                    } else if (SavedResultsManager.getSavedResultsCountHistory8() < 10) {
                        intent = new Intent(Result_Activity.this, Folders.class);
                    } else if (SavedResultsManager.getSavedResultsCountHistory9() < 10) {
                        intent = new Intent(Result_Activity.this, Folders.class);
                    } else if (SavedResultsManager.getSavedResultsCountHistory10() < 10) {
                        intent = new Intent(Result_Activity.this, Folders.class);
                    }
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

                    isSaving = false;
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
}