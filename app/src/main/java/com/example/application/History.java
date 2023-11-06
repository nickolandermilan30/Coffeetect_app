package com.example.application;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private List<SavedResult> savedResultsList = new ArrayList<>();
    private boolean isRecommendationClickable = true;
    private Button recommendationButton;
    Button cal, his;

    private TextView savedResultsCountTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);



        // Hide the action bar (app bar or title bar)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        savedResultsCountTextView = findViewById(R.id.savedResultsCount);
        recommendationButton = findViewById(R.id.recommendationButton);
        Button clearResultsButton = findViewById(R.id.clearResultsButton);

        // Load saved results from SharedPreferences
        loadSavedResultsFromSharedPreferences();

        // Retrieve saved results from SavedResultsManager
        List<SavedResult> savedResults = SavedResultsManager.getSavedResultsList();

        // Update the saved results count TextView
        updateSavedResultsCount(savedResults.size());

        // Initialize RecyclerView and set adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        SavedResultsAdapter adapter = new SavedResultsAdapter(savedResults);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Get recommendation
        String mostFrequentAndSevereDisease = SavedResultsManager.getMostFrequentDisease();

        // Configure recommendation button
        Button recommendationButton = findViewById(R.id.recommendationButton);

        // Check if there are at least 10 saved results to show the recommendation button
        if (SavedResultsManager.getSavedResultsList().size() < 10) {
            recommendationButton.setVisibility(View.GONE); // Hide the recommendation button
        }

        recommendationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRecommendationClickable) { // Check if button is clickable
                    Intent intent = new Intent(History.this, RecommendationActivity.class);
                    intent.putExtra("diseaseName", mostFrequentAndSevereDisease);
                    startActivity(intent);
                }
            }
        });

        clearResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(History.this);
                builder.setTitle("Clear Saved Results");
                builder.setMessage("Are you sure you want to clear all saved results? This action cannot be undone.");
                builder.setPositiveButton("Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isRecommendationClickable = false;

                        // Tawagan ang method para tanggalin ang mga larawan sa Firebase Storage
                        clearImagesInFirebaseStorage();

                        // Tanggalin ang iyong mga resulta sa "History"
                        SavedResultsManager.clearSavedResults();

                        // Refresh the RecyclerView
                        savedResultsList.clear();
                        RecyclerView recyclerView = findViewById(R.id.recyclerView);
                        SavedResultsAdapter adapter = new SavedResultsAdapter(savedResultsList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(History.this));
                        recyclerView.setAdapter(adapter);

                        recommendationButton.setVisibility(View.GONE);
                        isRecommendationClickable = true;

                        // I-update ang saved results count TextView na 0
                        updateSavedResultsCount(0);

                        // Clear saved results in SharedPreferences
                        clearSavedResultsInSharedPreferences();
                    }

                    private void clearImagesInFirebaseStorage() {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference();

                        for (SavedResult result : savedResultsList) {
                            String imageUrl = result.getImageUrl();
                            if (imageUrl != null) {
                                // Extract the filename from the image URL
                                String filename = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);

                                // Create a reference to the image file
                                StorageReference imageRef = storageRef.child("images/" + filename);

                                // Delete the image file
                                imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Handle success (image deleted)
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle failure (image not deleted)
                                    }
                                });



                }
                        }
                    }

                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Huwag gawin ang anumang hakbang, kinansela ng user ang operasyon
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void updateSavedResultsCount(int count) {
        savedResultsCountTextView.setText("Saved Results: " + count);
    }


    // Save results to SharedPreferences
    private void saveResultsToSharedPreferences(List<SavedResult> results) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String savedResultsString = convertSavedResultsToJson(results);
        editor.putString("savedResults", savedResultsString);
        editor.apply();
    }

    private String convertSavedResultsToJson(List<SavedResult> results) {
        Gson gson = new Gson();
        return gson.toJson(results);
    }

    // Load saved results from SharedPreferences
    private void loadSavedResultsFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String savedResultsString = sharedPreferences.getString("savedResults", "");

        if (!savedResultsString.isEmpty()) {
            savedResultsList = convertJsonToSavedResults(savedResultsString);
        } else {
            savedResultsList = new ArrayList<>(); // Gumamit ng bagong ArrayList kapag walang laman.
        }

        updateSavedResultsCount(savedResultsList.size());
    }

    // Function to convert JSON string to List of SavedResult
    private List<SavedResult> convertJsonToSavedResults(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<SavedResult>>() {}.getType();
        return gson.fromJson(json, type);
    }

    // Clear saved results in SharedPreferences
    private void clearSavedResultsInSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("savedResults");
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}