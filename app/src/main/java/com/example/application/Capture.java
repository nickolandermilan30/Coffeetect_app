package com.example.application;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Capture extends AppCompatActivity implements TextureView.SurfaceTextureListener  {

    private Camera camera;
    private TextureView textureView;
    private Button captureButton;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        // Check for camera permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            } else {
                initializeCamera();
            }
        }

        textureView = findViewById(R.id.textureView);
        textureView.setSurfaceTextureListener(this);

        captureButton = findViewById(R.id.captureButton);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capturePhoto();
            }
        });
    }

    private void initializeCamera() {
        try {
            camera = Camera.open();
            camera.setDisplayOrientation(90);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeCamera();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
        try {
            if (camera != null) {
                camera.setPreviewTexture(surface);
                camera.startPreview();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {
        // Handle surface size changes (if needed)
    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
        releaseCamera();
        return true;
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {
        // Handle texture updates (if needed)
    }

    private void capturePhoto() {
        if (camera != null) {
            try {
                camera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        // Save the image to a temporary file
                        String imagePath = saveImageLocally(data);

                        // Set up the Intent for ResultActivity and put imagePath in extras
                        Intent intent = new Intent(Capture.this, Result_Activity.class);
                        intent.putExtra("imagePath", imagePath);
                        startActivity(intent);

                        // Restart the camera preview
                        camera.startPreview();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String saveImageLocally(byte[] data) {
        FileOutputStream fos = null;
        File imagePath = null;
        try {
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            String imageFileName = "CoffeeDetect_" + System.currentTimeMillis() + ".jpg";
            imagePath = new File(storageDir, imageFileName);

            fos = new FileOutputStream(imagePath);
            fos.write(data);

            return imagePath.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}