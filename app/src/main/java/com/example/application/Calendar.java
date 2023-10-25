package com.example.application;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

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
    }

    private void addDataToPieChart() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(22, "Sakit 1"));
        entries.add(new PieEntry(30, "Sakit 2"));
        entries.add(new PieEntry(15, "Sakit 3"));
        entries.add(new PieEntry(12, "Sakit 4"));

        PieDataSet dataSet = new PieDataSet(entries, "Sakit Distribution");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);

        pieChart.setData(data);
        pieChart.invalidate();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Add your custom behavior for the back button here, if needed.
        // For example, you can navigate to a different activity or finish this one.
    }
}
