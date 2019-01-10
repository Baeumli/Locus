package com.locusapp.locus.activities.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.GeoPoint;
import com.locusapp.locus.R;
import com.locusapp.locus.models.CameraHandler;
import com.locusapp.locus.models.FirebaseDAO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CreateBountyActivity extends AppCompatActivity {

    private final String TAG = "BountyListActivity";

    // Permission variables
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private EditText etTitle, etHint, etWinMessage;
    private ImageView imgView;
    private Boolean mLocationPermissionsGranted = false;
    private LruCache<String, Bitmap> mMemoryCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_bounty);

        etTitle = findViewById(R.id.etTitle);
        etHint = findViewById(R.id.etHint);
        etWinMessage = findViewById(R.id.etWinMessage);
        FloatingActionButton btnTakePhoto = findViewById(R.id.btnTakePhoto);
        Button btnCreateBounty = findViewById(R.id.btnCreateBounty);
        imgView = findViewById(R.id.imgView);

        // Get Permission to use GPS
        getLocationPermission();

        if (savedInstanceState != null) {
            Bitmap bitmap = savedInstanceState.getParcelable("image");
            imgView.setImageBitmap(bitmap);
        }

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        btnCreateBounty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etTitle.getText().toString().matches("")) {
                    Toast.makeText(CreateBountyActivity.this, "Please enter a title.", Toast.LENGTH_SHORT).show();
                } else if (etHint.getText().toString().matches("")) {
                    Toast.makeText(CreateBountyActivity.this, "Please enter a hint.", Toast.LENGTH_SHORT).show();
                } else if (imgView.getDrawable() == null) {
                    Toast.makeText(CreateBountyActivity.this, "Please add a photo.", Toast.LENGTH_SHORT).show();
                } else if (etWinMessage.getText().toString().matches("")) {
                    Toast.makeText(CreateBountyActivity.this, "Please enter a win message.", Toast.LENGTH_SHORT).show();
                } else {
                    createBounty();
                }
            }
        });
    }

    public void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = CameraHandler.getCameraHandler().createImageFile(this);

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this, "com.locusapp.locus.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            int width = imgView.getWidth();
            int height = imgView.getHeight();

            Bitmap bitmap = CameraHandler.getCameraHandler().setPic(width, height);

            imgView.setImageBitmap(bitmap);

        }
    }

    private void createBounty() {

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
                final Task<Location> location = fusedLocationProviderClient.getLastLocation();

                location.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful() && task.getResult() != null){
                            Location currentLocation = (Location) task.getResult();

                            String userEmail = "";
                            if (FirebaseAuth.getInstance().getCurrentUser().getEmail() != null) {
                                userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail() ;
                            }

                            Map<String, Object> bounty = new HashMap<>();

                            bounty.put("creator", userEmail);
                            bounty.put("hint", etHint.getText().toString());
                            bounty.put("image", "/images/" + CameraHandler.getCameraHandler().getImageFileName());
                            bounty.put("location", new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude()));
                            bounty.put("title", etTitle.getText().toString());
                            bounty.put("win_message", etWinMessage.getText().toString());

                            Bitmap bitmap = ((BitmapDrawable)imgView.getDrawable()).getBitmap();
                            String imageFileName = CameraHandler.getCameraHandler().getImageFileName();

                            FirebaseDAO firebaseDAO = new FirebaseDAO();

                            firebaseDAO.saveBounty(bounty);
                            firebaseDAO.uploadImage(bitmap, imageFileName);
                            finish();

                            Log.d(TAG, "onComplete: location = " + currentLocation.getLatitude() + " " + currentLocation.getLongitude());
                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(CreateBountyActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (imgView.getDrawable() != null) {
            Bitmap bitmap = ((BitmapDrawable)imgView.getDrawable()).getBitmap();
            outState.putParcelable("image", bitmap);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
