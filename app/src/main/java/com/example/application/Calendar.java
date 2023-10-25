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
    ArrayList<PieEntry> entries;  // Ito ay para sa mga sakit na na-detect

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

        // Inisyal na pag-setup ng PieChart
        entries = new ArrayList<>();
        setupPieChartWithData();  // Ito ay magpapakita ng mga sakit na initial



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

    private void setupPieChartWithData() {
        // Ito ay ilalagay ang initial data ng PieChart (kung meron man)
        int totalSeverityLevels = 100;  // Total na sakit levels
        int initialSeverityLevels = 50;  // Halimbawa na data, dapat naging variable ito

        if (entries.isEmpty()) {
            // Ito ay halimbawa lamang. Dapat ay ang mga sakit at kanilang mga severity ay mula sa iyong application
            entries.add(new PieEntry(initialSeverityLevels, "Healthy Leaf"));
            entries.add(new PieEntry(totalSeverityLevels - initialSeverityLevels, "Sooty Mold"));
            entries.add(new PieEntry(totalSeverityLevels - initialSeverityLevels, "Cercospora"));
            entries.add(new PieEntry(totalSeverityLevels - initialSeverityLevels, "Leaf Miner"));
            entries.add(new PieEntry(totalSeverityLevels - initialSeverityLevels, "Leaf rust"));
            entries.add(new PieEntry(totalSeverityLevels - initialSeverityLevels, "Phoma"));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Sakit Distribution");
        dataSet.setColors(getRandomColors(entries.size()));  // I-customize ang kulay
        PieData data = new PieData(dataSet);

        pieChart.setData(data);
        pieChart.invalidate();
    }
    // Halimbawa na function para sa random colors, puwede mo ito palitan ayon sa iyong gusto
    private int[] getRandomColors(int count) {
        int[] colors = new int[count];
        for (int i = 0; i < count; i++) {
            colors[i] = 0xFF000000 + (int)(Math.random() * 0xFFFFFF);
        }
        return colors;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    // Kapag nais mong idagdag ang mga bagong resulta, itawag ito upang mag-update ng PieChart
    private void updatePieChartWithNewData(String sakitName, int severityLevel) {
        // Dapat ay ma-update mo ang entries depende sa mga nadidiskubre mong mga sakit
        entries.add(new PieEntry(severityLevel, sakitName));
        setupPieChartWithData();
    }
}
