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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

    // Initialize Firebase database reference
    DatabaseReference savedResultsRef = FirebaseDatabase.getInstance().getReference("saved_results");

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
                        SavedResultsManager.clearSavedResults();

                        // Refresh the RecyclerView
                        savedResultsList.clear();
                        RecyclerView recyclerView = findViewById(R.id.recyclerView);
                        SavedResultsAdapter adapter = new SavedResultsAdapter(savedResultsList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(History.this));
                        recyclerView.setAdapter(adapter);

                        recommendationButton.setVisibility(View.GONE);
                        isRecommendationClickable = true;

                        // Update the saved results count TextView to 0
                        updateSavedResultsCount(0);

                        // Clear saved results in Firebase
                        clearSavedResultsInFirebase();

                        // Clear saved results in SharedPreferences
                        clearSavedResultsInSharedPreferences();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do nothing, user canceled the clear operation
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

    // Load saved results from Firebase
    private void loadSavedResultsFromFirebase() {
        savedResultsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                savedResultsList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    SavedResult savedResult = ds.getValue(SavedResult.class);
                    savedResultsList.add(savedResult);
                }

                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                recyclerView.getAdapter().notifyDataSetChanged();

                updateSavedResultsCount(savedResultsList.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors if needed
            }
        });
    }

    // Clear saved results in Firebase
    private void clearSavedResultsInFirebase() {
        savedResultsRef.setValue(null);
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
