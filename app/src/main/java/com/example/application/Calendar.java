package com.example.application;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class Calendar extends AppCompatActivity {

    PieChart pieChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // Hide the action bar (app bar or title bar)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        pieChart = findViewById(R.id.pieChart);

        // Customize the chart as needed
        setupPieChart();

        // Add data to the PieChart
        addDataToPieChart();

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

        // Itigil ang pagpapakita ng legends
        pieChart.getLegend().setEnabled(false);
    }

    private void addDataToPieChart() {
        String[] sakitNames = {
                "Healthy Leaf",
                "Sooty Mold",
                "Cercospora",
                "Leaf Miner",
                "Leaf Rust",
                "Phoma"
        };

        ArrayList<PieEntry> entries = new ArrayList<>();
        int totalSeverityLevels = 100;

        for (int i = 0; i < sakitNames.length; i++) {
            int severityLevel = totalSeverityLevels / sakitNames.length;
            entries.add(new PieEntry(severityLevel, sakitNames[i]));
        }

        // Baguhin ang kulay dito, gamitin ang mga hexadecimal color codes
        int[] colors = {0xFF00796B, 0xFF8D6E63, 0xFF009688, 0xFFE57373, 0xFF795548, 0xFF0277BD};

        PieDataSet dataSet = new PieDataSet(entries, "Sakit Distribution");
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);

        pieChart.setData(data);
        pieChart.invalidate();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
