package com.example.application;

// Import the necessary Firebase classes

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Calendar extends AppCompatActivity {

    PieChart pieChart;
    ArrayList<PieEntry> entries = new ArrayList<>(); // To store detected diseases

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // Hide the action bar (app bar or title bar)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Modify your severity thresholds
        int mildThreshold = 20;
        int moderateThreshold = 40;
        int criticalThreshold = 60;

        pieChart = findViewById(R.id.pieChart);
        setupPieChart();

        // Initialize the Firebase database
        DatabaseReference monthlyReportRef = FirebaseDatabase.getInstance().getReference("monthly_reports").child("2023").child("October");

        // Example disease data (you can replace this with your actual data)
        HashMap<String, Integer> diseaseData = new HashMap<>();
        diseaseData.put("Cercospora", 15);
        diseaseData.put("Sooty Mold", 20);
        diseaseData.put("Leaf Rust", 30);
        diseaseData.put("Leaf Miner", 40);
        diseaseData.put("Phoma", 50);
        diseaseData.put("Healthy Leaf", 60);

// Calculate the total severity levels
        int totalSeverityLevels = 0;

        for (Map.Entry<String, Integer> entry : diseaseData.entrySet()) {
            totalSeverityLevels += entry.getValue();
        }

// Add data to the Firebase database
        monthlyReportRef.setValue(diseaseData);

// Populate the TableLayout
        TableLayout diseaseTable = findViewById(R.id.diseaseTable);

        for (Map.Entry<String, Integer> entry : diseaseData.entrySet()) {
            String diseaseName = entry.getKey();
            int severityLevel = entry.getValue();
            String severityCategory;

            // Determine the severity category based on severity level
            if (severityLevel < mildThreshold) {
                severityCategory = "Mild";
            } else if (severityLevel < moderateThreshold) {
                severityCategory = "Moderate";
            } else if (severityLevel < criticalThreshold) {
                severityCategory = "Critical";
            } else {
                severityCategory = "Unknown"; // Add more categories if needed
            }

            // Calculate the percentage based on the total severity levels
            double percentage = (severityLevel * 100.0) / totalSeverityLevels;

            // Create a new TableRow
            TableRow row = new TableRow(this);

            // Create TextViews for disease name, severity level, category, and percentage
            TextView nameTextView = new TextView(this);
            nameTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            nameTextView.setText(diseaseName);

            TextView severityTextView = new TextView(this);
            severityTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            severityTextView.setText(String.valueOf(severityLevel));

            TextView categoryTextView = new TextView(this);
            categoryTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            categoryTextView.setText(severityCategory);

            TextView percentageTextView = new TextView(this);
            percentageTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            percentageTextView.setText(String.format("%.2f%%", percentage));

            // Add TextViews to the TableRow
            row.addView(nameTextView);
            row.addView(severityTextView);
            row.addView(categoryTextView);
            row.addView(percentageTextView);

            // Add the TableRow to the TableLayout
            diseaseTable.addView(row);
        }






        // Retrieve data from Firebase
        monthlyReportRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                entries.clear(); // Clear the entries

                int totalSeverityLevels = 100; // Total disease severity levels

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String sakitName = ds.getKey();
                    int severityLevel = ds.getValue(Integer.class);
                    entries.add(new PieEntry(severityLevel, sakitName));
                }

                // Update the PieChart
                PieDataSet dataSet = new PieDataSet(entries, "Monthly Report");
                dataSet.setColors(getRandomColors(entries.size())); // Call getRandomColors function
                PieData data = new PieData(dataSet);

                pieChart.setData(data);
                pieChart.invalidate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors if needed
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

    private void setupPieChart() {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);

        // Disable the display of legends
        pieChart.getLegend().setEnabled(false);
    }

    // Function to generate random colors
    private int[] getRandomColors(int count) {
        int[] colors = new int[count];
        for (int i = 0; i < count; i++) {
            colors[i] = Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
        }
        return colors;
    }
}