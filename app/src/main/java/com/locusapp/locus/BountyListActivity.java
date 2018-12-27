package com.locusapp.locus;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BountyListActivity extends AppCompatActivity {

    private final String TAG = "BountyListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bounty_list);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Timestamp timestamp = new Timestamp(new Date());
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail() ;

        double latitude = -5.53807;
        double longitude = 51.66747;
        GeoPoint geoPoint = new GeoPoint(latitude, longitude);

        Map<String, Object> bounty = new HashMap<>();
        bounty.put("created_at", timestamp);
        bounty.put("creator", userEmail);
        bounty.put("hint", "Ein Hinweis");
        bounty.put("image", "Ein Bildlink");
        bounty.put("location", geoPoint);
        bounty.put("title", "Ein Titel");


        // Add a new document with a generated ID
        db.collection("Bounties")
                .add(bounty)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }
}
