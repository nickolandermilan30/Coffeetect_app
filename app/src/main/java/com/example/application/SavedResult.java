package com.example.application;

import android.graphics.Bitmap;

public class SavedResult {
    private String diseaseName;
    private Bitmap imageBitmap;

    public SavedResult(String diseaseName, Bitmap imageBitmap) {
        this.diseaseName = diseaseName;
        this.imageBitmap = imageBitmap;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }
}
