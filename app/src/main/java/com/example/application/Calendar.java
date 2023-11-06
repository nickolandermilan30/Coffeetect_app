package com.example.application;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Calendar extends AppCompatActivity {

    private PieChart pieChart;
    private FirebaseAuth auth;
    private DatabaseReference historyRef;
    private ArrayList<PieEntry> entries = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // Hide the action bar (app bar or title bar)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        auth = FirebaseAuth.getInstance();
        pieChart = findViewById(R.id.pieChart);

        // Authenticate the user anonymously (if not already authenticated)
        if (auth.getCurrentUser() == null) {
            auth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        Toast.makeText(Calendar.this, user.getUid(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        // Initialize Firebase Realtime Database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        historyRef = database.getReference("sakit_history");

        // Listen for changes in the sakit history data
        historyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Process the data and update the pie chart
                entries.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String sakit = snapshot.getValue(String.class);
                    // Process the sakit data as needed
                    entries.add(new PieEntry(1, sakit)); // 1 represents the count, you can adjust this as needed
                }

                // Update the pie chart with the latest data
                PieDataSet dataSet = new PieDataSet(entries, "Sakit History");
                PieData pieData = new PieData(dataSet);
                pieChart.setData(pieData);
                pieChart.invalidate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
}
