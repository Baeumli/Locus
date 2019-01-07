package com.locusapp.locus.activities.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.locusapp.locus.R;
import com.locusapp.locus.models.FirebaseDAO;

import java.util.ArrayList;

public class BountyDetailActivity extends AppCompatActivity {

    private TextView lblTitle, lblHint, lblCreator;
    private Button btnStartSearch;

    private double latitude, longitude;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bounty_detail);

        lblTitle = findViewById(R.id.lblTitle);
        lblHint = findViewById(R.id.lblHint);
        lblCreator = findViewById(R.id.lblCreator);
        btnStartSearch = findViewById(R.id.btnStartSearch);

        String id = getIntent().getStringExtra("id");

        FirebaseDAO firebaseDAO = new FirebaseDAO();

        firebaseDAO.getBountyDetails(id, new FirebaseDAO.FirebaseDetailCallback() {
            @Override
            public void onCallback(String title, String hint, String creator, double lat, double lng, String image) {

                latitude = lat;
                longitude = lng;
                imagePath = image;

                lblTitle.setText(title);
                lblHint.setText(hint);
                lblCreator.setText(creator);
            }

        });

        btnStartSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (longitude != 0.0 || latitude != 0.0|| imagePath != null) {
                    Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                    intent.putExtra("longitude", longitude);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("image", imagePath);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}

