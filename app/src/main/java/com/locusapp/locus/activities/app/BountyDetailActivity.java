package com.locusapp.locus.activities.app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.locusapp.locus.MapActivity;
import com.locusapp.locus.R;

import org.w3c.dom.Text;

public class BountyDetailActivity extends AppCompatActivity {

    private TextView lblTitle, lblHint, lblCreator;
    private Button btnStartSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bounty_detail);

        lblTitle = findViewById(R.id.lblTitle);
        lblHint = findViewById(R.id.lblHint);
        lblCreator = findViewById(R.id.lblCreator);
        btnStartSearch = findViewById(R.id.btnStartSearch);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        String id = getIntent().getStringExtra("id");

        firestore.collection("Bounties")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            String title = document.getString("title");
                            String hint = document.getString("hint");
                            String creator = (String) document.getString("creator");
                            double lng = document.getGeoPoint("location").getLongitude();
                            double lat = document.getGeoPoint("location").getLatitude();
                            String image = document.getString("image");
                            lblTitle.setText(title);
                            lblHint.setText(hint);
                            lblCreator.setText(creator);

                            btnStartSearch.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                                    intent.putExtra("longitude", lng);
                                    intent.putExtra("latitude", lat);
                                    intent.putExtra("image", image);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    }
                });

    }
}
