package com.example.application;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class RecommendationActivity extends AppCompatActivity {

    // Map to store recommendations based on disease and severity
    private Map<String, Map<Integer, String>> recommendationsMap = new HashMap<>();

    private RatingBar ratingBar;
    private TextView ratingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        // Hide the action bar (app bar or title bar)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }



        initializeRecommendations();

        TextView diseaseTextView = findViewById(R.id.diseaseTextView); // New TextView for disease
        TextView severityTextView = findViewById(R.id.severityTextView); // New TextView for severity
        TextView recommendationTextView = findViewById(R.id.recommendationTextView);
        ImageView mostFrequentImageView = findViewById(R.id.mostFrequentImageView);


        Intent intent = getIntent();
        if (intent != null) {
            String diseaseName = intent.getStringExtra("diseaseName");
            int severityLevel = intent.getIntExtra("severityLevel", 0);

            String recommendation = getRecommendation(diseaseName, severityLevel);
            diseaseTextView.setText("Disease: " + diseaseName);
            severityTextView.setText("Severity: " + severityLevel);
            recommendationTextView.setText(recommendation);

            Bitmap mostFrequentImage = SavedResultsManager.getImageForMostFrequentDisease();
            if (mostFrequentImage != null) {
                mostFrequentImageView.setImageBitmap(mostFrequentImage);
            }
        }

        // OnClickListener para sa leg button
        ImageButton legButton = findViewById(R.id.leg);
        legButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomAlertDialog();
            }
        });

        Button okButton = findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView mostFrequentTextView = findViewById(R.id.mostFrequentTextView);
        String mostFrequentDisease = SavedResultsManager.getMostFrequentDisease();
        if (mostFrequentDisease != null) {
            mostFrequentTextView.setText("Most Frequent Disease: " + mostFrequentDisease);
        }
    }

    private void showCustomAlertDialog() {
        // Inflate the custom layout for the dialog
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_alert_layout, null);

        // Find views in the custom layout
        ImageView imageView = dialogView.findViewById(R.id.customAlertDialogImageView);
        TextView textView = dialogView.findViewById(R.id.customAlertDialogTextView);

        // Set the image and text in the custom layout
        textView.setText("This is The meaning of 1,2,3 on severity");

        // Create the custom alert dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(dialogView);

        // Set any other properties for the dialog (title, buttons, etc.)

        // Show the dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void initializeRecommendations() {
        // Recommendations for Sooty Mold
        Map<Integer, String> sootyMoldRecommendations = new HashMap<>();
        sootyMoldRecommendations.put(1, "Recommendation for Mild Severity of Sooty Mold");

        sootyMoldRecommendations.put(2, "Recommendation for Moderate Severity of Sooty Mold");

        sootyMoldRecommendations.put(3, "Coffee leaf sooty mold is a fungal disease that appears as a black or dark-colored coating on the leaves, caused by the growth of fungi on honeydew excreted by insects such as mealybugs and scale insects. If left untreated, coffee leaf sooty mold can reduce the photosynthesis process of the plant, resulting in reduced coffee yields.\n" +
                "Coffee leaf Phoma can be controlled in critical conditions by following these simple steps:\n" +
                "\n" +
                "Control the insect infestation: Insects such as mealybugs and scale insects excrete honeydew, which attracts fungal growth, resulting in sooty mold. Therefore, controlling insect infestations is crucial to managing coffee leaf sooty mold. Insecticides or biological control methods, such as introducing natural predators, can be used to control insect infestations.\n" +
                "Prune infected leaves: Prune and remove the infected leaves as soon as you notice them. This will help to prevent the spread of the disease to other parts of the plant.\n" +
                "Apply fungicides: Fungicides can be used to control the growth of sooty mold. Different fungicides are available, and the choice of fungicide will depend on the severity of the infection and the stage of the crop. Consult with an agricultural specialist to determine the appropriate fungicide and application method.\n" +
                "Monitor the plant regularly: Regular monitoring of the plant can help to detect and treat the disease early before it becomes critical. Monitor the plant for any signs of disease or stress, such as black sooty mold or insect infestations.\n" +
                "\n" +
                "NOTE: It is essential to consult with an agricultural specialist or a plant pathologist for an accurate diagnosis of the disease and the appropriate treatment options.");








        // Recommendations for Phoma
        Map<Integer, String> phomaRecommendations = new HashMap<>();
        phomaRecommendations.put(1, "Recommendation for Mild Severity of Phoma");

        phomaRecommendations.put(2, "Recommendation for Moderate Severity of Phoma");

        phomaRecommendations.put(3, "Coffee leaf phoma is a fungal disease that affects coffee plants and can cause significant damage to the leaves, resulting in reduced coffee yields. Treatment for coffee leaf phoma in critical condition typically involves a combination of cultural, chemical, and biological control methods.\n" +
                "\n" +
                "Coffee leaf Phoma can be controlled in critical conditions by following these simple steps:\n" +
                "\n" +
                "Prune infected leaves: Prune and remove the infected leaves as soon as you notice them. This will help to prevent the spread of the disease to other parts of the plant.\n" +
                "Apply fungicides: Fungicides can be used to control the spread of the disease. Different fungicides are available, and the choice of fungicide will depend on the severity of the infection and the stage of the crop. Consult with an agricultural specialist to determine the appropriate fungicide and application method.\n" +
                "Consider biological control: Biological control methods, such as introducing natural predators or beneficial microbes, can also help to control the spread of the disease.\n" +
                "Monitor the plant regularly: Regular monitoring of the plant can help to detect and treat the disease early before it becomes critical. Monitor the plant for any signs of disease or stress, such as yellowing or wilting leaves.\n" +
                "\n" +
                "NOTE: It is essential to consult with an agricultural specialist or a plant pathologist for an accurate diagnosis of the disease and the appropriate treatment options.");







        // Recommendations for Leaf Miner
        Map<Integer, String> leafMinerRecommendations = new HashMap<>();
        leafMinerRecommendations.put(1, "Recommendation for Mild Severity of Leaf Miner");

        leafMinerRecommendations.put(2, "Recommendation for Moderate Severity of Leaf Miner");

        leafMinerRecommendations.put(3, "Recommendation for Critical Severity of Leaf Miner");









        // Recommendations for Leaf Rust
        Map<Integer, String> leafRustRecommendations = new HashMap<>();
        leafRustRecommendations.put(1, "Recommendation for Mild Severity of Leaf Rust");

        leafRustRecommendations.put(2, "Recommendation for Moderate Severity of Leaf Rust");

        leafRustRecommendations.put(3, "Recommendation for Critical Severity of Leaf Rust");






        // Recommendations for Cercospora
        Map<Integer, String> cercosporaRecommendations = new HashMap<>();
        cercosporaRecommendations.put(1, "Recommendation for Mild Severity of Cercospora");

        cercosporaRecommendations.put(2, "Coffee leaf cercospora is a fungal disease that causes circular or angular brown spots on the leaves of coffee plants.\n" +
                "\n" +
                "Coffee leaf Cercospora can be controlled in moderate conditions by following these simple steps:\n" +
                "Maintain proper plant nutrition: Make sure your coffee plants are getting the proper nutrients they need to stay healthy. Fertilize your plants regularly with a balanced fertilizer and make sure they are getting enough water.\n" +
                "Improve plant hygiene: Remove any infected leaves and dispose of them properly. This will help prevent the spread of the disease to healthy leaves.\n" +
                "Apply fungicides: There are several fungicides that can be used to treat coffee leaf cercospora, including copper-based fungicides and triazole fungicides. Follow the instructions on the label carefully when applying fungicides.\n" +
                "\n" +
                "NOTE: It is essential to consult with an agricultural specialist or a plant pathologist for an accurate diagnosis of the disease and the appropriate treatment options.");

        cercosporaRecommendations.put(3, "Recommendation for Critical Severity of Cercospora");







        recommendationsMap.put("Sooty Mold", sootyMoldRecommendations);
        recommendationsMap.put("Phoma", phomaRecommendations);
        recommendationsMap.put("Leaf Miner", leafMinerRecommendations);
        recommendationsMap.put("Leaf Rust", leafRustRecommendations);
        recommendationsMap.put("Cercospora", cercosporaRecommendations);
    }



    private String getRecommendation(String diseaseName, int severityLevel) {
        Map<Integer, String> severityRecommendations = recommendationsMap.get(diseaseName);
        if (severityRecommendations != null) {
            String recommendation = severityRecommendations.get(severityLevel);
            if (recommendation != null) {
                return recommendation;
            }
        }
        return "No Recommendation Available";
    }


}